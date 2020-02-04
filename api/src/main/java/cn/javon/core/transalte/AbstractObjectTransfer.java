package cn.javon.core.transalte;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * PoVo转换抽象类
 *
 * @author Javon
 */
public abstract class AbstractObjectTransfer<V, P> {

    /**
     * Po转Vo
     *
     * @param po 实体Po
     * @return Vo
     */
    public abstract V toVo(P po);

    public List<V> toVo(List<P> pos) {
        if (pos == null) {
            return null;
        }
        List<V> vos = new ArrayList<V>();
        for (P po : pos) {
            vos.add(toVo(po));
        }
        return vos;
    }

    public List<V> toVo(Set<P> pos) {
        if (pos == null) {
            return null;
        }
        List<V> vos = new ArrayList<V>();
        for (P po : pos) {
            vos.add(toVo(po));
        }
        return vos;
    }

	/**
	 * Vo转Po
	 *
	 * @param vo Vo
	 * @return po实体
	 */
    public abstract P toPo(V vo);

    public Set<P> toPo(List<V> vos) {
        if (vos == null) {
            return null;
        }
        Set<P> pos = new LinkedHashSet<P>();
        for (V vo : vos) {
            pos.add(toPo(vo));
        }
        return pos;
    }

    public Set<P> toPo(Set<V> vos) {
        if (vos == null) {
            return null;
        }
        Set<P> pos = new LinkedHashSet<P>();
        for (V vo : vos) {
            pos.add(toPo(vo));
        }
        return pos;
    }

}
