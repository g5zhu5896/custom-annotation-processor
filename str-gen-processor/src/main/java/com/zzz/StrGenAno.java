package com.zzz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zzx
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface StrGenAno {
}
