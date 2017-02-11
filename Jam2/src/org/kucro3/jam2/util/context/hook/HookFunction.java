package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.util.Reference;

@SuppressWarnings({"unchecked"})
public interface HookFunction {
	void onAction(
			ClassContext ref0,
			FieldContext ref1,
			MethodContext ref2,
		Action action, Reference<Object>... argument);
}
