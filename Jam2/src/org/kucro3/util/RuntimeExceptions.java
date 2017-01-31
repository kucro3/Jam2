package org.kucro3.util;

public class RuntimeExceptions {
	public static RuntimeException shouldNotReachHere()
	{
		return new RuntimeException("Should not reach here.");
	}
}