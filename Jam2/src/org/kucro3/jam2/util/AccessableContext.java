package org.kucro3.jam2.util;

public abstract class AccessableContext
{
	protected AccessableContext()
	{
	}
	
	public final int getModifier()
	{
		return modifier;
	}
	
	public final void setModifier(int modifier)
	{
		this.modifier = modifier;
	}
	
	public final AccessableContext appendModifier(int modifier)
	{
		this.modifier |= modifier;
		return this;
	}
	
	protected int modifier;
}
