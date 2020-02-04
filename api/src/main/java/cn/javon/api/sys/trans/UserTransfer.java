package cn.javon.api.sys.trans;

import cn.javon.api.sys.entity.User;
import cn.javon.api.sys.vo.UserVo;
import cn.javon.core.transalte.AbstractObjectTransfer;
import org.springframework.beans.BeanUtils;

/**
 * 用户PoVo转换类
 *
 * @author Javon
 */
public class UserTransfer extends AbstractObjectTransfer<UserVo, User> {

    final RoleTransfer roleTransfer = new RoleTransfer();

    @Override
    public User toPo(UserVo vo) {
        if (vo == null) {
            return null;
        }
        final User po = new User();
        BeanUtils.copyProperties(vo, po, User.class);
        po.setRoles(roleTransfer.toPo(vo.getRoles()));
        return po;
    }

    @Override
    public UserVo toVo(User po) {
        if (po == null) {
            return null;
        }
        final UserVo vo = new UserVo();
        BeanUtils.copyProperties(po, vo, UserVo.class);
        vo.setRoles(roleTransfer.toVo(po.getRoles()));
        return vo;
    }


}
