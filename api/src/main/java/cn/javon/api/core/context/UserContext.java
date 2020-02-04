package cn.javon.api.core.context;

import cn.javon.api.sys.vo.UserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户
 *
 * @author Javon
 */
public class UserContext {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserVo getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() == null) {
            return null;
        }
        return (UserVo) authentication.getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getId();
    }

}
