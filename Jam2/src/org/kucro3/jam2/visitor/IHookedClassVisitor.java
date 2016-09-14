package org.kucro3.jam2.visitor;

public interface IHookedClassVisitor {
	void setListener(ClassVisitorListener listener);
	
	ClassVisitorListener getListener();
}
