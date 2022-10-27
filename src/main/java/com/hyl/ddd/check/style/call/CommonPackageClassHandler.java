package com.hyl.ddd.check.style.call;

/**
 * @author huayuanlin on 2022/10/22
 */
public class CommonPackageClassHandler extends BaseClassCallRelationHandler {


    public CommonPackageClassHandler(CallRelationHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean allowCall(CheckClass source, CheckClass target) {
        CallRelationConfig config = CallRelationConfig.getInstance();
        String classPath = target.getClassPath();
        boolean isCommonPackage = config.getCommonPackagePatterns()
                .stream().
                anyMatch(pattern -> pattern.matcher(classPath).find());
        return isCommonPackage || nextHandler.allowCall(source, target);
    }


}