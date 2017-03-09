package org.kucro3.jam2.util.annotation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.kucro3.util.exception.RuntimeExceptions;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class Annotation {
	public Annotation()
	{
	}
	
	public Annotation(String descriptor)
	{
		this.descriptor = descriptor;
	}
	
	public Annotation(String descriptor, boolean visible)
	{
		this.descriptor = descriptor;
		this.visible = visible;
	}
	
	public void visitTo(AnnotationVisitor av)
	{
		for(Map.Entry<String, Value> entry : values.entrySet())
			entry.getValue().visit(av, entry.getKey());
	}
	
	protected boolean checkAttribute()
	{
		return descriptor != null;
	}
	
	public void visitOn(ClassVisitor cv)
	{
		if(checkAttribute())
			visitTo(cv.visitAnnotation(descriptor, visible));
	}
	
	protected AnnotationVisitor preVisit(ClassVisitor cv)
	{
		return cv.visitAnnotation(descriptor, visible);
	}
	
	public void visitOn(FieldVisitor fv)
	{
		if(checkAttribute())
			visitTo(preVisit(fv));
	}
	
	protected AnnotationVisitor preVisit(FieldVisitor fv)
	{
		return fv.visitAnnotation(descriptor, visible);
	}
	
	public void visitOn(MethodVisitor mv)
	{
		if(checkAttribute())
			visitTo(preVisit(mv));
	}
	
	protected AnnotationVisitor preVisit(MethodVisitor mv)
	{
		return mv.visitAnnotation(descriptor, visible);
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public void setDescriptor(String descriptor)
	{
		this.descriptor = descriptor;
	}
	
	public Value getValue(String name)
	{
		return values.get(name);
	}
	
	public Value removeValue(String name)
	{
		return values.remove(name);
	}
	
	public boolean containsValue(String name)
	{
		return values.containsKey(name);
	}
	
	public Value putValue(String name, Value value)
	{
		return values.put(name, value);
	}
	
	public void clearValues()
	{
		values.clear();
	}
	
	public Collection<Value> values()
	{
		return values.values();
	}
	
	public Set<Map.Entry<String, Value>> mapped()
	{
		return values.entrySet();
	}
	
	protected boolean visible; // optional
	
	protected String descriptor; // optional
	
	private final Map<String, Value> values = new HashMap<>();
	
	public static class Value
	{
		public Value(Object value)
		{
			if(value != null)
				setValue(value);
		}
		
		public Value() // unvisited array
		{
			initializeAsArray();
		}
		
		public Value(String desc, String value) // enum
		{
			this.bits = INSTANCE_ENUM_VALUE;
			this.value = new EnumValue(desc, value);
		}
		
		protected final void transformArray(Object value)
		{
			assert isPrimitive();
			
			LinkedList<Object> list;
			this.value = list = new LinkedList<>();
			if(isNumber()) // such as boolean[]... except char[]
			{
				int length = Array.getLength(value);
				for(int i = 0; i < length; i++)
					list.add(Array.get(value, i));
			}
			else if(isPrimitive()) // must be char[]
				for(char c : ((char[]) value))
					list.add(Character.valueOf(c));
			else if(value instanceof String[] 
					|| value instanceof Type[] 
					|| value instanceof EnumValue[]
					|| value instanceof Annotation[])
				for(int i = 0; i < ((Object[]) value).length; i++)
					list.add(ensureValue(((Object[]) value)[i]));
			else
				throw new IllegalArgumentException("Illegal type: " + value.getClass().getCanonicalName());
		}
		
		@SuppressWarnings("unchecked")
		public void addValue(Object obj)
		{
			if(isArray())
			{
				List<Object> list = (List<Object>) value;
				if((bits & MASK_PRIM_INSTANCE_BITS) == 0) // array init
				{
					assert list.size() == 0;
					
					bits |= ensureInstance(obj, true);
				}
				else
				{
					assert list.size() != 0;
					
					if((ensureInstance(obj, true) & MASK_PRIM_INSTANCE_BITS) != (bits & MASK_PRIM_INSTANCE_BITS))
						throw new IllegalArgumentException("Illegal type: " + obj.getClass().getCanonicalName());
					
					if(isEnum())
						if(!((EnumValue)obj).desc.equals(((EnumValue) list.get(0)).desc))
							throw new IllegalArgumentException("Illegal enum type: " + ((EnumValue) obj).desc);
				}
				list.add(ensureValue(obj));
			}
			else
				throw new UnsupportedOperationException("Target is not an array (see isArray())");
		}
		
		@SuppressWarnings("unchecked")
		public void addValueUnchecked(Object value)
		{
			if(isArray())
				((List<Object>) value).add(ensureValue(value));
			else
				throw new UnsupportedOperationException("Target is not an array (see isArray())");
		}
		
		public void clear()
		{
			this.bits = 0;
			this.value = null;
		}
		
		public boolean isInitialized()
		{
			return bits != 0;
		}
		
		public void initializeAsArray()
		{
			if(isInitialized())
				throw new IllegalStateException("Already initialized");
			this.bits = INSTANCE_ARRAY;
			this.value = new LinkedList<>();
		}
		
		public void setValue(Object obj)
		{
			int bits = ensureInstance(Objects.requireNonNull(obj), false);
			if(isArray())
				transformArray(obj);
			else
				this.value = ensureValue(obj);
			this.bits = bits;
		}
		
		@SuppressWarnings("unchecked") 
		public void visit(AnnotationVisitor av, String name)
		{
			ensureSelf();
			
			if(isArray())
			{
				AnnotationVisitor arrayVisitor = av.visitArray(name);
				List<Object> list = (List<Object>) value;
				for(int i = 0; i < list.size(); i++)
					visitValue(arrayVisitor, null, list.get(i));
			}
			else
				visitValue(av, name, value);
		}
		
		void visitValue(AnnotationVisitor av, String name, Object value)
		{
			assert !value.getClass().isArray();
			
			ensureToVisit(value);
			if(isEnum())
			{
				EnumValue enumValue = (EnumValue) value;
				av.visitEnum(name, enumValue.desc, enumValue.value);
			}
			else if(isNestedAnnotation())
			{
				Annotation nestedAnnotation = (Annotation) value;
				AnnotationVisitor nestedVisitor = av.visitAnnotation(name, nestedAnnotation.descriptor);
				nestedAnnotation.visitTo(nestedVisitor);
			}
			else
				av.visit(name, value);
		}
		
		<T> T ensureValue(T obj)
		{
			// reserved
			return obj;
		}
		
		<T> T ensureToVisit(T obj)
		{
			obj = ensureValue(obj);
			if(isNestedAnnotation())
				if(!((Annotation) obj).checkAttribute())
					throw new IllegalArgumentException("A Nested Annotation must be fully initialized");
				else;
			else if(isEnum())
				if(((EnumValue) obj).value == null)
					throw new IllegalArgumentException("The value of an Enumeration mustn't be null");
				else;
			else;
			return obj;
		}
		
		void ensureSelf()
		{
			if(!isInitialized())
				if(value != null)
					; // should not reach here
				else
					throw new IllegalStateException("Empty value container");
			else if(value == null)
				; // should not reach here
			else
				return;
			assert false : RuntimeExceptions.shouldNotReachHere();
		}
		
		static int ensureInstance(Object value, boolean cannotBeArray)
		{
			String descriptor = Type.getDescriptor(value.getClass());
			BLOCK: try {
				int index, val;
				Integer tempVal;
				if(cannotBeArray || (index = descriptor.lastIndexOf('[')) < 0)
					if((tempVal = DescriptorConstants.mappedTypes.get(descriptor)) != null)
						return tempVal.intValue();
					else
						break BLOCK;
				else if(index == 0)
					if((val = DescriptorConstants.types(descriptor.charAt(1))) == 0)
						if((tempVal = DescriptorConstants.mappedTypes.get(descriptor.substring(1))) != null)
							if((tempVal & INSTANCE_PRIM) == 0)
								return tempVal.intValue() | INSTANCE_ARRAY;
							else
								break BLOCK;
						else
							break BLOCK;
					else
						return val | INSTANCE_ARRAY;
				else
					break BLOCK;
				
//				assert false : "Should not reach here.";
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
			throw new IllegalArgumentException("Illegal annotation element type: " + descriptor);
		}
		
		public Object getValue()
		{
			return value;
		}
		
		public boolean isNumber()
		{
			return is(INSTANCE_NUMBER);
		}
		
		public boolean isPrimitive()
		{
			return is(INSTANCE_PRIM);
		}
		
		public boolean isArray()
		{
			return is(INSTANCE_ARRAY);
		}
		
		public boolean isNestedAnnotation()
		{
			return is(INSTANCE_NESTED_ANNOTATION);
		}
		
		public boolean isByte()
		{
			return is(INSTANCE_BYTE);
		}
		
		public boolean isBoolean()
		{
			return is(INSTANCE_BOOLEAN);
		}
		
		public boolean isShort()
		{
			return is(INSTANCE_SHORT);
		}
		
		public boolean isInteger()
		{
			return is(INSTANCE_INTEGER);
		}
		
		public boolean isLong()
		{
			return is(INSTANCE_LONG);
		}
		
		public boolean isFloat()
		{
			return is(INSTANCE_FLOAT);
		}
		
		public boolean isDouble()
		{
			return is(INSTANCE_DOUBLE);
		}
		
		public boolean isCharacter()
		{
			return is(INSTANCE_CHARACTER);
		}
		
		public boolean isType()
		{
			return is(INSTANCE_TYPE);
		}
		
		public boolean isEnum()
		{
			return is(INSTANCE_ENUM_VALUE);
		}
		
		public int getInstance()
		{
			return bits;
		}
		
		private boolean is(int instance)
		{
			assert // bit state check
				(bits != 0)
			&&	(((bits & MASK_PRIM_INSTANCE_BITS) & ((bits & MASK_PRIM_INSTANCE_BITS) - 1)) == 0)
			:	"Illegal state";
			
			return (bits & instance) != 0;
		}
		
		private Object value;
		
		private int bits;
		
		public static final int INSTANCE_BYTE = 0x00000001;
		
		public static final int INSTANCE_BOOLEAN = 0x00000002;
		
		public static final int INSTANCE_SHORT = 0x00000004;
		
		public static final int INSTANCE_INTEGER = 0x00000008;
		
		public static final int INSTANCE_LONG = 0x00000010;
		
		public static final int INSTANCE_FLOAT = 0x00000020;
		
		public static final int INSTANCE_DOUBLE = 0x00000040;
		
		public static final int INSTANCE_CHARACTER = 0x00000080;
		
		public static final int INSTANCE_STRING = 0x00000100;
		
		public static final int INSTANCE_TYPE = 0x00000200;
		
		public static final int INSTANCE_ENUM_VALUE = 0x00000400;
		
		public static final int INSTANCE_NESTED_ANNOTATION = 0x00000800;
		
		public static final int INSTANCE_ARRAY = 0x01000000;
		
		private static final int INSTANCE_PRIM = 0x02000000;
		
		private static final int INSTANCE_NUMBER = 0x04000000;
		
		public static final int MASK_PRIM_INSTANCE_BITS = 0x0000FFFF;
		
		private static class DescriptorConstants
		{
			static int types(int index)
			{
				int realIndex = index - 'A';
				if(realIndex < 0)
					return 0;
				else if(realIndex < types.length)
					return types[realIndex];
				else
					return 0;
			}
			
			static {
				int[] _types = new int['Z' - 'A' + 1];
				_types['B' - 'A'] = INSTANCE_BYTE | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['C' - 'A'] = INSTANCE_CHARACTER | INSTANCE_PRIM;
				_types['D' - 'A'] = INSTANCE_DOUBLE | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['F' - 'A'] = INSTANCE_FLOAT | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['I' - 'A'] = INSTANCE_INTEGER | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['J' - 'A'] = INSTANCE_LONG | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['S' - 'A'] = INSTANCE_SHORT | INSTANCE_PRIM | INSTANCE_NUMBER;
				_types['Z' - 'A'] = INSTANCE_BOOLEAN | INSTANCE_PRIM;
				types = _types;
				
				Map<String, Integer> _mapped = new HashMap<>();
				_mapped.put("Ljava/lang/Byte;", INSTANCE_BYTE | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Character;", INSTANCE_CHARACTER | INSTANCE_PRIM);
				_mapped.put("Ljava/lang/Double;", INSTANCE_DOUBLE | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Float;", INSTANCE_FLOAT | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Integer;", INSTANCE_INTEGER | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Long;", INSTANCE_LONG | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Short;", INSTANCE_SHORT | INSTANCE_PRIM | INSTANCE_NUMBER);
				_mapped.put("Ljava/lang/Boolean;", INSTANCE_BOOLEAN | INSTANCE_PRIM);
				_mapped.put("Ljava/lang/String;", INSTANCE_STRING);
				_mapped.put("Lorg/objectweb/asm/Type;", INSTANCE_TYPE);
				_mapped.put("Lorg/kucro3/jam2/util/annotation/Annotation$Value$EnumValue;", INSTANCE_ENUM_VALUE);
				_mapped.put("Lorg/kucro3/jam2/util/annotation/Annotation;", INSTANCE_NESTED_ANNOTATION);
				mappedTypes = _mapped;
			}
			
			private static final int[] types;
			
			private static final Map<String, Integer> mappedTypes;
		}
		
		public static class EnumValue
		{
			public EnumValue(Enum<?> value)
			{
				Objects.requireNonNull(value);
				this.desc = Type.getDescriptor(value.getClass());
				this.value = value.name();
				this.enumValue = value;
			}
			
			public EnumValue(String desc, String value)
			{
				this.desc = Objects.requireNonNull(desc);
				this.value = value;
			}
			
			@SuppressWarnings("unchecked")
			public static <T extends Enum<T>> EnumValue[] values(Enum<T>... enums)
			{
				EnumValue[] enumValues = new EnumValue[enums.length];
				for(int i = 0; i < enums.length; i++)
					enumValues[i] = new EnumValue(enums[i]);
				return enumValues;
			}
			
			public String getDescriptor()
			{
				return desc;
			}
			
			public String getValue()
			{
				return value;
			}
			
			public void setValue(String value)
			{
				if(!value.equals(this.value))
				{
					this.value = value;
					this.enumValue = null;
				}
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Enum<?> getEnum() throws ClassNotFoundException
			{
				if(enumValue != null)
					return enumValue;
				Class enumClass = Class.forName(Type.getType(desc).getClassName());
				enumValue = Enum.valueOf(enumClass, value);
				return enumValue;
			}
			
			final String desc;
			
			String value;
			
			Enum<?> enumValue;
		}
	}
}