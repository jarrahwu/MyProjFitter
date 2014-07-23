package com.jarrah.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON数据的注解属性
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JProperty {

	/**
	 * 对应的json数据的key的名字是什么
	 * @return
	 */
	String key() default "";

	/**
	 * 对应的属性是否存在于嵌套的json对象中<br/>
	 * example : <br/>
	 * {"name":"rico", age:12, "scores":{"math":90,"english":80, "other":{"computer":90, "art":"77"}}}<br/>
	 * 我要获取math多少分的话,就可以这样写:<br/>
	 * ----- @JProperty(key="math",subObjectName="scores") -----<br/>
	 * 我要获取computer多少分的话,就可以这样写:<br/>
	 * ----- @JProperty(key="computer",subObjectName="scores.other") -----<br/>
	 * @return
	 */
	String subObjectName() default "";
}
