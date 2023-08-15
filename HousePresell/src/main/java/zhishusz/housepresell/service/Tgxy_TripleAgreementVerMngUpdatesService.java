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
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementVerMngUpdatesService
{
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;
	@Autowired
	private Tgxy_CoopAgreementVerMngDao Tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	
	public Properties execute(Tgxy_TripleAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableId=model.getTableId();
		String theName = model.getTheName();
		String theVersion = model.getTheVersion();
		String theType = model.getTheType();		
		
		//协议版本名称唯一
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm.setTheName(theName);
		tgxy_TripleAgreementVerMngForm.setTheState(S_TheState.Normal);
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMngList=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm).getResultList();
		if(tgxy_TripleAgreementVerMngList.size()>1){
			return MyBackInfo.fail(properties, "协议版本名称已存在");
		}
		//判断启用时间是否存在
		//启用时间(将String类型转换成long类型)
		Long enableTimeStamp = MyDatetime.getInstance().stringToLong(model.getP1().toString());
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm1=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm1.setEnableTimeStamp(enableTimeStamp);
		tgxy_TripleAgreementVerMngForm1.setTheState(S_TheState.Normal);
		tgxy_TripleAgreementVerMngForm1.setTheType(model.getTheType());
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist1=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm).getResultList();
		if(tgxy_TripleAgreementVerMnglist1.size()>1){
			return MyBackInfo.fail(properties, "启用时间已经存在");
		}
		//版本号(唯一)
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm2=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm2.setTheVersion(theVersion);
		tgxy_TripleAgreementVerMngForm2.setTheState(S_TheState.Normal);
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist2=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm2).getResultList();
		if(tgxy_TripleAgreementVerMnglist2.size()>1){
			return MyBackInfo.fail(properties, "版本号已经存在");
		}
		
		//操作人
		Sm_User userUpdate= model.getUser();	
		if(userUpdate == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		//操作日期
		long lastUpdateTimeStamp=System.currentTimeMillis();			
				
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
			return MyBackInfo.fail(properties, "三方协议类型不能为空");
		}
		if(enableTimeStamp == null || enableTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "启用时间不能为空");
		}	
	
		Long tgxy_TripleAgreementVerMngId = model.getTableId();
		Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng = (Tgxy_TripleAgreementVerMng)tgxy_TripleAgreementVerMngDao.findById(tgxy_TripleAgreementVerMngId);
		if(tgxy_TripleAgreementVerMng == null)
		{
			return MyBackInfo.fail(properties, "'Tgxy_TripleAgreementVerMng(Id:" + tgxy_TripleAgreementVerMngId + ")'不存在");
		}
		
		//tgxy_TripleAgreementVerMng.setTheState1(theState1);
	
		tgxy_TripleAgreementVerMng.setLastUpdateTimeStamp(lastUpdateTimeStamp);	
		tgxy_TripleAgreementVerMng.setTheName(theName);
		tgxy_TripleAgreementVerMng.setTheVersion(theVersion);
		tgxy_TripleAgreementVerMng.setTheType(theType);
		tgxy_TripleAgreementVerMng.setUserUpdate(userUpdate);
		tgxy_TripleAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
		tgxy_TripleAgreementVerMng.setDownTimeStamp(null);
		
		/*tgxy_TripleAgreementVerMng.setTemplateContentStyle(templateContentStyle);*/
	
		tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);
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

			String sourceId = String.valueOf(tableId);
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
					sm_Attachment.setSourceId(tableId.toString());
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
