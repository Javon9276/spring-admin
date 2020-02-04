package cn.javon.core.exception;

/**
 * 验证异常类
 *
 * @author Javon
 */
public class ValidatorException extends RuntimeException {

    public ValidatorException(final String message) {
        super(message);
    }

    public ValidatorException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
