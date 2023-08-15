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
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngAddsService
{
	private static final String BUSI_CODE = "06110102";
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
			
		//协议版本名称（唯一）
		String theName = model.getTheName();
		Tgxy_CoopAgreementVerMngForm tgxy_CoopAgreementVerMngForm=new Tgxy_CoopAgreementVerMngForm();
		tgxy_CoopAgreementVerMngForm.setTheName(theName);
		tgxy_CoopAgreementVerMngForm.setTheState(S_TheState.Normal);
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMngList=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), tgxy_CoopAgreementVerMngForm).getResultList();
		if(tgxy_CoopAgreementVerMngList.size()>0){
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
		if(tgxy_CoopAgreementVerMngList1.size()>0){
			return MyBackInfo.fail(properties, "启用时间已存在");
		}
		//版本号
		String theVersion = model.getTheVersion();
		Tgxy_CoopAgreementVerMngForm tgxy_CoopAgreementVerMngForm2=new Tgxy_CoopAgreementVerMngForm();
		tgxy_CoopAgreementVerMngForm2.setTheVersion(theVersion);
		tgxy_CoopAgreementVerMngForm2.setTheState(S_TheState.Normal);
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMngList2=tgxy_CoopAgreementVerMngDao.getQuery(tgxy_CoopAgreementVerMngDao.getBasicHQL(), tgxy_CoopAgreementVerMngForm2).getResultList();
		if(tgxy_CoopAgreementVerMngList2.size()>0){
			return MyBackInfo.fail(properties, "版本号已存在");
		}
		//合作协议类型
		String theType = model.getTheType();
		//操作人
		Sm_User operateUser=model.getUser();
		if(operateUser == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录");
		}
		//操作日期
		Long createTimeStamp = System.currentTimeMillis();
		
		//业务编码
		String busiState = "未备案";
		
		
		
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
		
			
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = new Tgxy_CoopAgreementVerMng();
		//tgxy_CoopAgreementVerMng.setTheState1(theState1);
		tgxy_CoopAgreementVerMng.setTheState(S_TheState.Normal);
		tgxy_CoopAgreementVerMng.setApprovalState(S_ApprovalState.WaitSubmit);
		tgxy_CoopAgreementVerMng.setBusiState(busiState);
		tgxy_CoopAgreementVerMng.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgxy_CoopAgreementVerMng.setUserStart(operateUser);
		tgxy_CoopAgreementVerMng.setCreateTimeStamp(createTimeStamp);
		tgxy_CoopAgreementVerMng.setTheName(theName);
		tgxy_CoopAgreementVerMng.setTheVersion(theVersion);
		tgxy_CoopAgreementVerMng.setTheType(theType);
		tgxy_CoopAgreementVerMng.setEnableTimeStamp(enableTimeStamp);
		tgxy_CoopAgreementVerMng.setDownTimeStamp(null);
		
		Serializable entity=tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);

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
