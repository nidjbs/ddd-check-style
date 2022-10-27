package com.hyl.ddd.check.style.call;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author huayuanlin on 2022/10/22
 */
public class AllowDependClassPackageHandler extends BaseClassCallRelationHandler {


    public AllowDependClassPackageHandler(CallRelationHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public boolean allowCall(CheckClass source, CheckClass target) {
        CallRelationConfig config = CallRelationConfig.getInstance();
        String sourceExcludedBasePackage = source.getExcludedBasePackage();
        String targetExcludedBasePackage = target.getExcludedBasePackage();
        Map<String, List<String>> allowCallPackages = config.getAllowCallPackages();
        boolean allowCall = allowCallPackages.entrySet().stream().anyMatch(entry -> {
            String sourceConfig = entry.getKey();
            List<String> targetConfigs = entry.getValue();
            Pattern patternCache = CallRelationConfig.getOrInitPattern(sourceConfig);
            if (patternCache.matcher(sourceExcludedBasePackage).find()) {
                return targetConfigs.stream().map(CallRelationConfig::getOrInitPattern)
                        .anyMatch(pattern -> pattern.matcher(targetExcludedBasePackage).find());
            }
            return false;
        });
        return allowCall || doNextHandler(source, target);
    }
}