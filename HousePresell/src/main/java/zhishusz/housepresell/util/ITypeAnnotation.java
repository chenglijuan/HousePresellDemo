package zhishusz.housepresell.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解相关设置
@Target({ElementType.TYPE})   
@Retention(RetentionPolicy.RUNTIME)   
@Documented 
public @interface ITypeAnnotation
{
	String remark() default "注释信息";
	String tableName() default "";
	boolean isNeeded() default true;
}
