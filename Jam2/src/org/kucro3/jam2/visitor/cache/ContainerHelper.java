package org.kucro3.jam2.visitor.cache;

import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.ClassEssentialsContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.FieldContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.InfoAnnotationContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.InfoInnerClassContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.InfoOuterClassContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.InfoSourceContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.InfoTypeAnnotationContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.MethodContainer;

public class ContainerHelper {
	public static void copyTo(MethodContainer src, MethodContainer dst)
	{
		dst.access = src.access;
		dst.exceptions = src.exceptions;
		dst.mcv = src.mcv;
		dst.signature = src.signature;
	}
	
	public static MethodContainer copyFully(ClassCacheVisitor caller, MethodContainer src)
	{
		MethodContainer cpy = caller.new MethodContainer(src.name, src.desc);
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(FieldContainer src, FieldContainer dst)
	{
		dst.access = src.access;
		dst.desc = src.desc;
		dst.fcv = src.fcv;
		dst.signature = src.signature;
		dst.value = src.value;
	}
	
	public static FieldContainer copyFully(ClassCacheVisitor caller, FieldContainer src)
	{
		FieldContainer cpy = caller.new FieldContainer(src.name);
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(ClassEssentialsContainer src, ClassEssentialsContainer dst)
	{
		dst.access = src.access;
		dst.interfaces = src.interfaces;
		dst.name = src.name;
		dst.signature = src.signature;
		dst.superName = src.superName;
		dst.version = src.version;
	}
	
	public static ClassEssentialsContainer copyFully(ClassCacheVisitor caller, ClassEssentialsContainer src)
	{
		ClassEssentialsContainer cpy = caller.new ClassEssentialsContainer();
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(InfoAnnotationContainer src, InfoAnnotationContainer dst)
	{
		dst.acv = src.acv;
		dst.desc = src.desc;
		dst.visible = src.visible;
	}
	
	public static InfoAnnotationContainer copyFully(ClassCacheVisitor caller, InfoAnnotationContainer src)
	{
		InfoAnnotationContainer cpy = caller.new InfoAnnotationContainer();
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(InfoInnerClassContainer src, InfoInnerClassContainer dst)
	{
		dst.access = src.access;
		dst.innerName = src.innerName;
		dst.name = src.name;
		dst.outerName = src.outerName;
	}
	
	public static InfoInnerClassContainer copyFully(ClassCacheVisitor caller, InfoInnerClassContainer src)
	{
		InfoInnerClassContainer cpy = caller.new InfoInnerClassContainer();
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(InfoOuterClassContainer src, InfoOuterClassContainer dst)
	{
		dst.desc = src.desc;
		dst.name = src.name;
		dst.owner = src.owner;
	}
	
	public static InfoOuterClassContainer copyFully(ClassCacheVisitor caller, InfoOuterClassContainer src)
	{
		InfoOuterClassContainer cpy = caller.new InfoOuterClassContainer();
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(InfoSourceContainer src, InfoSourceContainer dst)
	{
		dst.debug = src.debug;
		dst.source = src.source;
	}
	
	public static InfoSourceContainer copyFully(ClassCacheVisitor caller, InfoSourceContainer src)
	{
		InfoSourceContainer cpy = caller.new InfoSourceContainer();
		copyTo(src, cpy);
		return cpy;
	}
	
	public static void copyTo(InfoTypeAnnotationContainer src, InfoTypeAnnotationContainer dst)
	{
		copyTo((InfoAnnotationContainer) src, (InfoAnnotationContainer) dst);
		dst.typeRef = src.typeRef;
		dst.typePath = src.typePath;
	}
	
	public static InfoTypeAnnotationContainer copyFully(ClassCacheVisitor caller, InfoTypeAnnotationContainer src)
	{
		InfoTypeAnnotationContainer cpy = caller.new InfoTypeAnnotationContainer();
		copyTo(src, cpy);
		return cpy;
	}
}