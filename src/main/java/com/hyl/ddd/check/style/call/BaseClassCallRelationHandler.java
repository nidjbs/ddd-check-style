package com.hyl.ddd.check.style.call;

import java.util.Optional;

/**
 * @author huayuanlin on 2022/10/22
 */
public abstract class BaseClassCallRelationHandler implements CallRelationHandler {

    protected final CallRelationHandler nextHandler;

    /**
     * @param nextHandler null able
     */
    public BaseClassCallRelationHandler(CallRelationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected boolean doNextHandler(CheckClass source, CheckClass target) {
        return Optional.ofNullable(nextHandler)
                .map(handler -> handler.allowCall(source, target))
                .orElse(Boolean.FALSE);
    }


}