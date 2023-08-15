package zhishusz.housepresell.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解相关设置
@Target({ElementType.FIELD})   
@Retention(RetentionPolicy.RUNTIME)   
@Documented 
public @interface IFieldAnnotation
{
	String remark() default "注释信息";
	String tableName() default "";
	String columnName() default "";
	boolean isPrimarykey() default false;
	boolean isNeeded() default false;
}
