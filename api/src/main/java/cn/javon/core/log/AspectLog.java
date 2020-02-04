package cn.javon.core.log;

import cn.javon.core.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * 请求日志切面
 *
 * @author Javon
 */
@Slf4j
@Aspect
@Component
public class AspectLog {

    public final String string = "execution(*  *..*.*.controller..*.*(..))";

    private static final String UTF_8 = "utf-8";

    @Pointcut(string)
    public void excudeService() {
    }

    /**
     * 执行切点之前
     *
     * @param pjp
     */
    @Before("excudeService()")
    public void exBefore(JoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    /**
     * 通知（环绕）
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long endTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String params = "";
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        try {
            long startTime = (long) request.getAttribute("startTime");
            //获取请求参数集合并进行遍历拼接
            if (args.length > 0) {
                if ("POST".equals(method)) {
                    Object object = args[0];
                    params = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
                } else if ("GET".equals(method)) {
                    params = queryString;
                }
                if (StringUtil.isNotEmpty(params)) {
                    params = URLDecoder.decode(params, UTF_8);
                }
            }
            if (log.isInfoEnabled()) {
                log.info("requestMethod:{},url:{},params:{},responseBody:{},elapsed:{}ms.", method, uri, params,
                        JSON.toJSONString(result, SerializerFeature.WriteMapNullValue), (endTime - startTime));
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("请求异常：", e);
            }
        }
        return result;
    }


    /**
     * 执行切点之后
     */
    @After("excudeService()")
    public void exAfter(JoinPoint joinPoint) {
    }

}