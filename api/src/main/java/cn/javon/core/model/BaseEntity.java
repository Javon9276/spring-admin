package cn.javon.core.model;

import cn.javon.core.exception.ValidatorException;
import cn.javon.core.vo.BaseVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 基础对象类
 *
 * @author Javon
 */
@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {

    /**
     * ID
     * <p>
     * 可选择自动生成或UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据状态： 0 可用 1 不可用
     */
    @Column(name = "status", nullable = false, columnDefinition = "INT default 0")
    private Integer status = 0;

    /**
     * 是否删除: 0 未删除 1 已删除
     */
    @Column(name = "is_delete", nullable = false, columnDefinition = "INT default 0")
    private Integer isDelete = 0;

    /**
     * 版本控制
     */
    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    /**
     * 创建人
     */
    @Column(name = "creater")
    private Long creater;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "updater")
    private Long updater;

    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 确认版本是否已经更新
     *
     * @param baseVo 基础类VO
     */
    public void checkVersion(BaseVo baseVo) {
        if (!this.version.equals(baseVo.getVersion())) {
            throw new ValidatorException("该记录已经被更新，请刷新页面");
        }
    }

}
