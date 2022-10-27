package com.hyl.ddd.check.style.call;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Objects;

/**
 * @author huayuanlin on 2022/10/22
 * this will be executed as a single thread.
 */
public class ClassCallRelationCheck extends AbstractCheck {

    private CallRelationHandler callRelationHandler;

    private CheckingClassHolder curCheckingClass;

    /**
     * ex: com.xxx.yyy
     */
    private String allowCallBasePackage = "";

    /**
     * relationships that allow calls,it can be used in combination with allBasePackage.
     * ex: aaa->bbb,bbb->ccc
     * aaa.*->bbb.*->ccc.ddd.*
     */
    private String allowCallPackages;

    /**
     * ignore check package
     * ex: test.*    actual= com.xxx.yyy.test.* =allowCallBasePackage.+ignorePackagePatterns
     */
    private String ignorePackagePatterns;

    /**
     * allow packages of the same level to call each other?
     */
    private boolean allowSamePackage = true;

    /**
     * ignore the common package for this dependency.
     * ex: test.*    actual= com.xxx.yyy.test.* =allowCallBasePackage.+ ignorePackagePatterns
     */
    private String commonPackagePatterns;

    @Override
    public void init() {
        this.callRelationHandler = RelationHandlerFactory.buildHandler();
        CallRelationConfig.instance = new CallRelationConfig(allowSamePackage,
                allowCallBasePackage, allowCallPackages, ignorePackagePatterns, commonPackagePatterns);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void beginTree(DetailAST rootAst) {
        curCheckingClass = null;
    }

    @Override
    public void finishTree(DetailAST rootAst) {
        curCheckingClass = null;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.IMPORT, TokenTypes.CLASS_DEF, TokenTypes.STATIC_IMPORT};
    }


    @Override
    public void visitToken(DetailAST ast) {
        if (ast.getType() == TokenTypes.CLASS_DEF
                && Objects.nonNull(curCheckingClass)) {
            // class name
            String className = getFullImportIdent(ast, TokenTypes.IDENT);
            curCheckingClass.setCurCheckClass(className, ast.getLineNo());
            // check all import classes collected by the current class
            doCheckCurClass();
        } else if (ast.getType() == TokenTypes.IMPORT ||
                ast.getType() == TokenTypes.STATIC_IMPORT) {
            if (Objects.isNull(curCheckingClass)) {
                // package path
                String curPackage = getFullImportIdent(ast.getPreviousSibling(), TokenTypes.DOT);
                curCheckingClass = new CheckingClassHolder(curPackage);
            }
            // import path
            String importPath;
            if (ast.getType() == TokenTypes.STATIC_IMPORT) {
                importPath = FullIdent.createFullIdent(ast.getFirstChild().getNextSibling()).getText();
            } else {
                importPath = FullIdent.createFullIdentBelow(ast).getText();
            }
            curCheckingClass.addCheckImportClass(importPath, ast.getLineNo());
        }
    }


    private String getFullImportIdent(DetailAST token, int type) {
        if (token == null) {
            return "";
        } else {
            return FullIdent.createFullIdent(token.findFirstToken(type)).getText();
        }
    }


    private void doCheckCurClass() {
        CheckClass checkingClass = curCheckingClass.getCheckingClass();
        curCheckingClass.getImportClass().stream()
                .filter(importClass -> !callRelationHandler.allowCall(checkingClass, importClass))
                .forEach(checkFailed -> {
                    log(checkFailed.getLineNum(), "not support class call relation, please check it.");
                });
    }

    public String getAllowCallBasePackage() {
        return allowCallBasePackage;
    }

    public String getAllowCallPackages() {
        return allowCallPackages;
    }

    public String getIgnorePackagePatterns() {
        return ignorePackagePatterns;
    }

    public boolean isAllowSamePackage() {
        return allowSamePackage;
    }

    public String getCommonPackagePatterns() {
        return commonPackagePatterns;
    }

    public void setAllowCallBasePackage(String allowCallBasePackage) {
        this.allowCallBasePackage = allowCallBasePackage;
    }

    public void setAllowCallPackages(String allowCallPackages) {
        this.allowCallPackages = allowCallPackages;
    }

    public void setIgnorePackagePatterns(String ignorePackagePatterns) {
        this.ignorePackagePatterns = ignorePackagePatterns;
    }

    public void setAllowSamePackage(boolean allowSamePackage) {
        this.allowSamePackage = allowSamePackage;
    }

    public void setCommonPackagePatterns(String commonPackagePatterns) {
        this.commonPackagePatterns = commonPackagePatterns;
    }
}