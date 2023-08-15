package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.ObjectCopier;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by Dechert on 2018-11-14.
 * Company: zhishusz
 */
@Service
public class ApprovalDetailEchoUtil<T, F extends BaseForm> {
    private int addOrUpdate = -1;//1：初始维护，2：变更维护

    @Autowired
    private Sm_ApprovalProcess_AFDao approvalProcessAFDao;
    @Autowired
    private Gson gson;
    @Autowired
    private IsNeedBackupUtil isNeedBackupUtil;

    public void getEcho(T poObj, F model, Properties properties, String originPoKey, String newPoKey, OnSetChangeMap<T, F> onSetChangeMap) {
        if (model.getReqSource() != null && model.getReqSource().equals("详情")) {
            properties.put(originPoKey, poObj);
        } else {
            if (model.getAfId() != null) {//审批详情
                getApprovalMessage(poObj, model, properties, originPoKey, newPoKey, onSetChangeMap);
            } else {//编辑
                getEditApprovalMessage(poObj, model, properties, originPoKey, newPoKey, onSetChangeMap);
            }
        }
    }

    private void getApprovalMessage(T poObj, BaseForm model, Properties properties, String originPoKey, String newPoKey, OnSetChangeMap<T, F> onSetChangeMap) {
        Class<T> poClass = (Class<T>) poObj.getClass();
        Class<F> formClass = (Class<F>) model.getClass();
        if (poObj == null) {
//            System.out.println("poObj is null!");
            return;
        }
        Long afId = model.getAfId();
        if (afId == null) {
//            System.out.println("afId is null!");
            return;
        }
        Long tableId = null;
        String busiCode = null;
        String busiState = null;
        String approvalState = null;
        try {
            tableId = (Long) poObj.getClass().getMethod("getTableId").invoke(poObj);
//            System.out.println("tableId is " + tableId);
            busiState = (String) poObj.getClass().getMethod("getBusiState").invoke(poObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tableId == null) {
//            System.out.println("tableId is null");
            return;
        }
        if (busiState == null) {
//            System.out.println("busiState is null");
            return;
        }
        Sm_ApprovalProcess_AF afDetail = approvalProcessAFDao.findById(afId);
        busiCode = afDetail.getBusiCode();
//        setIsNeedBackup(properties, afDetail);
        isNeedBackupUtil.setIsNeedBackup(properties, afDetail);
        if (busiCode == null) {
//            System.out.println("busiCode is null!");
        } else {
            //todo 看看这个busiCode有没有问题
            System.out.println("busiCode is " + busiCode);
        }
        if (afDetail != null) {
            int busiCodeAddOrUpdate = getBusiCodeAddOrUpdate(busiCode);
            if (busiCodeAddOrUpdate == 1) {//如果是初始维护审批详情
                if (busiState.equals(S_BusiState.NoRecord)) {//如果是未备案，则是新增待提交的审批流，使用Database数据
                    properties.put(originPoKey, poObj);//先放现在的po类
                    setMapWithNewForm(poObj, properties, newPoKey, onSetChangeMap, formClass);
                } else if (busiState.equals(S_BusiState.HaveRecord)) {//如果是已备案，则是看的历史数据，在OSS 中
                    /*F newMapForm = gson.fromJson(afDetail.getExpectObjJson(), formClass);//新的form，里面有变更后的值
                    T copy = ObjectCopier.wholeCopy(poObj);
                    properties.put(originPoKey, copy);
                    onSetChangeMap.onSetOrgMap(copy, newMapForm);//将form里的原值，赋给这个拷贝后的po
                    HashMap<String, Object> newValueMap = createNewValueMap(properties, newPoKey);
                    setMapWithGsonForm(poObj, formClass, afDetail, newValueMap);//OSS中的值作为newMap中的值*/
                	properties.put(originPoKey, poObj);//先放现在的po类
                    setMapWithNewForm(poObj, properties, newPoKey, onSetChangeMap, formClass);
                }
            } else if (busiCodeAddOrUpdate == 2) {//如果是变更审批详情
                if (afDetail.getBusiState().equals(S_ApprovalState.WaitSubmit) || afDetail.getBusiState().equals(S_ApprovalState.Examining)) {//如果是待提交审核中，则是取当前database中的数据和申请单进行对比
                    properties.put(originPoKey, poObj);
                    HashMap<String, Object> newValueMap = createNewValueMap(properties, newPoKey);
                    setMapWithGsonForm(poObj, formClass, afDetail, newValueMap);//将json中的值作为new map
                } else {//剩下的是已完结和已删除，则是两个申请单进行对比
                    HashMap<String, Object> newValueMap = createNewValueMap(properties, newPoKey);
                    setMapWithGsonForm(poObj, formClass, afDetail, newValueMap);//将json中的值作为new map
                    Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
                    sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
                    sm_ApprovalProcess_AFForm.setSourceId(tableId);
                    sm_ApprovalProcess_AFForm.setOrderBy("createTimeStamp asc");
                    List<Sm_ApprovalProcess_AF> afList = approvalProcessAFDao.findByPage(approvalProcessAFDao.getQuery(approvalProcessAFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
                    int indexOfNow = -1;
                    for (int i = 0; i < afList.size(); i++) {
                        Sm_ApprovalProcess_AF item = afList.get(i);
                        if (item.getTableId().equals(afId)) {
                            indexOfNow = i;
                            break;
                        }
                    }
                    int oldHistory = indexOfNow - 1;
                    if (oldHistory >= 0) {
                        Sm_ApprovalProcess_AF oldAf = afList.get(oldHistory);
                        String expectObjJson = oldAf.getExpectObjJson();
                        T copy = ObjectCopier.copy(poObj);
                        onSetChangeMap.onSetOrgMap(copy, gson.fromJson(expectObjJson, formClass));
                        properties.put(originPoKey, copy);
                    }
                }
            }
        } else {

        }
    }

    /**
     * 复制org的form到new中
     *
     * @param poObj
     * @param properties
     * @param newPoKey
     * @param onSetChangeMap
     * @param formClass
     */
    private void setMapWithNewForm(T poObj, Properties properties, String newPoKey, OnSetChangeMap<T, F> onSetChangeMap, Class<F> formClass) {
        HashMap<String, Object> newValueMap = createNewValueMap(properties, newPoKey);
        try {
            F newInstanceForm = formClass.newInstance();
            onSetChangeMap.onSetNewMap(newInstanceForm);//复制现在的po类进map，让两个Map里的变更字段保持一致
            ArrayList<String> changeArgs = getChangeArgs(poObj);
            for (String changeArg : changeArgs) {
                if (changeArg.equals("generalAttachmentList")) {//如果是附件字段则单独考虑
//                    setAttachmentList(newValueMap, newInstanceForm);
                } else {
                    try {
                        Field declaredField = newInstanceForm.getClass().getDeclaredField(changeArg);
                        declaredField.setAccessible(true);
                        newValueMap.put(changeArg, declaredField.get(newInstanceForm));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将json中的数据放到form中
     *
     * @param poObj
     * @param formClass
     * @param afDetail
     * @param newValueMap
     */
    private void setMapWithGsonForm(T poObj, Class<F> formClass, Sm_ApprovalProcess_AF afDetail, HashMap<String, Object> newValueMap) {
        F newMapForm = gson.fromJson(afDetail.getExpectObjJson(), formClass);//新的form，里面有变更后的值
        ArrayList<String> changeArgs = getChangeArgs(poObj);
        for (String changeArg : changeArgs) {
            if (changeArg.equals("generalAttachmentList")) {//如果是附件字段则单独考虑
//                setAttachmentList(newValueMap, newMapForm);
            } else {
                try {
                    Field declaredFieldWithValue = newMapForm.getClass().getDeclaredField(changeArg);
                    declaredFieldWithValue.setAccessible(true);
                    Object o = declaredFieldWithValue.get(newMapForm);
                    newValueMap.put(changeArg, o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAttachmentList(HashMap<String, Object> newValueMap, F newMapForm) {
        try {
            Sm_AttachmentForm[] generalAttachmentList = (Sm_AttachmentForm[]) newMapForm.getClass().getMethod("getGeneralAttachmentList").invoke(newMapForm);
            if (generalAttachmentList != null) {
                for (Sm_AttachmentForm sm_attachmentForm : generalAttachmentList) {
//                    System.out.println("form info is " + sm_attachmentForm.toString());
                }
                newValueMap.put("generalAttachmentList", generalAttachmentList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> createNewValueMap(Properties properties, String newPoKey) {
        HashMap<String, Object> newValueMap = new HashMap<>();
        properties.put(newPoKey, newValueMap);
        return newValueMap;
    }

//    private void setIsNeedBackup(Properties properties, Sm_ApprovalProcess_AF afDetail) {
//        if (afDetail != null) {
//            Integer isNeedBackup = afDetail.getIsNeedBackup();
//            if (S_IsNeedBackup.Yes.equals(isNeedBackup)) {
//                properties.put("isNeedBackup", true);
//            } else {
//                properties.put("isNeedBackup", false);
//            }
//        }
//    }

    /**
     * 编辑时获取申请单的消息
     *
     * @param poObj
     * @param model
     * @param properties
     * @param originPoKey
     * @param newPoKey
     * @param onSetChangeMap
     */
    private void getEditApprovalMessage(T poObj, F model, Properties properties, String originPoKey, String newPoKey, OnSetChangeMap<T, F> onSetChangeMap) {
        try {
            String busiCodeFromDetail = (String) properties.get("busiCode");
            System.out.println("busiCodeFromDetail is " + busiCodeFromDetail);
            Class<F> formClass = (Class<F>) model.getClass();
            Long tableId = (Long) poObj.getClass().getMethod("getTableId").invoke(poObj);
            Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
            sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
            sm_ApprovalProcess_AFForm.setBusiState("待提交");
            sm_ApprovalProcess_AFForm.setSourceId(tableId);
            sm_ApprovalProcess_AFForm.setBusiCode(busiCodeFromDetail);
            Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessAFDao.findOneByQuery_T(approvalProcessAFDao.getQuery(approvalProcessAFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
            if (sm_ApprovalProcess_AF == null) {//没有待提交的申请单，两个都看po
                properties.put(originPoKey, poObj);
                setMapWithNewForm(poObj, properties, newPoKey, onSetChangeMap, formClass);
            } else {//有待提交的申请单
                String busiCode = sm_ApprovalProcess_AF.getBusiCode();
                int busiCodeAddOrUpdate = getBusiCodeAddOrUpdate(busiCode);
                if (busiCodeAddOrUpdate == 1) {//新增的业务都看po
                    properties.put(originPoKey, poObj);
                    setMapWithNewForm(poObj, properties, newPoKey, onSetChangeMap, formClass);
                } else {//变更的业务看po和OSS
                    properties.put(originPoKey, poObj);
                    //OSS是新的
                    setMapWithGsonForm(poObj, formClass, sm_ApprovalProcess_AF, createNewValueMap(properties, newPoKey));
                }
                isNeedBackupUtil.setIsNeedBackup(properties, sm_ApprovalProcess_AF);
//                setIsNeedBackup(properties, sm_ApprovalProcess_AF);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private T setMatchValue(T poObj, Class<T> poClass, Sm_ApprovalProcess_AF afDetail) {
        T copy = ObjectCopier.copy(poObj);
        String expectObjJson = afDetail.getExpectObjJson();
        try {
            T historyObj = gson.fromJson(expectObjJson, poClass);
            ArrayList<String> changeArgs = getChangeArgs(poObj);
            copyChangeValue(copy, historyObj, changeArgs);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return copy;
    }

    private void copyChangeValueAsMap(T orgObj, F newFormInstance, ArrayList<String> changeArgs, Properties properties, String newMapKey) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (changeArgs != null) {
                for (String changeArg : changeArgs) {
                    Field declaredField = newFormInstance.getClass().getDeclaredField(changeArg);
                    declaredField.setAccessible(true);
                    Object fieldValueHistory = declaredField.get(orgObj);
                    map.put(changeArg, fieldValueHistory);
                }
            } else {
//                System.out.println("getChangeArgs is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        properties.put(newMapKey, map);
    }

    private void copyChangeValue(T copy, T historyObj, ArrayList<String> changeArgs) {
        try {
            if (changeArgs != null) {
                for (String changeArg : changeArgs) {
                    Field declaredField = historyObj.getClass().getDeclaredField(changeArg);
                    declaredField.setAccessible(true);
                    Object fieldValueHistory = declaredField.get(historyObj);
                    Field copyField = copy.getClass().getDeclaredField(changeArg);
                    copyField.setAccessible(true);
                    copyField.set(copy, fieldValueHistory);
                }
            } else {
//                System.out.println("getChangeArgs is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private T newObjCopyNeed(T poObj) {
        ArrayList<String> changeArgs = getChangeArgs(poObj);
        try {
            T newInstance = (T) poObj.getClass().newInstance();
            copyChangeValue(newInstance, poObj, changeArgs);
            return newInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getChangeArgs(T poObj) {
        try {
            if (poObj instanceof IApprovable) {
                IApprovable approvable = (IApprovable) poObj;
//                ArrayList<String> getPeddingApprovalkey = (ArrayList<String>) poObj.getClass().getMethod("getPeddingApprovalkey").invoke(poObj);
                return (ArrayList<String>) approvable.getPeddingApprovalkey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public int getBusiCodeAddOrUpdate(String busiCode) {
        if (StringUtils.isEmpty(busiCode)) {
            return -1;
        }
        switch (busiCode) {
            case S_BusiCode.busiCode_020101:
            case S_BusiCode.busiCode_020103:
            case S_BusiCode.busiCode_020106:
            case S_BusiCode.busiCode_020108:
            case S_BusiCode.busiCode_03010101:
            case S_BusiCode.busiCode_03010201:
            case S_BusiCode.busiCode_03010301:
            case S_BusiCode.busiCode_03030101:
            case S_BusiCode.busiCode_03030102:
            case S_BusiCode.busiCode_03030201:
            case S_BusiCode.busiCode_06010101:
            case S_BusiCode.busiCode_06010102:
            case S_BusiCode.busiCode_06010103:
            case S_BusiCode.busiCode_06110201:
            case S_BusiCode.busiCode_06110301:
            case S_BusiCode.busiCode_06110304:
            case S_BusiCode.busiCode_06120201:
            case S_BusiCode.busiCode_06120202:
            case S_BusiCode.busiCode_06120301:
            case S_BusiCode.busiCode_06120302:
            case S_BusiCode.busiCode_06120303:
            case S_BusiCode.busiCode_06120401:
            case S_BusiCode.busiCode_06120403:
                return 1;
            case S_BusiCode.busiCode_020102:
            case S_BusiCode.busiCode_020105:
            case S_BusiCode.busiCode_020107:
            case S_BusiCode.busiCode_020109:
            case S_BusiCode.busiCode_03010102:
            case S_BusiCode.busiCode_03010202:
            case S_BusiCode.busiCode_03010302:
                return 2;
            default:
                return 1;
        }
    }

    public interface OnSetChangeMap<T, F extends BaseForm> {
        void onSetOrgMap(T copyPo, F jsonFromOssForm);

        void onSetNewMap(F newValueForm);
    }
}
