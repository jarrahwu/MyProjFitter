package com.chzh.fitter.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 写入SharedPreference 里面的注解
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSharedPreference {


	/**
	 * 设置在sharedPreference里面的key, 不写的话,默认是'',在SharedPreference保存的key为变量名
	 * @return
	 */
	String key() default "";
}
