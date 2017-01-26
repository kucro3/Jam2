package org.kucro3.jam2.util;

import org.objectweb.asm.Opcodes;

public class Version {
	public static int getJam2Version()
	{
		return 1;
	}
	
	public static int getClassVersion()
	{
		return classVersion;
	}
	
	public static int getASMVersion()
	{
		return API;
	}
	
	private static final int API = Opcodes.ASM5;
	
	static {
		int topClassVersion = Opcodes.V1_8;
		
		int index;
		String vmV = System.getProperty("java.version");
		vmV = vmV.substring(index = vmV.indexOf('.') + 1, index + 1);
		int iVmV = Integer.parseInt(vmV);
		switch(iVmV)
		{
		case 0:
		case 1:
			classVersion = Opcodes.V1_1;
			break;
		case 2:
			classVersion = Opcodes.V1_2;
			break;
		case 3:
			classVersion = Opcodes.V1_3;
			break;
		case 4:
			classVersion = Opcodes.V1_4;
			break;
		case 5:
			classVersion = Opcodes.V1_5;
			break;
		case 6:
			classVersion = Opcodes.V1_6;
			break;
		case 7:
			classVersion = Opcodes.V1_7;
			break;
		case 8:
			classVersion = Opcodes.V1_8;
			break;
		default:
			classVersion = topClassVersion;
//			throw new IllegalStateException("Unsupported vm version.");
		}
	}
	
	private static int classVersion;
}
