package org.kucro3.jam2.jar;

import java.io.IOException;

public class JarIOException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8374626611532879612L;
	
	public JarIOException(IOException e)
	{
		super(e);
	}
}
