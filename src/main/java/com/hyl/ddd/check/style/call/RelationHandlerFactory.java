package com.hyl.ddd.check.style.call;

/**
 * @author huayuanlin on 2022/10/22
 */
public class RelationHandlerFactory {


    public static CallRelationHandler buildHandler() {
        AllowDependClassPackageHandler dependClassPackageHandler = new AllowDependClassPackageHandler(null);
        SamePackageClassHandler samePackageClassHandler = new SamePackageClassHandler(dependClassPackageHandler);
        CommonPackageClassHandler commonPackageClassHandler = new CommonPackageClassHandler(samePackageClassHandler);
        return new IgnorePackageClassHandler(commonPackageClassHandler);
    }

}