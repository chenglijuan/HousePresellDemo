package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg_Copy;
import zhishusz.housepresell.database.po.state.S_AcceptFileType;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyLong;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.ApprovalDetailEchoUtil;
import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.OSSClientProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：附件配置
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_AttachmentCfgRebuild extends RebuilderBase<Sm_AttachmentCfg>
{
	@Autowired
	private OSSClientProperty oss;

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Gson gson;
	@Autowired
	private ApprovalDetailEchoUtil approvalDetailEchoUtil;

	@Override
	public Properties getSimpleInfo(Sm_AttachmentCfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("eCode", object.geteCode());//附件类型编码
		properties.put("tableId", object.getTableId());//附件类型主键
		properties.put("busiType", object.getBusiType());
		properties.put("theName", object.getTheName());//附件档案名称
		properties.put("acceptFileType", object.getAcceptFileType());//接收文件类型
		properties.put("acceptFileCount", object.getAcceptFileCount());//接收文件数量
		properties.put("maxFileSize", object.getMaxFileSize());//最大
		properties.put("minFileSize", object.getMinFileSize());//最小
		properties.put("remark", object.getRemark());//备注
		properties.put("isImage", object.getIsImage());//是否是图片  0-否 1-是
		properties.put("isNeeded", object.getIsNeeded());//是否必须 0-否 1-是
		properties.put("listType", object.getListType());//展示形式

		//设置封装参数
//		DataInfo data = new DataInfo();
//		data.setExtra(object.geteCode());
//		data.setAppid("-");
//		data.setAppsecret(" ");
//		properties.put("data", data);

		Sm_AttachmentCfgDataInfo dataInfo = new Sm_AttachmentCfgDataInfo();
		dataInfo.setExtra(object.geteCode());
		dataInfo.setAppid(oss.getAppid());
		dataInfo.setAppsecret(oss.getAppsecret());
		dataInfo.setRemote(oss.getRemote());
		dataInfo.setProject(oss.getProject());

		//http://192.168.1.8:19000/oss/material/bananaUpload/uploadMaterial
		String remote = oss.getRemote();
		String subremote = remote.substring(0, remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";
		dataInfo.setUpLoadUrl(subremote);
		properties.put("data", dataInfo);
		return properties;
	}

	@Override
	public Properties getDetail(Sm_AttachmentCfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("busiType", object.getBusiType());
		properties.put("theName", object.getTheName());
		properties.put("acceptFileType", object.getAcceptFileType());
		properties.put("acceptFileCount", object.getAcceptFileCount());
		properties.put("maxFileSize", object.getMaxFileSize());
		properties.put("minFileSize", object.getMinFileSize());
		properties.put("remark", object.getRemark());
		properties.put("isImage", object.getIsImage());
		properties.put("isNeeded", object.getIsNeeded());
		properties.put("listType", object.getListType());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_AttachmentCfg> sm_AttachmentCfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_AttachmentCfgList != null)
		{
			for(Sm_AttachmentCfg object:sm_AttachmentCfgList)
			{
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("userUpdate", object.getUserUpdate());
				properties.put("userUpdateId", object.getUserUpdate().getTableId());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("busiType", object.getBusiType());
				properties.put("theName", object.getTheName());
				properties.put("acceptFileType", object.getAcceptFileType());
				properties.put("acceptFileCount", object.getAcceptFileCount());
				properties.put("maxFileSize", object.getMaxFileSize());
				properties.put("minFileSize", object.getMinFileSize());
				properties.put("remark", object.getRemark());
				properties.put("isImage", object.getIsImage());
				properties.put("isNeeded", object.getIsNeeded());
				properties.put("listType", object.getListType());

				list.add(properties);
			}
		}
		return list;
	}


	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin2(List<Sm_AttachmentCfg> sm_AttachmentCfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_AttachmentCfgList != null)
		{
			for(Sm_AttachmentCfg object:sm_AttachmentCfgList)
			{
				Properties properties = new MyProperties();

				properties.put("eCode", object.geteCode());//附件类型编码
				properties.put("tableId", object.getTableId());//附件类型主键
				properties.put("busiType", object.getBusiType());
				properties.put("theName", object.getTheName());//附件档案名称
				properties.put("acceptFileType", object.getAcceptFileType());//接收文件类型
				properties.put("acceptFileCount", object.getAcceptFileCount());//接收文件数量
				properties.put("maxFileSize", object.getMaxFileSize());//最大
				properties.put("minFileSize", object.getMinFileSize());//最小
				properties.put("remark", object.getRemark());//备注
				properties.put("isImage", object.getIsImage());//是否是图片  0-否 1-是
				properties.put("isNeeded", object.getIsNeeded());//是否必须 0-否 1-是
				properties.put("listType", object.getListType());//展示形式
				
				properties.put("isCfgSignature", null==object.getIsCfgSignature()?"0":object.getIsCfgSignature());//是否需要签章

				Sm_AttachmentCfgDataInfo dataInfo = new Sm_AttachmentCfgDataInfo();
				dataInfo.setExtra(object.geteCode());
				dataInfo.setAppid(oss.getAppid());
				dataInfo.setAppsecret(oss.getAppsecret());

				dataInfo.setProject(oss.getProject());
				//如果需要签章文件先提交到本地服务器
				if("1".equals(object.getIsCfgSignature())){
					dataInfo.setRemote("https://test2.czzhengtai.com:19000");
				}else{
					dataInfo.setRemote(oss.getRemote());
				}
				//http://192.168.1.8:19000/oss/material/bananaUpload/uploadMaterial
				String remote = oss.getRemote();
				String subremote = remote.substring(0, remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";

				dataInfo.setUpLoadUrl(subremote);

				properties.put("data", dataInfo);

				List<Sm_Attachment> smAttachmentList = object.getSmAttachmentList();
				List<Properties> list2 = new ArrayList<Properties>();

				if (null != smAttachmentList)
				{
					for (Sm_Attachment sm_Attachment : smAttachmentList)
					{
						Properties properties2 = new MyProperties();
						properties2.put("tableId", sm_Attachment.getTableId());
						Sm_AttachmentCfg attachmentCfg = sm_Attachment.getAttachmentCfg();
						if (attachmentCfg != null)
						{
							properties2.put("sourceType", sm_Attachment.getAttachmentCfg().geteCode());
						}
						properties2.put("sourceId", sm_Attachment.getSourceId());
						properties2.put("busiType", sm_Attachment.getBusiType());
						properties2.put("url", sm_Attachment.getTheLink());
						properties2.put("fileType", sm_Attachment.getFileType());
						properties2.put("name", sm_Attachment.getRemark());
						properties2.put("theSize", sm_Attachment.getTheSize());

						list2.add(properties2);
					}
				}

				properties.put("smAttachmentList", list2);

				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin3(List<Sm_AttachmentCfg_Copy> sm_AttachmentCfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_AttachmentCfgList != null)
		{
			for(Sm_AttachmentCfg_Copy object:sm_AttachmentCfgList)
			{
				Properties properties = new MyProperties();

				properties.put("eCode", object.geteCode());//附件类型编码
				properties.put("tableId", object.getTableId());//附件类型主键
				properties.put("busiType", object.getBusiType());
				properties.put("theName", object.getTheName());//附件档案名称
				properties.put("acceptFileType", object.getAcceptFileType());//接收文件类型
				properties.put("acceptFileCount", object.getAcceptFileCount());//接收文件数量
				properties.put("maxFileSize", object.getMaxFileSize());//最大
				properties.put("minFileSize", object.getMinFileSize());//最小
				properties.put("remark", object.getRemark());//备注
				properties.put("isImage", object.getIsImage());//是否是图片  0-否 1-是
				properties.put("isNeeded", object.getIsNeeded());//是否必须 0-否 1-是
				properties.put("listType", object.getListType());//展示形式
				
				properties.put("isCfgSignature", null==object.getIsCfgSignature()?"0":object.getIsCfgSignature());//是否需要签章

				Sm_AttachmentCfgDataInfo dataInfo = new Sm_AttachmentCfgDataInfo();
				dataInfo.setExtra(object.geteCode());
				dataInfo.setAppid(oss.getAppid());
				dataInfo.setAppsecret(oss.getAppsecret());
				dataInfo.setRemote(oss.getRemote());
				dataInfo.setProject(oss.getProject());

				//http://192.168.1.8:19000/oss/material/bananaUpload/uploadMaterial
				String remote = oss.getRemote();
				String subremote = remote.substring(0, remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";

				dataInfo.setUpLoadUrl(subremote);

				properties.put("data", dataInfo);

				List<Sm_Attachment> smAttachmentList = object.getSmAttachmentList();
				List<Properties> list2 = new ArrayList<Properties>();

				if (null != smAttachmentList)
				{
					for (Sm_Attachment sm_Attachment : smAttachmentList)
					{
						Properties properties2 = new MyProperties();
						properties2.put("tableId", sm_Attachment.getTableId());
						Sm_AttachmentCfg attachmentCfg = sm_Attachment.getAttachmentCfg();
						if (attachmentCfg != null)
						{
							properties2.put("sourceType", sm_Attachment.getAttachmentCfg().geteCode());
						}
						properties2.put("sourceId", sm_Attachment.getSourceId());
						properties2.put("busiType", sm_Attachment.getBusiType());
						properties2.put("url", sm_Attachment.getTheLink());
						properties2.put("fileType", sm_Attachment.getFileType());
						properties2.put("name", sm_Attachment.getRemark());
						properties2.put("theSize", sm_Attachment.getTheSize());

						list2.add(properties2);
					}
				}

				properties.put("smAttachmentList", list2);

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Properties> getListForApproval(List<Sm_AttachmentCfg> sm_AttachmentCfgList, Sm_AttachmentCfgForm model)
	{
		List<Properties> list = getDetailForAdmin2(sm_AttachmentCfgList);

        if (model.getSourceId() == null || model.getSourceId().length() == 0) 
        {  // sourceId 为 null 说明是新增
            return list;
        }

        Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = null;
        
        if (model.getAfId() != null) //审批详情
        {	
        	
            sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findById(model.getAfId());
            int busiCodeAddOrUpdate = approvalDetailEchoUtil.getBusiCodeAddOrUpdate(sm_ApprovalProcess_AF.getBusiCode());
            if("03030101".equals(sm_ApprovalProcess_AF.getBusiCode()) || "03010301".equals(sm_ApprovalProcess_AF.getBusiCode()))
            {
            	return list;
            }
            else
            {
            	if ((S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState())
    					|| S_ApprovalState.Examining.equals(sm_ApprovalProcess_AF.getBusiState()))
    					&& busiCodeAddOrUpdate == 1) //如果是新增待提交，查数据库
                {
                    return list;
                } 
                else //变更审核中已完结等等，查申请单
                {
                    showAttachment(sm_AttachmentCfgList, list, sm_ApprovalProcess_AF);
                }
            }
            
        } 
        else //编辑/详情 直接查申请单，有就用，没有就用数据库的
        {
            //审核的申请单
            Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
            sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
            sm_ApprovalProcess_AFForm.setBusiState("待提交");
            sm_ApprovalProcess_AFForm.setSourceId(MyLong.getInstance().parse(model.getSourceId()));
            sm_ApprovalProcess_AFForm.setBusiCode(model.getApprovalCode());
            sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

            if (sm_ApprovalProcess_AF == null)
            {
                sm_ApprovalProcess_AFForm.setBusiState("审核中");
                sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
            }
            if (sm_ApprovalProcess_AF == null) 
            {
                return list;
            }
            
            int busiCodeAddOrUpdate = approvalDetailEchoUtil.getBusiCodeAddOrUpdate(sm_ApprovalProcess_AF.getBusiCode());
            if (S_ApprovalState.WaitSubmit.equals(sm_ApprovalProcess_AF.getBusiState()) && busiCodeAddOrUpdate == 1) //如果是新增待提交，查数据库
            {
                return list;
            } 
            else //变更审核中已完结等等，查申请单
            {
                showAttachment(sm_AttachmentCfgList, list, sm_ApprovalProcess_AF);
            }
        }
        return list;
	}

    private void showAttachment(List<Sm_AttachmentCfg> sm_AttachmentCfgList, List<Properties> list, Sm_ApprovalProcess_AF sm_ApprovalProcess_AF) 
    {

        String expectObj = sm_ApprovalProcess_AF.getExpectObjJson();
        if (expectObj != null && expectObj.length() > 2 ) {
        	//清空列表重新存放数据
            list.clear();
        	
            BaseForm baseForm = gson.fromJson(expectObj, BaseForm.class);
            Sm_AttachmentForm[] attachmentListOSS = baseForm.getGeneralAttachmentList();

            List<Sm_AttachmentForm> attachmentList = new ArrayList<>();

            if (attachmentListOSS != null && attachmentListOSS.length > 0)
            {
                for (Sm_AttachmentForm obj:attachmentListOSS)
                {
                    attachmentList.add(obj);
                }
            }

            for (Sm_AttachmentCfg object:sm_AttachmentCfgList)
            {
                // 先存基本的数据
                Properties propertiesCfg = getSimpleInfo(object);

                List<Sm_AttachmentForm> list1 = new ArrayList<>(); //存放对应的附件
                List<Sm_AttachmentForm> list2 = new ArrayList<>(); //存放不对应的附件

                for (Sm_AttachmentForm form:attachmentList)
                {
                    if (object.geteCode().equals(form.getSourceType()))
                    {
                        list1.add(form);
                    }
                    else
                    {
                        list2.add(form);
                    }

                    attachmentList = list2;

                    Boolean flag = false;  //flag初始为否，即没有变更

                    for (Sm_AttachmentForm formOSS : list1)
                    {
                        //如果有form没有tableId，说明有新增
                        if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
                        {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) //如果没有新增再看有没有删除
                    {
                        Integer totalCountNew = list1.size();
                        Integer totalCount = null;
                        if(object.getSmAttachmentList() == null)
                        {
                        	totalCount = 0;
                        }
                        else
                        {
                        	totalCount = object.getSmAttachmentList().size();
                        }
                        
                        if (totalCountNew < totalCount)
                        {
                            flag = true;
                        }
                    }

                    if (flag) //flag为true说明有变更
                    {
                        List<Properties> listNew = new ArrayList<>();  //新数据是OSS服务器上的数据
                        for (Sm_AttachmentForm formOSS : list1)
                        {
                            Properties propertiesNew = new MyProperties();

                            propertiesNew.put("tableId", formOSS.getTableId());
                            propertiesNew.put("sourceType", formOSS.getSourceType());
                            propertiesNew.put("sourceId", formOSS.getSourceId());
                            propertiesNew.put("busiType", formOSS.getBusiType());
                            propertiesNew.put("url", formOSS.getTheLink());
                            propertiesNew.put("fileType", formOSS.getFileType());
                            propertiesNew.put("name", formOSS.getRemark());
                            propertiesNew.put("theSize", formOSS.getTheSize());

                            listNew.add(propertiesNew);
                        }

                        List<Properties> listOld = new ArrayList<>();  //新数据是OSS服务器上的数据
                        List<Sm_Attachment> smAttachmentList = new ArrayList<>();
                        if (object.getSmAttachmentList() != null && object.getSmAttachmentList().size() > 0)
                        {
                            smAttachmentList = object.getSmAttachmentList();
                        }
                        for (Sm_Attachment sm_Attachment : smAttachmentList)
                        {
                            Properties propertiesOld = new MyProperties();

                            propertiesOld.put("tableId", sm_Attachment.getTableId());
                            Sm_AttachmentCfg attachmentCfg = sm_Attachment.getAttachmentCfg();
                            if (attachmentCfg != null)
                            {
                                propertiesOld.put("sourceType", sm_Attachment.getAttachmentCfg().geteCode());
                            }
                            propertiesOld.put("sourceId", sm_Attachment.getSourceId());
                            propertiesOld.put("busiType", sm_Attachment.getBusiType());
                            propertiesOld.put("url", sm_Attachment.getTheLink());
                            propertiesOld.put("fileType", sm_Attachment.getFileType());
                            propertiesOld.put("name", sm_Attachment.getRemark());
                            propertiesOld.put("theSize", sm_Attachment.getTheSize());

                            listOld.add(propertiesOld);
                        }

                        propertiesCfg.put("smAttachmentList", listNew);
                        propertiesCfg.put("smAttachmentList_old", listOld);
                    }
                    else
                    {
                        List<Properties> listNew = new ArrayList<>();  //新数据是OSS服务器上的数据
                        List<Sm_Attachment> smAttachmentList = object.getSmAttachmentList();
                        if(smAttachmentList != null)
                        {
                        	for (Sm_Attachment sm_Attachment : smAttachmentList)
                            {
                                Properties propertiesOld = new MyProperties();

                                propertiesOld.put("tableId", sm_Attachment.getTableId());
                                Sm_AttachmentCfg attachmentCfg = sm_Attachment.getAttachmentCfg();
                                if (attachmentCfg != null)
                                {
                                    propertiesOld.put("sourceType", sm_Attachment.getAttachmentCfg().geteCode());
                                }
                                propertiesOld.put("sourceId", sm_Attachment.getSourceId());
                                propertiesOld.put("busiType", sm_Attachment.getBusiType());
                                propertiesOld.put("url", sm_Attachment.getTheLink());
                                propertiesOld.put("fileType", sm_Attachment.getFileType());
                                propertiesOld.put("name", sm_Attachment.getRemark());
                                propertiesOld.put("theSize", sm_Attachment.getTheSize());

                                listNew.add(propertiesOld);
                            }
                        }
                        
                        // 不显示旧数据但是要传个空列表过去
                        propertiesCfg.put("smAttachmentList", listNew);
                        propertiesCfg.put("smAttachmentList_old", new ArrayList<>());
                    }
                }

                list.add(propertiesCfg);
            }
        }
    }

//	private class DataInfo{
//		
//		public String extra;
//		public String appid;
//		public String appsecret;
//		
//		public void setExtra(String extra)
//		{
//			this.extra = extra;
//		}
//		public void setAppid(String appid)
//		{
//			this.appid = appid;
//		}
//		public void setAppsecret(String appsecret)
//		{
//			this.appsecret = appsecret;
//		}
//		
//		
//		
//	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAcceptType(List<Sm_AttachmentCfg> sm_AttachmentCfgList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_AttachmentCfgList != null)
		{
			for(Sm_AttachmentCfg object:sm_AttachmentCfgList)
			{
				Properties properties = new MyProperties();

				if( object.getTableId()!=null){
					properties.put("tableId", object.getTableId());//附件类型主键
				}
				if(object.getBusiType()!=null){
					properties.put("busiType", object.getBusiType());
				}
				if(object.getTheName()!=null){
					properties.put("theName", object.getTheName());//附件档案名称
				}
				if(object.getAcceptFileType()!=null){
					String AcceptFileType="";
					 String[] AcceptfileTypeArray=	object.getAcceptFileType().split("/");
					  for (S_AcceptFileType s_AcceptFileType :S_AcceptFileType.values()){
						  for(int i=0;i<AcceptfileTypeArray.length;i++){
								if(AcceptfileTypeArray[i].equals(s_AcceptFileType.getType())){
									AcceptFileType+=s_AcceptFileType.getName()+",";
								}
						  }
					  }
					  if(AcceptFileType.length()>0){
						  AcceptFileType=AcceptFileType.substring(0, AcceptFileType.length()-1);
					  }
					properties.put("acceptFileType", AcceptFileType);//接收文件类型
				}
				if(object.getAcceptFileCount()!=null){
					properties.put("acceptFileCount", object.getAcceptFileCount());//接收文件数量
				}
				if(object.getMaxFileSize()!=null){
					properties.put("maxFileSize", object.getMaxFileSize());//最大
				}
				if( object.getMinFileSize()!=null){
					properties.put("minFileSize", object.getMinFileSize());//最小
				}
				if(object.getRemark()!=null){
					properties.put("remark", object.getRemark());//备注
				}
				if(object.getIsImage()!=null){
					properties.put("isImage", object.getIsImage());//是否是图片  0-否 1-是
				}
				if(object.getIsNeeded()!=null){
					properties.put("isNeeded", object.getIsNeeded());//是否必须 0-否 1-是
				}
				if( object.getListType()!=null){
					properties.put("listType", object.getListType());//展示形式
				}
				if( object.getIsCfgSignature() != null)
				{
					properties.put("isCfgSignature", object.getIsCfgSignature());//是否签章
				}
				else
				{
					properties.put("isCfgSignature", "0");//是否签章
				}
				/*if(object.getCreateTimeStamp()!=null){
					//时间处理
					properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
				}			
				if(object.getUserStart()!=null){
					properties.put("realName", object.getUserStart().getRealName());//创建人
				}*/
				list.add(properties);
			}
		}
		return list;
	}

	public Properties getSimpleInfoToupdate(Sm_AttachmentCfg object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		if( object.getTableId()!=null){
			properties.put("tableId", object.getTableId());//附件类型主键
		}
		if(object.getBusiType()!=null){
			properties.put("busiType", object.getBusiType());
		}
		if(object.getTheName()!=null){
			properties.put("theName", object.getTheName());//附件档案名称
		}
		if(object.getAcceptFileType()!=null){
			String AcceptFileType="";
			 String[] AcceptfileTypeArray=	object.getAcceptFileType().split("/");
			  for (S_AcceptFileType s_AcceptFileType :S_AcceptFileType.values()){
				  for(int i=0;i<AcceptfileTypeArray.length;i++){
						if(AcceptfileTypeArray[i].equals(s_AcceptFileType.getType())){
							AcceptFileType+=s_AcceptFileType.getName()+",";
						}
				  }
			  }
			  if(AcceptFileType.length()>0){
				  AcceptFileType=AcceptFileType.substring(0, AcceptFileType.length()-1);
			  }
			properties.put("acceptFileType", AcceptFileType);//接收文件类型
		}
		if(object.getAcceptFileCount()!=null){
			properties.put("acceptFileCount", object.getAcceptFileCount());//接收文件数量
		}
		if(object.getMaxFileSize()!=null){
			properties.put("maxFileSize", object.getMaxFileSize());//最大
		}
		if( object.getMinFileSize()!=null){
			properties.put("minFileSize", object.getMinFileSize());//最小
		}
		if(object.getRemark()!=null){
			properties.put("remark", object.getRemark());//备注
		}
		if(object.getIsImage()!=null){
			properties.put("isImage", object.getIsImage());//是否是图片  0-否 1-是
		}
		if(object.getIsNeeded()!=null){
			properties.put("isNeeded", object.getIsNeeded());//是否必须 0-否 1-是
		}
		if( object.getListType()!=null){
			properties.put("listType", object.getListType());//展示形式
		}
		if( object.getIsCfgSignature() != null)
		{
			properties.put("isCfgSignature", object.getIsCfgSignature());//是否签章
		}
		else
		{
			properties.put("isCfgSignature", "0");//是否签章
		}
		/*if(object.getCreateTimeStamp()!=null){
			//时间处理
			properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		}*/
		return properties;
	}

}
