package com.hyl.ddd.check.style.call;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author huayuanlin on 2022/10/22
 */
public class CheckingClassHolder {

    private final String curPackage;

    private CheckClass checkingClass;

    private final List<CheckClass> importClass = Lists.newArrayList();

    public CheckingClassHolder(String curPackage) {
        this.curPackage = curPackage;
    }

    public void setCurCheckClass(String curClassName, int line) {
        this.checkingClass = new CheckClass(this.curPackage, curClassName, line);
    }

    public void addCheckImportClass(String classPath, int line) {
        importClass.add(new CheckClass(classPath, line));
    }


    public CheckClass getCheckingClass() {
        return checkingClass;
    }

    public List<CheckClass> getImportClass() {
        return importClass;
    }
}