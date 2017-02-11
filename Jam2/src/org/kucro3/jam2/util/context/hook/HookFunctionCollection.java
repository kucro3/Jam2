package org.kucro3.jam2.util.context.hook;

import java.util.ArrayList;
import java.util.HashSet;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.util.Reference;

@SuppressWarnings({"unchecked"})
public class HookFunctionCollection {
	public void hook(HookFunction... funcs)
	{
		for(HookFunction func : funcs)
			hook(func);
	}
	
	public void hook(HookFunction func)
	{
		if(duplicationCheck.contains(func))
			return;
		
		funcs.add(func);
		duplicationCheck.add(func);
	}
	
	public void unhook(HookFunction... funcs)
	{
		for(HookFunction func : funcs)
			unhook(func);
	}
	
	public void unhook(HookFunction func)
	{
		if(duplicationCheck.remove(func))
			funcs.remove(func);
	}
	
	public void unhookAll()
	{
		funcs.clear();
		duplicationCheck.clear();
	}
	
	public Object[] fire(
				ClassContext ref0,
				FieldContext ref1,
				MethodContext ref2,
			Action action, Object... args)
	{
		if(funcs.isEmpty())
			return args;
		
		Reference<Object>[] refs;
		
		if(args != null)
		{
			refs = new Reference[args.length];
			for(int i = 0; i < refs.length; i++)
				refs[i] = new Reference<>(args[i]);
		}
		else
			refs = null;
		
		for(HookFunction func : funcs)
			func.onAction(ref0, ref1, ref2, action, refs);
		
		if(refs == null)
			return null;
		
		Object[] ret = new Object[refs.length];
		
		for(int i = 0; i < refs.length; i++)
			ret[i] = refs[i].get();
		
		return ret;
	}
	
	private final ArrayList<HookFunction> funcs = new ArrayList<>();
	
	private final HashSet<HookFunction> duplicationCheck = new HashSet<>();
}
