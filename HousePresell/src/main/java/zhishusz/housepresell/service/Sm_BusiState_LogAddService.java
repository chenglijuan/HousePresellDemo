package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_BusiState_LogForm;
import zhishusz.housepresell.database.dao.Sm_BusiState_LogDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_BusiState_Log;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Properties;

import javax.transaction.Transactional;

import zhishusz.objectdiffer.differ.ProjectObjectDiffer;
import zhishusz.objectdiffer.model.ObjectCompareModel;

/*
 * Service添加操作：日志-业务状态
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_BusiState_LogAddService {
    @Autowired
    private Sm_BusiState_LogDao sm_BusiState_LogDao;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private OssServerUtil ossServerUtil;
    @Autowired
    private Gson gson;

    public Properties addLog(BaseForm baseForm, Long tableId, Object oldObj, Object newObj) {
        return addLog(baseForm, tableId, oldObj, newObj, "tableId");
    }

    public Properties addLog(BaseForm baseForm, Long tableId, Object oldObj, Object newObj, String uniqueString) {
        try {
            ProjectObjectDiffer objectDiffer = new ProjectObjectDiffer();
            objectDiffer.setTableUniqueString(uniqueString);
            objectDiffer.addTotalFilterString("serialVersionUID");
            objectDiffer.addTotalFilterString("userUpdate");
            objectDiffer.addTotalFilterString("userStart");
            objectDiffer.addTotalFilterString("userRecord");
            objectDiffer.addTotalFilterString("lastUpdateTimeStamp");
            objectDiffer.addTotalFilterString("version");
            objectDiffer.addTotalFilterString("approvalState");
            objectDiffer.setLog(false);
            ArrayList<ObjectCompareModel> differ = objectDiffer.projectDiffer(oldObj, newObj);

//            String differListJson = gson.toJson(differ);
//            System.out.println("differListJson is "+differListJson);

//            Field serialVersionUID = newObj.getClass().getDeclaredField("serialVersionUID");
//            serialVersionUID.setAccessible(true);
//            serialVersionUID.set(newObj,-1);

//            JsonUtil jsonUtil = new JsonUtil();
//            Class<?> objClass = oldObj.getClass();
//            String oldObjJsonHandle = jsonUtil.objToJson(oldObj, 3);
//            String newObjJsonHandle = jsonUtil.objToJson(newObj, 3);
//            Object oldObjHandle = gson.fromJson(oldObjJsonHandle, objClass);
//            Object newObjHandle = gson.fromJson(newObjJsonHandle, objClass);
//		Gson gson = new GsonBuilder()
//				.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create() ;

//		}).registerTypeAdapter(HibernateProxy.class, new TypeAdapter<HibernateProxy>() {
//			@Override
//			public void write(JsonWriter jsonWriter, HibernateProxy object) throws IOException {
//
//			}
//
//			@Override
//			public HibernateProxy read(JsonReader jsonReader) throws IOException {
//				return null;
//			}
//		}).create();
//            ObjectDiffer objectDiffer = new ObjectDiffer();
//            ProjectObjectDiffer projectObjectDiffer = new ProjectObjectDiffer();
//            projectObjectDiffer.addFilterString("userUpdate");
//            projectObjectDiffer.addFilterString("userStart");
//            projectObjectDiffer.addFilterString("userRecord");
//            projectObjectDiffer.addFilterString("lastUpdateTimeStamp");
//            projectObjectDiffer.addFilterString("version");
//            ArrayList<ObjectCompareModel> objectCompareModels = projectObjectDiffer.projectDiffer(oldObj, newObj);

//            IObjectDiffer objectDiffer = (IObjectDiffer) Naming.lookup(projectDifferProperty.getObjectDifferPath());
//            if(objectDiffer==null){
//                System.out.println("objectDiffer is null");
//            }else{
//                System.out.println("objectDiffer not null");
//            }

//            ArrayList<ObjectCompareModel> differ = objectDiffer.differ(oldObj,newObj);

//            for (ObjectCompareModel objectCompareModel : differ) {
//                System.out.println("objectCompareModel is " + objectCompareModel.toString());
//            }

//            if(objectDiffer.calDiffentFieldAmount(differ)==0){
//            if(projectObjectDiffer.isTwoObjSame(objectCompareModels)){
            if (objectDiffer.isTwoObjSame(differ)) {
                System.out.println("没有改变，不保存log");
                return null;
            } else {
                System.out.println("发生改变，保存log");
                for (ObjectCompareModel objectCompareModel : differ) {
                    if (!objectCompareModel.isSame()) {
                        String json = gson.toJson(objectCompareModel);
                        System.out.println("不同的字段为：" + json);
                    }
                }
            }
            Sm_BusiState_LogForm sm_busiState_logForm = new Sm_BusiState_LogForm();
            sm_busiState_logForm.setTheState(S_TheState.Normal);
            sm_busiState_logForm.setUserOperate(baseForm.getUser());
            sm_busiState_logForm.setRemoteAddress(baseForm.getIpAddress());
            sm_busiState_logForm.setOperateTimeStamp(System.currentTimeMillis());
            sm_busiState_logForm.setSourceId(tableId);
            sm_busiState_logForm.setSourceType(newObj.getClass().getSimpleName());
//            System.out.println("class name is " + newObj.getClass().getName());
//            String oldObjJson = jsonUtil.objToJson(oldObj, 3);
//            String newObjJson = jsonUtil.objToJson(newObj, 3);
//            System.out.println("oldObjJson is " + oldObjJson);
//            System.out.println("newObjJson is " + newObjJson);

//            ReceiveMessage uploadOld = ossServerUtil.stringUpload(oldObjJson);
            String oldObjJson = gson.toJson(oldObj);
            ReceiveMessage uploadOld = ossServerUtil.stringUpload(oldObjJson);
//            System.out.println(uploadOld);
//            ReceiveMessage uploadNew = ossServerUtil.stringUpload(newObjJson);
            String newObjJson = gson.toJson(newObj);
            ReceiveMessage uploadNew = ossServerUtil.stringUpload(newObjJson);
            sm_busiState_logForm.setOrgObjJsonFilePath(uploadOld.getData().get(0).getUrl());
            sm_busiState_logForm.setNewObjJsonFilePath(uploadNew.getData().get(0).getUrl());
            return execute(sm_busiState_logForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Properties execute(Sm_BusiState_LogForm model) {
        Properties properties = new MyProperties();

        Integer theState = S_TheState.Normal;
        Long userOperateId = model.getUserOperateId();
        String remoteAddress = model.getRemoteAddress();
        Long operateTimeStamp = model.getOperateTimeStamp();
        Long sourceId = model.getSourceId();
        String sourceType = model.getSourceType();
        String orgObjJsonFilePath = model.getOrgObjJsonFilePath();
        String newObjJsonFilePath = model.getNewObjJsonFilePath();

//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "'theState'不能为空");
//		}
//		if(userOperateId == null || userOperateId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userOperate'不能为空");
//		}
//		if(remoteAddress == null || remoteAddress.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'remoteAddress'不能为空");
//		}
//		if(operateTimeStamp == null || operateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'operateTimeStamp'不能为空");
//		}
//		if(sourceId == null || sourceId < 1)
//		{
//			return MyBackInfo.fail(properties, "'sourceId'不能为空");
//		}
//		if(sourceType == null || sourceType.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'sourceType'不能为空");
//		}
//		if(orgObjJsonFilePath == null || orgObjJsonFilePath.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'orgObjJsonFilePath'不能为空");
//		}
//		if(newObjJsonFilePath == null || newObjJsonFilePath.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'newObjJsonFilePath'不能为空");
//		}
//
//		Sm_User userOperate = (Sm_User)sm_UserDao.findById(userOperateId);
//		if(userOperate == null)
//		{
//			return MyBackInfo.fail(properties, "'userOperate'不能为空");
//		}

        Sm_BusiState_Log sm_BusiState_Log = new Sm_BusiState_Log();
        sm_BusiState_Log.setTheState(theState);
        sm_BusiState_Log.setUserOperate(model.getUserOperate());
        sm_BusiState_Log.setRemoteAddress(remoteAddress);
        sm_BusiState_Log.setOperateTimeStamp(operateTimeStamp);
        sm_BusiState_Log.setSourceId(sourceId);
        sm_BusiState_Log.setSourceType(sourceType);
        sm_BusiState_Log.setOrgObjJsonFilePath(orgObjJsonFilePath);
        sm_BusiState_Log.setNewObjJsonFilePath(newObjJsonFilePath);
        sm_BusiState_LogDao.save(sm_BusiState_Log);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
