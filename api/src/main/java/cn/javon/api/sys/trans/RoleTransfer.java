package cn.javon.api.sys.trans;

import cn.javon.api.sys.entity.Role;
import cn.javon.api.sys.vo.RoleVo;
import cn.javon.core.transalte.AbstractObjectTransfer;
import org.springframework.beans.BeanUtils;

/**
 * 角色PoVo转换类
 *
 * @author Javon
 */
public class RoleTransfer extends AbstractObjectTransfer<RoleVo, Role> {

    final PermissionTransfer permissionTransfer = new PermissionTransfer();

    @Override
    public Role toPo(RoleVo vo) {
        if (vo == null) {
            return null;
        }
        final Role po = new Role();
        BeanUtils.copyProperties(vo, po, Role.class);
        po.setPermissions(permissionTransfer.toPo(vo.getPermissions()));
        return po;
    }

    @Override
    public RoleVo toVo(Role po) {
        if (po == null) {
            return null;
        }
		final RoleVo vo = new RoleVo();
        BeanUtils.copyProperties(po, vo, RoleVo.class);
        vo.setPermissions(permissionTransfer.toVo(po.getPermissions()));
        return vo;
    }

}
