package cn.javon.core.log;

import cn.javon.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志注解解析器，日志可以异步保存到数据库，这里只是打印了而已，
 *
 * @author Javon
 */
@Aspect
@Component
@Slf4j
public class AuditLogResolver {

    /**
     * 切点
     */
    private final String EXECUTION = "@annotation(cn.javon.core.log.AuditLog)";

    @Around(EXECUTION)
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
        //执行方法
        Object result = null;
        if (log.isInfoEnabled()) {
            //获取要执行的方法
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            final String className = joinPoint.getSignature().getDeclaringTypeName();
            final String methodName = String.format("%s.%s", className, method.getName());
            //获取注解信息
            final AuditLog upgradeProgress = method.getAnnotation(AuditLog.class);
            // 当前操作
            final String operate = upgradeProgress.operate();
            //开始日志
            final String startLog = StringUtil.isNotEmpty(operate) ? String.format("start %s %s method", operate, methodName) : String.format("start execute %s method", methodName);
            //记录方法执行前日志
            log.info(startLog);
            //获取方法参数名称
            final String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            // 参数值：
            final Object[] argValues = joinPoint.getArgs();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < argNames.length; i++) {
                String value = argValues[i] == null ? "null" : argValues[i].toString();
                sb.append(argNames[i]).append("=").append(value).append(",");
            }
            final String paramStr = sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) + "]" : "";
            log.info("参数信息为：{}", paramStr);
            //执行方法
            result = joinPoint.proceed();
            //结束日志
            final String endLog = StringUtil.isNotEmpty(operate) ? String.format("execute %s %s method successfully", operate, methodName) : String.format("execute %s method successfully", methodName);

            String resultStr = result == null ? "null" : result.toString();
            log.info("返回结果为：{}", resultStr);
            //记录方法执行后日志
            log.info(endLog);
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }
}
