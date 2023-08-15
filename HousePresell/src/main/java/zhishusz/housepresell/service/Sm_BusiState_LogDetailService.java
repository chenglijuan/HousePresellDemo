package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_BusiState_LogForm;
import zhishusz.housepresell.database.dao.Sm_BusiState_LogDao;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.objectdiffer.differ.ProjectObjectDiffer;
import zhishusz.objectdiffer.model.ObjectCompareModel;

/*
 * Service详情：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusiState_LogDetailService {
    @Autowired
    private Sm_BusiState_LogDao sm_BusiState_LogDao;
    @Autowired
    private Gson gson;

    public Properties execute(Sm_BusiState_LogForm model) {
        Properties properties = new MyProperties();

        Long sm_BusiState_LogId = model.getTableId();
        Sm_BusiState_Log sm_BusiState_Log = sm_BusiState_LogDao.findById(sm_BusiState_LogId);
        if (sm_BusiState_Log == null || S_TheState.Deleted.equals(sm_BusiState_Log.getTheState())) {
            return MyBackInfo.fail(properties, "'Sm_BusiState_Log(Id:" + sm_BusiState_LogId + ")'不存在");
        }
        String sourceType = sm_BusiState_Log.getSourceType();

        ArrayList<ObjectCompareModel> differ = new ArrayList<>();
        ArrayList<ObjectCompareModel> sortList = new ArrayList<>();
        try {
            Class<?> poClass = null;
            if (model.getPackagePath() == null || model.getPackagePath().length() == 0) {
                poClass = Class.forName("zhishusz.housepresell.database.po." + sourceType);
            } else {
                poClass = Class.forName(model.getPackagePath() + sourceType);
            }

            String orgObjJson = sm_BusiState_Log.getOrgObjJson();
            String newObjJson = sm_BusiState_Log.getNewObjJson();
            Object oldObj = gson.fromJson(orgObjJson, poClass);
            Object newObj = gson.fromJson(newObjJson, poClass);
            if(sourceType.equals("Sm_Permission_RangeAuthorizationTemplate")){//如果是范围授权，则单独使用
                properties.put("rangeOld", oldObj);
                properties.put("rangeNew", newObj);
            }
            Method getNeedFieldList = oldObj.getClass().getMethod("getNeedFieldList");
            List<String> invokeList = (List) getNeedFieldList.invoke(oldObj);
            ArrayList<String> needFieldList = new ArrayList<>();
            if (invokeList != null) {
                for (String fieldString : invokeList) {
                    if (fieldString.equals("orgMemberList")) {
                        setOrgMemberList(properties, oldObj, newObj);
                        needFieldList.add(fieldString);
                    } else if (fieldString.equals("forcastDtlList")) {
                        setForcastDtlList(properties, oldObj, newObj);
                    } else {
                        needFieldList.add(fieldString);
                    }
                }
            }
            ProjectObjectDiffer projectObjectDiffer = new ProjectObjectDiffer();
//			IProjectDiffer objectObjectDiffer = (IProjectDiffer) Naming.lookup(projectDifferProperty.getProjectDifferPath());
//			ProjectObjectDiffer objectObjectDiffer = new ProjectObjectDiffer(oldObj, newObj, "${null}$");
            projectObjectDiffer.addNeedFieldList(needFieldList);
            projectObjectDiffer.setTableUniqueString("tableId");
            projectObjectDiffer.setLog(false);
            differ = projectObjectDiffer.projectDiffer(oldObj, newObj);
            for (String needList : invokeList) {
                for (ObjectCompareModel objectCompareModel : differ) {
                    if (needList.equals(objectCompareModel.getFieldName())) {
                        sortList.add(objectCompareModel);
                        break;
                    }
                }
            }
            if(sourceType.equals("Sm_Permission_RangeAuthorizationTemplate")){//范围授权，删除第0个
//                sortList.set(0, null);
                sortList.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        Sm_BusiState_Log copy = ObjectCopier.copy(sm_BusiState_Log);
        properties.put("sm_BusiState_Log", copy);
        properties.put("differ", sortList);

        return properties;
    }

    private void setForcastDtlList(Properties properties, Object oldObj, Object newObj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Properties> forcastDtlListOld = (List<Properties>) oldObj.getClass().getMethod("getForcastDtlList").invoke(oldObj);
        List<Properties> forcastDtlListNew = (List<Properties>) newObj.getClass().getMethod("getForcastDtlList").invoke(newObj);
        properties.put("forcastDtlListOld", forcastDtlListOld);
        properties.put("forcastDtlListNew", forcastDtlListNew);
    }

    private void setOrgMemberList(Properties properties, Object oldObj, Object newObj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Emmp_OrgMember> getOrgMemberListOld = (List<Emmp_OrgMember>) oldObj.getClass().getMethod("getOrgMemberList").invoke(oldObj);
        List<Emmp_OrgMember> getOrgMemberListNew = (List<Emmp_OrgMember>) newObj.getClass().getMethod("getOrgMemberList").invoke(newObj);
        properties.put("orgMemberListOld", getOrgMemberListOld);
        properties.put("orgMemberListNew", getOrgMemberListNew);
    }
}
