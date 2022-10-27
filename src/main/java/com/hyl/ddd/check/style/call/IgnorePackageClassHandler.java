package com.hyl.ddd.check.style.call;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author huayuanlin on 2022/10/22
 */
public class IgnorePackageClassHandler extends BaseClassCallRelationHandler {


    public IgnorePackageClassHandler(CallRelationHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean allowCall(CheckClass source, CheckClass target) {
        CallRelationConfig config = CallRelationConfig.getInstance();
        String basePackage = config.getBasePackage();
        String sourcePackageName = source.getPackageName();
        String targetPackageName = target.getPackageName();
        if (!sourcePackageName.startsWith(basePackage)
                || !targetPackageName.startsWith(basePackage)) {
            return true;
        }
        List<Pattern> ignorePackagePatterns = config.getIgnorePackagePatterns();
        String sourceClassPath = source.getClassPath();
        boolean isIgnoreClass = ignorePackagePatterns.stream()
                .anyMatch(pattern -> pattern.matcher(sourceClassPath).find());
        return isIgnoreClass || doNextHandler(source, target);
    }
}