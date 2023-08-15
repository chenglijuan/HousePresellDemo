package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngUpdatesService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	
	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
		
		//协议版本名称（唯一）
		String theName = model.getTheName();
		Tgxy_CoopAgreementVerMngForm tgxy_CoopAgreementVerMngForm=new Tgxy_CoopAgreementVerMngForm();
		tgxy_CoopAgreementVerMngForm.setTheName(theName);
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMngList=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), tgxy_CoopAgreementVerMngForm).getResultList();
		if(tgxy_CoopAgreementVerMngList.size()>1){
			return MyBackInfo.fail(properties, "协议版本名称已存在");
		}
		//判断启用时间是否存在
		//启用时间
		String enableTimeStamp = model.getEnableTimeStamp();
		Tgxy_CoopAgreementVerMngForm tgxy_CoopAgreementVerMngForm1=new Tgxy_CoopAgreementVerMngForm();
		tgxy_CoopAgreementVerMngForm1.setEnableTimeStamp(enableTimeStamp);
		tgxy_CoopAgreementVerMngForm1.setTheState(S_TheState.Normal);
		tgxy_CoopAgreementVerMngForm1.setTheType(model.getTheType());
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMngList1=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), tgxy_CoopAgreementVerMngForm1).getResultList();
		if(tgxy_CoopAgreementVerMngList1.size()>1){
			return MyBackInfo.fail(properties, "启用时间已存在");
		}
		//版本号
		String theVersion = model.getTheVersion();
		Tgxy_CoopAgreementVerMngForm tgxy_CoopAgreementVerMngForm2=new Tgxy_CoopAgreementVerMngForm();
		tgxy_CoopAgreementVerMngForm2.setTheVersion(theVersion);
		tgxy_CoopAgreementVerMngForm2.setTheState(S_TheState.Normal);
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMngList2=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), tgxy_CoopAgreementVerMngForm2).getResultList();
		if(tgxy_CoopAgreementVerMngList2.size()>1){
			return MyBackInfo.fail(properties, "版本号已存在");
		}
		//合作协议类型
		String theType = model.getTheType();
		//操作人
		Sm_User updateUser=model.getUser();
		if(updateUser == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		//操作日期
		Long lastUpdateTimeStamp = System.currentTimeMillis();
		
		
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "协议版本名称不能为空");
		}
		if(theVersion == null || theVersion.length() == 0)
		{
			return MyBackInfo.fail(properties, "版本号不能为空");
		}
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "合作协议类型不能为空");
		}
		if(enableTimeStamp == null || enableTimeStamp.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "启用时间不能为空");
		}		
	
		Long tgxy_CoopAgreementVerMngId = model.getTableId();
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = (Tgxy_CoopAgreementVerMng)tgxy_CoopAgreementVerMngDao.findById(tgxy_CoopAgreementVerMngId);
		if(tgxy_CoopAgreementVerMng == null)
		{
			return MyBackInfo.fail(properties, "'合作协议(Id:" + tgxy_CoopAgreementVerMngId + ")'不存在");
		}
		
		//tgxy_CoopAgreementVerMng.setTheState1(theState1);
		tgxy_CoopAgreementVerMng.setUserStart(updateUser);
		tgxy_CoopAgreementVerMng.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgxy_CoopAgreementVerMng.setTheName(theName);
		tgxy_CoopAgreementVerMng.setTheVersion(theVersion);
		tgxy_CoopAgreementVerMng.setTheType(theType);
		tgxy_CoopAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
		tgxy_CoopAgreementVerMng.setDownTimeStamp(null);
	
		tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);
		/*
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		// 附件信息
		String smAttachmentJson = null;
		if (null != model.getSmAttachmentList() && model.getSmAttachmentList().length() > 0)
		{
			// 根据退房退款ID进行查询附件功能
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			String sourceId = String.valueOf(model.getTableId());
			from.setTheState(S_TheState.Normal);
			from.setSourceId(sourceId);

			// 查询附件
			@SuppressWarnings("unchecked")
			List<Sm_Attachment> smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), from));
			// 删除附件
			if (null != smAttachmentList && smAttachmentList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : smAttachmentList)
				{
					sm_Attachment.setTheState(S_TheState.Deleted);
					smAttachmentDao.save(sm_Attachment);
				}
			}

			// 重新保存附件
			smAttachmentJson = model.getSmAttachmentList().toString();
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);

			if (null != gasList && gasList.size() > 0)
			{
				for (Sm_Attachment sm_Attachment : gasList)
				{
					//查询附件配置表
					Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
					form.seteCode(sm_Attachment.getSourceType());
					Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
					
					sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
					sm_Attachment.setSourceId(model.getTableId().toString());
					sm_Attachment.setTheState(S_TheState.Normal);
					smAttachmentDao.save(sm_Attachment);
				}
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
