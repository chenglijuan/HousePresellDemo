package zhishusz.housepresell.util;


import java.util.HashSet;

import zhishusz.objectdiffer.copier.ProjectObjectCopyier;

public class ObjectCopier {
    public synchronized static<T> T copy(T srcObj){
        ProjectObjectCopyier<T> projectObjectCopyier = new ProjectObjectCopyier<>();
        HashSet<String> filterTotalSet = new HashSet<>();
        filterTotalSet.add("serialVersionUID");
        filterTotalSet.add("userUpdate");
        filterTotalSet.add("lastUpdateTimeStamp");
        filterTotalSet.add("_filter_signature");
        filterTotalSet.add("_methods_");
        filterTotalSet.add("version");
        filterTotalSet.add("approvalState");
//        filterTotalSet.add("userStart");
//        filterTotalSet.add("userRecord");
        filterTotalSet.add("qualificationInformationList");
        projectObjectCopyier.setFilterTotalSet(filterTotalSet);
        projectObjectCopyier.setHibernateCopy(true);
        projectObjectCopyier.setLog(false);
        return projectObjectCopyier.hibernateCopy(srcObj);


//        BeanCopier beanCopier = BeanCopier.create(srcObj.getClass(), srcObj.getClass(), false);
//        try {
//            T newObj = (T) srcObj.getClass().newInstance();
//            beanCopier.copy(srcObj, newObj, null);
//            return newObj;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    public synchronized static<T> T wholeCopy(T srcObj){
        ProjectObjectCopyier<T> projectObjectCopyier = new ProjectObjectCopyier<>();
        HashSet<String> filterTotalSet = new HashSet<>();
        filterTotalSet.add("serialVersionUID");
//        filterTotalSet.add("userUpdate");
//        filterTotalSet.add("lastUpdateTimeStamp");
        filterTotalSet.add("_filter_signature");
        filterTotalSet.add("_methods_");
//        filterTotalSet.add("version");
//        filterTotalSet.add("approvalState");
//        filterTotalSet.add("userStart");
//        filterTotalSet.add("userRecord");
        filterTotalSet.add("qualificationInformationList");
        projectObjectCopyier.setFilterTotalSet(filterTotalSet);
        projectObjectCopyier.setHibernateCopy(true);
        projectObjectCopyier.setLog(false);
        return projectObjectCopyier.hibernateCopy(srcObj);


//        BeanCopier beanCopier = BeanCopier.create(srcObj.getClass(), srcObj.getClass(), false);
//        try {
//            T newObj = (T) srcObj.getClass().newInstance();
//            beanCopier.copy(srcObj, newObj, null);
//            return newObj;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
