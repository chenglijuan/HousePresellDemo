package zhishusz.housepresell.service;

import java.util.Properties;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_AcceptFileType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgUpdateService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		
		
		Long tableId= model.getTableId();			
		String theName = model.getTheName();
		String acceptFileType = model.getAcceptFileType();
		Integer acceptFileCount = model.getAcceptFileCount();
		Integer maxFileSize = model.getMaxFileSize();
		Integer minFileSize = model.getMinFileSize();
		String remark = model.getRemark();
		Boolean isImage = model.getIsImage();
		Boolean isNeeded = model.getIsNeeded();
		String listType = model.getListType();				
		long lastUpdateTimeStamp=System.currentTimeMillis();
		
		String isCfgSignature = model.getIsCfgSignature();
		
		/*if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(busiType == null || busiType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiType'不能为空");
		}*/
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "附件类型名称不能为空");
		}
		if(acceptFileType == null || acceptFileType.length() == 0)
		{
			return MyBackInfo.fail(properties, "可接受文件类型不能为空");
		}
		if(acceptFileCount == null || acceptFileCount < 1)
		{
			return MyBackInfo.fail(properties, "可接收文件数量不能为空");
		}
		if(maxFileSize == null || maxFileSize < 1)
		{
			return MyBackInfo.fail(properties, "文件大小的最大值不能为空");
		}
		if(minFileSize == null || minFileSize < 1)
		{
			return MyBackInfo.fail(properties, "文件大小的最小值不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "备注不能为空");
		}
		if(isImage == null)
		{
			return MyBackInfo.fail(properties, "是否是图片不能为空");
		}
		if(isNeeded == null)
		{
			return MyBackInfo.fail(properties, "是否必须上传不能为空");
		}
		if(listType == null || listType.length() == 0)
		{
			return MyBackInfo.fail(properties, "附件列表类型不能为空");
		}
		if(isCfgSignature == null || isCfgSignature.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择是否需要签章");
		}
		
		Sm_User userUpdate = model.getUser();;
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}

	
		/*Long sm_AttachmentCfgId = model.getTableId();
		Sm_AttachmentCfg sm_AttachmentCfg = (Sm_AttachmentCfg)sm_AttachmentCfgDao.findById(sm_AttachmentCfgId);
		if(sm_AttachmentCfg == null)
		{
			return MyBackInfo.fail(properties, "'Sm_AttachmentCfg(Id:" + sm_AttachmentCfgId + ")'不存在");
		}*/
		
		//选择文件签章时，可接受类型必须是PDF
		if("1".equals(isCfgSignature))
		{
			if(!"PDF".equals(acceptFileType))
			{
				return MyBackInfo.fail(properties, "需要签章时，文件类型必须是PDF！");
			}
		}
		
		Sm_AttachmentCfg sm_AttachmentCfg=sm_AttachmentCfgDao.findById(tableId);
		if(sm_AttachmentCfg==null){
			return MyBackInfo.fail(properties, "'Sm_AttachmentCfg(Id:" + tableId + ")'不存在");
		}
				
		//获取可接受文件的类型
		String newacceptFileType="";
		String[] acceptFileTypes=acceptFileType.split(",");
		for (S_AcceptFileType s_AcceptFileType :S_AcceptFileType.values()) {
			for(int i=0;i<acceptFileTypes.length;i++){
				if(s_AcceptFileType.getName().equals(acceptFileTypes[i])){
					newacceptFileType+=	s_AcceptFileType.getType()+"/";
				}			
			}
		}
		if(newacceptFileType!=null){
			newacceptFileType.substring(0,newacceptFileType.length()-1);
		}
		//保存的信息
		sm_AttachmentCfg.setTheName(theName);
		sm_AttachmentCfg.setAcceptFileType(newacceptFileType);
		sm_AttachmentCfg.setAcceptFileCount(acceptFileCount);
		sm_AttachmentCfg.setMaxFileSize(maxFileSize);
		sm_AttachmentCfg.setMinFileSize(minFileSize);
		sm_AttachmentCfg.setIsImage(isImage);
		sm_AttachmentCfg.setIsNeeded(isNeeded);
		sm_AttachmentCfg.setListType(listType);
		sm_AttachmentCfg.setUserUpdate(userUpdate);
		sm_AttachmentCfg.setRemark(remark);
		sm_AttachmentCfg.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		sm_AttachmentCfg.setIsCfgSignature(isCfgSignature);			
		sm_AttachmentCfgDao.save(sm_AttachmentCfg);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
