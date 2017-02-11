package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.util.Reference;

@SuppressWarnings({"unchecked"})
public enum HookPolicy implements HookFunction {
	NONE {
		@Override
		public void onAction(
				ClassContext ref0,
				FieldContext ref1,
				MethodContext ref2,
			Action action, Reference<Object>... argument)
		{
		}
	},
	CC_NO_CLEARING_METHODS {
		@Override
		public void onAction(
				ClassContext ref0,
				FieldContext ref1,
				MethodContext ref2,
			Action action, Reference<Object>... argument) 
		{
			if(ref0 != null)
				if(action == Action.CC_clearMethods)
					HookRejectionException.rejected(this, "You cannot clear methods.");
		}
	},
	CC_NO_CLEARING_FIELDS {
		@Override
		public void onAction(
				ClassContext ref0,
				FieldContext ref1,
				MethodContext ref2,
			Action action, Reference<Object>... argument) 
		{
			if(ref0 != null)
				if(action == Action.CC_clearFields)
					HookRejectionException.rejected(this, "You cannot clear fields.");
		}
	},
	CC_ONLY_CHANGE_NAME_WHEN_EMPTY {
		@Override
		public void onAction(
				ClassContext ref0,
				FieldContext ref1,
				MethodContext ref2,
			Action action, Reference<Object>... argument) 
		{
			if(ref0 != null)
				if(action == Action.CC_setName)
					if(ref0.hasMethod() || ref0.hasField())
						HookRejectionException.rejected(this, "You can only change the internal name when"
								+ " this class context does not contain any field or method.");
		}
	};
	
	private HookPolicy()
	{
	}
}