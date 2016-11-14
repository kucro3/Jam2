package org.kucro3.jam2.jar;

import org.kucro3.jam2.opcode.Instruction;
import org.kucro3.jam2.opcode.InstructionContainer;

public interface ClassMethod {
	int getAccess();
	
	String getDescriptor();
	
	String getReturnType();
	
	String[] getArguments();
	
	String[] getExceptions();
	
	@Deprecated Instruction[] getInstructions();
	
	InstructionContainer getInstructionContainer();
	
	String getName();
	
	String getSignature();
	
	default void setAccess(int access) {throw new UnsupportedOperationException();}
	
	default void setExceptions(String[] exceptions) {throw new UnsupportedOperationException();}
	
	default void setSignature(String signature) {throw new UnsupportedOperationException();}

	public static interface Modifiable extends ClassMethod
	{
		void setAccess(int access);
		
		void setExceptions(String[] exceptions);
		
		void setSignature(String signature);
	}
}
