package cn.javon.core.log;

import java.lang.annotation.*;

/**
 * 方法日志处理注解
 * operate：操作所需要的名称
 *
 * @author Javon
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    String operate() default "";

}
