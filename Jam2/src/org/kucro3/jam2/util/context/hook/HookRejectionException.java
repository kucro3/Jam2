package org.kucro3.jam2.util.context.hook;

public class HookRejectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -640972110660956113L;
	
	public static void rejected(HookPolicy cause, String message)
	{
		throw new HookRejectionException(cause, message);
	}
	
	public static void rejected(String message)
	{
		throw new HookRejectionException(null, message);
	}
	
	public HookRejectionException(HookPolicy cause, String message)
	{
		super("Rejected by hook: " + cause == null ? "CUSTOM" : cause.name() + " (" + message + ")");
	}
}
