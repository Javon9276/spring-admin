package cn.javon.api.sys.trans;

import cn.javon.api.sys.entity.Permission;
import cn.javon.api.sys.vo.PermissionVo;
import cn.javon.core.transalte.AbstractObjectTransfer;
import org.springframework.beans.BeanUtils;

/**
 * 权限PoVo转换类
 *
 * @author Javon
 */
public class PermissionTransfer extends AbstractObjectTransfer<PermissionVo, Permission> {

    @Override
    public Permission toPo(PermissionVo vo) {
        if (vo == null) {
            return null;
        }
        final Permission po = new Permission();
        BeanUtils.copyProperties(vo, po, Permission.class);
        return po;
    }

    @Override
    public PermissionVo toVo(Permission po) {
        if (po == null) {
            return null;
        }
        final PermissionVo vo = new PermissionVo();
        BeanUtils.copyProperties(po, vo, PermissionVo.class);
        return vo;
    }

}
