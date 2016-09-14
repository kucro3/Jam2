package org.kucro3.jam2.visitor;

public interface IHookedFieldVisitor {
	void setListener(FieldVisitorListener listener);
	
	FieldVisitorListener getListener();
}
