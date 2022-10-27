package com.hyl.ddd.check.style.call;

import com.hyl.ddd.check.style.common.Constants;

import java.util.Objects;
import java.util.Optional;

/**
 * @author huayuanlin on 2022/10/22
 */
public class CheckClass {

    private final String packageName;

    private final String className;

    private final String classPath;

    private final int lineNum;

    private String excludedBasePackageCache;


    public CheckClass(String packageName, String className, int lineNum) {
        this.className = className;
        this.packageName = packageName;
        this.lineNum = lineNum;
        this.classPath = this.packageName + Constants.DOT + this.className;
    }

    public CheckClass(String classPath, int lineNum) {
        this.classPath = classPath;
        this.className = classPath.substring(classPath.lastIndexOf(Constants.DOT));
        this.packageName = classPath.substring(0, classPath.lastIndexOf(Constants.DOT));
        this.lineNum = lineNum;
    }

    public String getExcludedBasePackage() {
        if (Objects.nonNull(excludedBasePackageCache)) {
            return excludedBasePackageCache;
        }
        String basePackage = CallRelationConfig.getInstance().getBasePackage();
        return excludedBasePackageCache = Optional.ofNullable(packageName)
                .map(it -> it.substring(basePackage.length()))
                .filter(it -> it.startsWith(Constants.DOT))
                .map(packageStr -> {
                    String remainPackage = packageStr.substring(Constants.DOT.length());
                    return remainPackage.contains(Constants.DOT) ?
                            remainPackage.substring(0, remainPackage.indexOf(Constants.DOT)) : remainPackage;
                }).orElse("");
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getClassPath() {
        return classPath;
    }

    public int getLineNum() {
        return lineNum;
    }
}