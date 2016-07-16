package org.kucro3.jam2.util;

import java.util.Objects;

import org.objectweb.asm.MethodVisitor;

public class MaxsContext
{
	public static MaxsContext newContext(MaxsHandleMode mode)
	{
		return new MaxsContext(mode);
	}
	
	public static MaxsContext newDefaultContext()
	{
		return new MaxsContext(MaxsHandleMode.DELTA);
	}
	
	MaxsContext(MaxsHandleMode mode)
	{
		this.mode = mode;
	}
	
	public void visitMaxs(MethodVisitor mv)
	{
		mv.visitMaxs(computedStack, computedLocal);
	}
	
	public MaxsHandleMode getMode()
	{
		return mode;
	}
	
	public void setMode(MaxsHandleMode mode)
	{
		Objects.requireNonNull(mode);
		this.mode = mode;
	}
	
	public int getComputedMaxStack()
	{
		return computedStack;
	}
	
	public int getComputedMaxLocal()
	{
		return computedLocal;
	}
	
	public int getMaxStack()
	{
		return maxStack;
	}
	
	public int getMaxLocal()
	{
		return maxLocal;
	}
	
	public int getStackDelta()
	{
		return stackDelta;
	}
	
	public int getLocalDelta()
	{
		return localDelta;
	}
	
	public void setDelta(int stackDelta, int localDelta)
	{
		setStackDelta(stackDelta);
		setLocalDelta(localDelta);
	}
	
	public void setStackDelta(int stackDelta)
	{
		this.stackDelta = stackDelta;
	}
	
	public void setLocalDelta(int localDelta)
	{
		this.localDelta = localDelta;
	}
	
	public void setMaxs(int maxStack, int maxLocal)
	{
		setMaxStack(maxStack);
		setMaxLocal(maxLocal);
	}
	
	public void setMaxStack(int maxStack)
	{
		this.maxStack = maxStack;
	}
	
	public void setMaxLocal(int maxLocal)
	{
		this.maxLocal = maxLocal;
	}
	
	void updateMaxs(int stack, int local)
	{
		switch(mode)
		{
		case CUSTOM:
		case CUSTOM_NODELTA:
			break;
		default:
			this.maxStack = stack;
			this.maxLocal = local;
		}
	}
	
	public void compute()
	{
		switch(mode)
		{
		case COMPUTE:
		case CUSTOM_NODELTA:
			computedStack = maxStack;
			computedLocal = maxLocal;
			break;
			
		case CUSTOM:
		case DELTA:
			computedStack = maxStack + stackDelta;
			computedLocal = maxStack + localDelta;
			break;
			
		default:
			throw new NullPointerException();
			// Impossible
		}
	}
	
	private MaxsHandleMode mode;
	
	private int maxStack;
	
	private int maxLocal;
	
	private int stackDelta;
	
	private int localDelta;
	
	private int computedStack;
	
	private int computedLocal;
	
	public static enum MaxsHandleMode
	{
		COMPUTE,
		CUSTOM,
		CUSTOM_NODELTA,
		DELTA
	}
}
