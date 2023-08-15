package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
	
/*
 * Service添加操作：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_EscrowStandardVerMngAddService
{
	private static final String BUSI_CODE = "06010101";//具体业务编码参看SVN文
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	public Properties execute(Tgpj_EscrowStandardVerMngForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		model.setTheState(theState);
//		String busiState = model.getBusiState();
		String eCode =  sm_BusinessCodeGetService.execute(BUSI_CODE); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
		Long createTimeStamp = System.currentTimeMillis();
		String theName = model.getTheName();
		String theVersion = model.getTheVersion();
		String theType = model.getTheType();
//		String theContent = model.getTheContent();
		Double amount = model.getAmount();
		Double percentage = model.getPercentage();
		String extendParameter1 = model.getExtendParameter1();
		String extendParameter2 = model.getExtendParameter2();

		long beginExpirationDate = model.getBeginExpirationDate();
		long endExpirationDate = model.getEndExpirationDate();
		Boolean hasEnable = true;   //10-30新改动，不需要启用状态，所有设置成默认启用， model.getHasEnable()


		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管标准协议版本号不能为空");
		}
		if(theVersion == null || theVersion.length() == 0)
		{
			return MyBackInfo.fail(properties, "版本号不能为空");
		}
		if(Double.valueOf(theVersion) <= 0)
		{
			return MyBackInfo.fail(properties, "版本号不正确");
		}

		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管标准类型不能为空");
		}
//		if(theContent == null || theContent.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'theContent'不能为空");
//		}
		if(S_EscrowStandardType.StandardAmount.equals(theType) && amount == null)
		{
			return MyBackInfo.fail(properties, "托管标准不能为空");
		}
		if(S_EscrowStandardType.StandardPercentage.equals(theType) && percentage == null)
		{
			return MyBackInfo.fail(properties, "托管标准不能为空");
		}
		if(S_EscrowStandardType.StandardAmount.equals(theType) && amount <= 0)
		{
			return MyBackInfo.fail(properties, "标准金额不正确");
		}
		if(S_EscrowStandardType.StandardPercentage.equals(theType) && (percentage <= 0 || percentage > 100.0))
		{
			return MyBackInfo.fail(properties, "标准比例不正确，请输入0~100之间的数。");
		}
//		if(extendParameter1 == null || extendParameter1.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'extendParameter1'不能为空");
//		}
//		if(extendParameter2 == null || extendParameter2.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'extendParameter2'不能为空");
//		}
		if(beginExpirationDate <= 0)
		{
			return MyBackInfo.fail(properties, "启用日期不能为空");
		}
//		if(endExpirationDate == null || endExpirationDate < 1)
//		{
//			return MyBackInfo.fail(properties, "'endExpirationDate'不能为空");
//		}

		Sm_User userStart = (Sm_User)model.getUser();
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在，请先登录");
		}

		Long beginTimeStamp = model.getBeginExpirationDate();
		Long endTimeStamp = model.getEndExpirationDate();
		model.setBeginExpirationDate(null);
		model.setEndExpirationDate(null);
		Integer totalCount = tgpj_EscrowStandardVerMngDao.findByPage_Size(tgpj_EscrowStandardVerMngDao.getQuery_Size(tgpj_EscrowStandardVerMngDao.getEexistenceHQL(), model));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "存在重复托管标准协议版本号");
		}
		model.setTheName(null);
		model.setBeginExpirationDate(beginTimeStamp);
		model.setEndExpirationDate(endTimeStamp);
		totalCount = tgpj_EscrowStandardVerMngDao.findByPage_Size(tgpj_EscrowStandardVerMngDao.getQuery_Size(tgpj_EscrowStandardVerMngDao.getEexistenceHQL(), model));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "存在重复启用、停用日期");
		}

		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
		
		Tgpj_EscrowStandardVerMng tgpj_EscrowStandardVerMng = new Tgpj_EscrowStandardVerMng();
		tgpj_EscrowStandardVerMng.setTheState(theState);
//		tgpj_EscrowStandardVerMng.setBusiState(busiState);
		tgpj_EscrowStandardVerMng.seteCode(eCode);
		tgpj_EscrowStandardVerMng.setUserStart(userStart);
		tgpj_EscrowStandardVerMng.setCreateTimeStamp(createTimeStamp);
		tgpj_EscrowStandardVerMng.setTheName(theName);
		tgpj_EscrowStandardVerMng.setTheVersion(theVersion);
		tgpj_EscrowStandardVerMng.setTheType(theType);
//		tgpj_EscrowStandardVerMng.setTheContent(theContent);
		tgpj_EscrowStandardVerMng.setAmount(MyDouble.getInstance().getShort(amount, 0));
		tgpj_EscrowStandardVerMng.setPercentage(MyDouble.getInstance().getShort(percentage, 0));
		tgpj_EscrowStandardVerMng.setExtendParameter1(extendParameter1);
		tgpj_EscrowStandardVerMng.setExtendParameter2(extendParameter2);
		tgpj_EscrowStandardVerMng.setBeginExpirationDate(beginExpirationDate);
		tgpj_EscrowStandardVerMng.setEndExpirationDate(endExpirationDate);
		tgpj_EscrowStandardVerMng.setHasEnable(hasEnable);

		//审批流
		properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
		//没有配置审批流程无需走审批流直接保存数据库
		if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			tgpj_EscrowStandardVerMng.setUserRecord(userStart);
			tgpj_EscrowStandardVerMng.setRecordTimeStamp(createTimeStamp); //已备案的添加备案人、备案日期
			tgpj_EscrowStandardVerMng.setBusiState(S_BusiState.HaveRecord); //已备案
			tgpj_EscrowStandardVerMng.setApprovalState(S_ApprovalState.Completed); //已完结
			tgpj_EscrowStandardVerMngDao.save(tgpj_EscrowStandardVerMng);
			
			sm_AttachmentBatchAddService.execute(model, tgpj_EscrowStandardVerMng.getTableId());
		}
		else
		{
			//判断是否满足审批条件（有审批角色，单审批流程）
			if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}

			tgpj_EscrowStandardVerMng.setBusiState(S_BusiState.NoRecord); //未备案
			tgpj_EscrowStandardVerMngDao.save(tgpj_EscrowStandardVerMng);

			sm_AttachmentBatchAddService.execute(model, tgpj_EscrowStandardVerMng.getTableId());

			//审批操作
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
			sm_approvalProcessService.execute(tgpj_EscrowStandardVerMng, model, sm_approvalProcess_cfg);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_EscrowStandardVerMng.getTableId());

		return properties;
	}
}
