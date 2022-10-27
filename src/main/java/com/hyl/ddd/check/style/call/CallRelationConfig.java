package com.hyl.ddd.check.style.call;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyl.ddd.check.style.common.Constants;
import com.hyl.ddd.check.style.common.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huayuanlin on 2022/10/22
 */
public class CallRelationConfig {

    protected static CallRelationConfig instance;

    private final String basePackage;

    private final Map<String, List<String>> allowCallPackages;

    private static final Map<String, Pattern> REGEX_CACHE = Maps.newHashMap();

    private final List<Pattern> ignorePackagePatterns;

    private boolean allowSamePackage = true;

    private final List<Pattern> commonPackagePatterns;


    public CallRelationConfig(boolean allowSamePackage,
                              String basePackage,
                              String allowCallPackageStr,
                              String ignorePathStr,
                              String commonPackagesStr) {
        this.allowSamePackage = allowSamePackage;
        this.basePackage = basePackage;
        this.allowCallPackages = initCallRelation(allowCallPackageStr);
        this.ignorePackagePatterns = parseToPattern(ignorePathStr);
        this.commonPackagePatterns = parseToPattern(commonPackagesStr);
    }

    public static CallRelationConfig getInstance() {
        if (Objects.isNull(instance)) {
            throw new IllegalStateException("config instance not init.");
        }
        return instance;
    }

    public static Pattern getOrInitPattern(String config) {
        Pattern patternCache = REGEX_CACHE.get(config);
        return Optional.ofNullable(patternCache).orElseGet(() -> {
            Pattern pattern;
            if (config.startsWith(instance.basePackage)) {
                pattern = Pattern.compile(config);
            } else {
                pattern = Pattern.compile(instance.basePackage + Constants.DOT + config);
            }
            REGEX_CACHE.put(config, pattern);
            return pattern;
        });
    }

    private Map<String, List<String>> initCallRelation(String allowCallPackageStr) {
        if (StringUtils.isEmpty(allowCallPackageStr)) {
            return new HashMap<>(2);
        }
        Map<String, List<String>> allowCallPackages = new HashMap<>(8);
        Stream.of(allowCallPackageStr.split(Constants.SLIP))
                .filter(call -> StringUtils.isNotEmpty(call) && call.contains(Constants.POINT_TO))
                .forEach(caller -> {
                    int indexOf = caller.indexOf(Constants.POINT_TO);
                    String source = caller.substring(0, indexOf);
                    String target = caller.substring(indexOf + Constants.POINT_TO.length());
                    REGEX_CACHE.put(source, Pattern.compile(source));
                    REGEX_CACHE.put(target, Pattern.compile(target));
                    List<String> targets = allowCallPackages.get(source);
                    if (CollectionUtils.isEmpty(targets)) {
                        allowCallPackages.put(source, Lists.newArrayList(target));
                    } else {
                        targets.add(target);
                    }
                    allowCallPackages.putIfAbsent(source, targets);
                });
        return allowCallPackages;
    }


    private List<Pattern> parseToPattern(String ignorePathStr) {
        if (StringUtils.isNotEmpty(ignorePathStr)) {
            return Stream.of(ignorePathStr.split(Constants.SLIP))
                    .filter(StringUtils::isNotEmpty)
                    .map(str -> str.replaceAll(" ", ""))
                    .map(str -> ignorePathStr.startsWith(basePackage) ? str : basePackage + Constants.DOT + str)
                    .map(Pattern::compile)
                    .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public String getBasePackage() {
        return basePackage;
    }

    public Map<String, List<String>> getAllowCallPackages() {
        return allowCallPackages;
    }

    public List<Pattern> getIgnorePackagePatterns() {
        return ignorePackagePatterns;
    }

    public boolean isAllowSamePackage() {
        return allowSamePackage;
    }

    public List<Pattern> getCommonPackagePatterns() {
        return commonPackagePatterns;
    }
}