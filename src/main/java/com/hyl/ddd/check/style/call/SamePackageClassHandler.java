package com.hyl.ddd.check.style.call;

/**
 * @author huayuanlin on 2022/10/22
 */
public class SamePackageClassHandler extends BaseClassCallRelationHandler {

    public SamePackageClassHandler(CallRelationHandler nextHandler) {
        super(nextHandler);
    }


    @Override
    public boolean allowCall(CheckClass source, CheckClass target) {
        CallRelationConfig config = CallRelationConfig.getInstance();
        String sourceSubPackage = source.getExcludedBasePackage();
        String targetSubPackage = target.getExcludedBasePackage();
        boolean allowSamePackage = config.isAllowSamePackage();
        boolean isSamePackageWithPass = allowSamePackage && sourceSubPackage.equals(targetSubPackage);
        return isSamePackageWithPass || doNextHandler(source, target);
    }


}