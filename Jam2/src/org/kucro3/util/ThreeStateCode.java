package org.kucro3.util;

public class ThreeStateCode {
	private ThreeStateCode()
	{
	}
	
	public static boolean isError(int i)
	{
		return i == ERROR;
	}
	
	public static boolean isFalse(int i)
	{
		return i == FALSE;
	}
	
	public static boolean isTrue(int i)
	{
		return i == TRUE;
	}
	
	public static final int ERROR = -1;
	
	public static final int FALSE = 0;
	
	public static final int TRUE = 1;
}
