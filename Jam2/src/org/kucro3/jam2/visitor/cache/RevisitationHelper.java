package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public final class RevisitationHelper {
	private RevisitationHelper()
	{
	}
	
	public static void revisit(MethodRevisitable mr, MethodVisitor mv)
	{
		mr.revisitParameters(mv);
		mr.revisitAnnotations(mv);
		mr.revisitAttributes(mv);
		mr.revisitInstructions(mv);
		mr.revisitTryCatchBlocks(mv);
		mr.revisitLocals(mv);
		mr.revisitLocalAnnotations(mv);
		mr.revisitLineNumbers(mv);
		mr.revisitMaxs(mv);
		mr.revisitEnd(mv);
	}
	
	public static void revisitOptional(MethodRevisitable mr, MethodVisitor mv)
	{
		if(mr != null && mv != null)
			revisit(mr, mv);
	}
	
	public static void revisit(FieldRevisitable fr, FieldVisitor fv)
	{
		fr.revisitAttribute(fv);
		fr.revisitAnnotations(fv);
		fr.revisitEnd(fv);
	}
	
	public static void revisitOptional(FieldRevisitable fr, FieldVisitor fv)
	{
		if(fr != null & fv != null)
			revisit(fr, fv);
	}
	
	public static void revisit(ClassRevisitable cr, ClassVisitor cv)
	{
		cr.revisitEssentials(cv);
		cr.revisitSource(cv);
		cr.revisitOuterClass(cv);
		cr.revisitAnnotations(cv);
		cr.revisitAttributes(cv);
		cr.revisitInnerClasses(cv);
		cr.revisitFields(cv);
		cr.revisitMethods(cv);
		cr.revisitEnd(cv);
	}
	
	public static void revisitOptional(ClassRevisitable cr, ClassVisitor cv)
	{
		if(cr != null && cv != null)
			revisit(cr, cv);
	}
}
