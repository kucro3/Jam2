package org.kucro3.util.exception;

import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImplicitThrows {
	Class<?>[] value();
}