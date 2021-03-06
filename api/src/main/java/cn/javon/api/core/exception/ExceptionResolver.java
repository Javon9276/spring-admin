package cn.javon.api.core.exception;

import cn.javon.core.exception.ValidatorException;
import cn.javon.core.response.Result;
import cn.javon.core.response.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 统一异常处理器
 *
 * @author Javon
 */
@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    /**
     * 校验实体数据异常问题
     *
     * @param e
     * @return 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidatorException.class)
    public Result validatorException(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("验证数据 => {}", e.getMessage());
        }
        return ResultGenerator.genFailedResult(e.getMessage());
    }

    /**
     * 校验实体数据异常问题
     *
     * @param e
     * @return 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result validatorException(final ConstraintViolationException e) {
        if (log.isErrorEnabled()) {
            log.error("验证实体异常 => {}", e.getMessage());
        }
        final String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return ResultGenerator.genFailedResult(msg);
    }

    /**
     * 服务业务处理异常
     *
     * @param e
     * @return 500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ServletException.class})
    public Result serviceException(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("服务异常 => {}", e.getMessage(), e);
        }
        return ResultGenerator.genFailedResult(e.getMessage());
    }

    /**
     * 数据库处理异常
     *
     * @param e
     * @return 500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public Result databaseException(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("数据库异常 => {}", e.getMessage(), e);
        }
        return ResultGenerator.genFailedResult("database error");
    }

    /**
     * 身份验证异常
     *
     * @param e
     * @return 401
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public Result authException(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("身份验证异常 => {}", e.getMessage());
        }
        return ResultGenerator.genUnauthorizedResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class, UsernameNotFoundException.class})
    public Result userException(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("用户异常 => {}", e.getMessage());
        }
        return ResultGenerator.genFailedResult(e.getMessage());
    }

    /**
     * 请求不存在异常
     *
     * @param e
     * @return 401
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result apiNotFound(final Throwable e, final HttpServletRequest request) {
        if (log.isErrorEnabled()) {
            log.error("API 不存在 => {}", e.getMessage(), e);
        }
        return ResultGenerator.genFailedResult("API [" + request.getRequestURI() + "] not existed");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result methodNotSupport(final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("方法异常 => {}", e.getMessage(), e);
        }
        return ResultGenerator.genMethodErrorResult();
    }

    /**
     * 全局异常
     *
     * @param request
     * @param e
     * @return 500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result globalException(final HttpServletRequest request, final Throwable e) {
        if (log.isErrorEnabled()) {
            log.error("全局异常：{}", e.getMessage(), e);
        }
        return ResultGenerator.genInternalServerErrorResult(String.format("%s => %s", request.getRequestURI(), e.getMessage()));
    }
}
