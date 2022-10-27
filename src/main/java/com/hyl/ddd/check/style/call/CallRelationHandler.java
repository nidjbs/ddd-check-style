package com.hyl.ddd.check.style.call;

/**
 * @author huayuanlin on 2022/10/22
 */
public interface CallRelationHandler {

    /**
     * 允许调用?
     *
     * @param source 源
     * @param target 目标
     * @return boolean
     */
    boolean allowCall(CheckClass source, CheckClass target);

}
