package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementVerMngAddsService
{
	private static final String BUSI_CODE = "06110103";
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;
	@Autowired
	private Tgxy_CoopAgreementVerMngDao Tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	public Properties execute(Tgxy_TripleAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();

		//协议版本名称(唯一)
		String theName=model.getTheName();
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm.setTheName(theName);
		tgxy_TripleAgreementVerMngForm.setTheState(S_TheState.Normal);
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm).getResultList();
		if(tgxy_TripleAgreementVerMnglist.size()>0){
			return MyBackInfo.fail(properties, "协议版本名称已经存在");
		}
		//判断启用时间是否存在
		//启用时间(将String类型转换成long类型)
		Long enableTimeStamp = MyDatetime.getInstance().stringToLong(model.getP1().toString());
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm1=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm1.setEnableTimeStamp(enableTimeStamp);
		tgxy_TripleAgreementVerMngForm1.setTheType(model.getTheType());
		tgxy_TripleAgreementVerMngForm1.setTheState(S_TheState.Normal);
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist1=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm1).getResultList();
		if(tgxy_TripleAgreementVerMnglist1.size()>0){
			return MyBackInfo.fail(properties, "启用时间已经存在");
		}
		
		//版本号(唯一)
		String theVersion=model.getTheVersion();
		Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm2=new Tgxy_TripleAgreementVerMngForm();
		tgxy_TripleAgreementVerMngForm2.setTheVersion(theVersion);
		tgxy_TripleAgreementVerMngForm2.setTheState(S_TheState.Normal);
		List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist2=tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQL(), tgxy_TripleAgreementVerMngForm2).getResultList();
		if(tgxy_TripleAgreementVerMnglist2.size()>0){
			return MyBackInfo.fail(properties, "版本号已经存在");
		}
		
		//三方协议类型
		String theType=	model.getTheType();
		
		//合作协议版本号(三方协议需要跟合作协议关联)
		String eCodeOfCooperationAgreement=model.geteCodeOfCooperationAgreement();	
		//合作协议版本名称(三方协议需要跟合作协议关联)
		String theNameOfCooperationAgreement=model.getTheNameOfCooperationAgreement();
				
		//模板内容样式
		String templateContentStyle = model.getTemplateContentStyle();
		//操作人
		Sm_User userStart= model.getUser();	
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		//操作日期
		long createTimeStamp=System.currentTimeMillis();		
		//备案或未备案(审核通过之后算备案)
		String busiState ="未备案";
		
		
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
		if(eCodeOfCooperationAgreement == null || eCodeOfCooperationAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "合作协议版本号不能为空");
		}
		if(theNameOfCooperationAgreement == null || theNameOfCooperationAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "合作协议版本名称不能为空");
		}
				
		/*if(templateContentStyle == null || templateContentStyle.length() == 0)
		{
			return MyBackInfo.fail(properties, "'templateContentStyle'不能为空");
		}*/
	
		Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng = new Tgxy_TripleAgreementVerMng();		
		tgxy_TripleAgreementVerMng.setBusiState(busiState);
		tgxy_TripleAgreementVerMng.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgxy_TripleAgreementVerMng.setApprovalState(S_ApprovalState.WaitSubmit);
		tgxy_TripleAgreementVerMng.setTheName(theName);
		tgxy_TripleAgreementVerMng.setTheVersion(theVersion);
		tgxy_TripleAgreementVerMng.setTheType(theType);
		tgxy_TripleAgreementVerMng.seteCodeOfCooperationAgreement(eCodeOfCooperationAgreement);
		tgxy_TripleAgreementVerMng.setTheNameOfCooperationAgreement(theNameOfCooperationAgreement);
		tgxy_TripleAgreementVerMng.setTheState(S_TheState.Normal);
		//tgxy_TripleAgreementVerMng.setTheState1(theState1);
		tgxy_TripleAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
		tgxy_TripleAgreementVerMng.setDownTimeStamp(null);
		tgxy_TripleAgreementVerMng.setTemplateContentStyle(templateContentStyle);
		tgxy_TripleAgreementVerMng.setUserStart(userStart);
		tgxy_TripleAgreementVerMng.setCreateTimeStamp(createTimeStamp);
			
		Serializable entity=tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);
		// 附件信息
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}

		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

		if (null != gasList && gasList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				sm_Attachment.setSourceId(entity.toString());
				sm_Attachment.setTheState(S_TheState.Normal);
				smAttachmentDao.save(sm_Attachment);
			}
		}
		properties.put("tableId",entity.toString());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
