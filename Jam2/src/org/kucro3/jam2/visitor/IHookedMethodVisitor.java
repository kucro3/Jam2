package org.kucro3.jam2.visitor;

public interface IHookedMethodVisitor {
	void setListener(MethodVisitorListener listener);
	
	MethodVisitorListener getListener();
}
