package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_LandMortgageState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
	
/*
 * Service添加操作：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfoAddService
{
	private static final String BUSI_CODE = "03010201";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;
	
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		Sm_User userLogin = (Sm_User)sm_UserDao.findById(model.getUserId());
		if(userLogin == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		Long projectId = model.getProjectId();
		Long tgpj_EscrowStandardVerMngId = model.getTgpj_EscrowStandardVerMngId();
		Double buildingArea = model.getBuildingArea();
		Double escrowArea = model.getEscrowArea();
		String deliveryType = model.getDeliveryType();
		Double upfloorNumber = model.getUpfloorNumber();
		Double downfloorNumber = model.getDownfloorNumber();
		String remark = model.getRemark();
		Integer landMortgageState = model.getLandMortgageState();
		Double landMortgageAmount = model.getLandMortgageAmount();
		String landMortgagor = model.getLandMortgagor();
		Double paymentLines = model.getPaymentLines();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		
		//判断是否有对应的审批流程配置
		properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
		if("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			return properties;
		}
		
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "请选择项目");
		}
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*if(tgpj_EscrowStandardVerMngId == null || tgpj_EscrowStandardVerMngId < 1)
		{
			return MyBackInfo.fail(properties, "请选择托管标准");
		}*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
		{
			return MyBackInfo.fail(properties, "请输入施工编号");
		}
//		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "请输入公安编号");
//		}
		if(buildingArea == null || buildingArea < 1)
		{
			return MyBackInfo.fail(properties, "请输入有效的建筑面积");
		}
		if(escrowArea == null || escrowArea < 1)
		{
			return MyBackInfo.fail(properties, "请输入有效的托管面积");
		}
		if(escrowArea > buildingArea)
		{
			return MyBackInfo.fail(properties, "托管面积不得大于建筑面积");
		}
		if(deliveryType == null || deliveryType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择房屋类型");
		}
		if(upfloorNumber == null || upfloorNumber < 1)
		{
			return MyBackInfo.fail(properties, "请输入有效的地上楼层数");
		}
		if(MyDouble.getInstance().parse(downfloorNumber) == null || MyDouble.getInstance().parse(downfloorNumber) < 0)
		{
			return MyBackInfo.fail(properties, "请输入有效的地下楼层数");
		}
		if(landMortgageState == null || landMortgageState < 0)
		{
			return MyBackInfo.fail(properties, "请选择土地抵押状态");
		}
		if(S_LandMortgageState.Yes.equals(landMortgageState))
		{
			if(landMortgagor == null || landMortgagor.length() == 0)
			{
				return MyBackInfo.fail(properties, "请输入土地抵押权人");
			}
			if(MyDouble.getInstance().parse(landMortgageAmount) == null)
			{
				return MyBackInfo.fail(properties, "请输入有效的土地抵押金额");
			}
		}
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*if(MyDouble.getInstance().parse(paymentLines) == null)
		{
			return MyBackInfo.fail(properties, "请选择支付保证上限比例");
		}*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		Empj_ProjectInfo project = empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "选择的项目不存在");
		}
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*Tgpj_EscrowStandardVerMng escrowStandardVerMng = tgpj_EscrowStandardVerMngDao.findById(tgpj_EscrowStandardVerMngId);
		if(escrowStandardVerMng == null)
		{
			return MyBackInfo.fail(properties, "选择的托管标准不存在");
		}*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if(!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}
		
		Empj_BuildingExtendInfo empj_BuildingExtendInfo = new Empj_BuildingExtendInfo();
		
		if(S_LandMortgageState.Yes.equals(landMortgageState))
		{
			empj_BuildingExtendInfo.setLandMortgagor(landMortgagor);
			empj_BuildingExtendInfo.setLandMortgageAmount(landMortgageAmount);
		}
		
		empj_BuildingExtendInfo.setUserStart(userLogin);
		empj_BuildingExtendInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_BuildingExtendInfo.setUserUpdate(userLogin);
		empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		empj_BuildingExtendInfo.setTheState(S_TheState.Normal);
		empj_BuildingExtendInfo.setLandMortgageState(landMortgageState);
		empj_BuildingExtendInfo.setEscrowState(S_EscrowState.UnEscrowState);
		empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
		
		Empj_BuildingInfo empj_BuildingInfo = new Empj_BuildingInfo();
		
		empj_BuildingInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
		empj_BuildingInfo.seteCodeFromConstruction(eCodeFromConstruction);
		empj_BuildingInfo.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		empj_BuildingInfo.setProject(project);
		empj_BuildingInfo.setCityRegion(project.getCityRegion());
		empj_BuildingInfo.setTheNameOfProject(project.getTheName());
		empj_BuildingInfo.setDevelopCompany(project.getDevelopCompany());
		empj_BuildingInfo.setBuildingArea(buildingArea);
		empj_BuildingInfo.setEscrowArea(escrowArea);
		empj_BuildingInfo.setDeliveryType(deliveryType);
		empj_BuildingInfo.setUpfloorNumber(upfloorNumber);
		empj_BuildingInfo.setDownfloorNumber(downfloorNumber);
		empj_BuildingInfo.setRemark(remark);
		empj_BuildingInfo.setExtendInfo(empj_BuildingExtendInfo);
		empj_BuildingInfo.setTheState(S_TheState.Normal);
		empj_BuildingInfo.setUserStart(userLogin);
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*empj_BuildingInfo.setEscrowStandardVerMng(escrowStandardVerMng);
		if(S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
		{
			empj_BuildingInfo.setEscrowStandard(MyString.getInstance().parse(escrowStandardVerMng.getAmount()));
		}
		if(S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
		{
			empj_BuildingInfo.setEscrowStandard(MyString.getInstance().parse(escrowStandardVerMng.getPercentage()));
		}
		tgpj_BuildingAccount.setPaymentLines(paymentLines);
		*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		empj_BuildingInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_BuildingInfo.setUserUpdate(userLogin);
		empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		
		empj_BuildingInfoDao.save(empj_BuildingInfo);
		
		empj_BuildingExtendInfo.setBuildingInfo(empj_BuildingInfo);
		empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
		
		Tgpj_BuildingAccount tgpj_BuildingAccount = new Tgpj_BuildingAccount();
		tgpj_BuildingAccount.setTheState(S_TheState.Normal);
//		tgpj_BuildingAccount.setBusiState(busiState);
		tgpj_BuildingAccount.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgpj_BuildingAccount.setUserStart(userLogin);
		tgpj_BuildingAccount.setCreateTimeStamp(System.currentTimeMillis());
		tgpj_BuildingAccount.setUserUpdate(userLogin);
		tgpj_BuildingAccount.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BuildingAccount.setDevelopCompany(project.getDevelopCompany());
		if(project.getDevelopCompany() != null)
		{
			tgpj_BuildingAccount.seteCodeOfDevelopCompany(project.getDevelopCompany().geteCode());
		}
		
		tgpj_BuildingAccount.setProject(project);
		tgpj_BuildingAccount.setTheNameOfProject(project.getTheName());
		tgpj_BuildingAccount.setBuilding(empj_BuildingInfo);
		tgpj_BuildingAccount.seteCodeOfBuilding(empj_BuildingInfo.geteCode());
		tgpj_BuildingAccount.setEscrowStandard(empj_BuildingInfo.getEscrowStandard());
		tgpj_BuildingAccount.setEscrowArea(escrowArea);
		tgpj_BuildingAccount.setBuildingArea(buildingArea);
		tgpj_BuildingAccount.setOrgLimitedAmount(0.0);
		tgpj_BuildingAccount.setCurrentFigureProgress("0");
		tgpj_BuildingAccount.setCurrentLimitedRatio(100.0);
		tgpj_BuildingAccount.setNodeLimitedAmount(0.0);
		tgpj_BuildingAccount.setTotalGuaranteeAmount(0.0);
		tgpj_BuildingAccount.setCashLimitedAmount(0.0);
		tgpj_BuildingAccount.setEffectiveLimitedAmount(0.0);
		tgpj_BuildingAccount.setTotalAccountAmount(0.0);
		tgpj_BuildingAccount.setSpilloverAmount(0.0);
		tgpj_BuildingAccount.setPayoutAmount(0.0);
		tgpj_BuildingAccount.setAppliedNoPayoutAmount(0.0);
		tgpj_BuildingAccount.setApplyRefundPayoutAmount(0.0);
		tgpj_BuildingAccount.setRefundAmount(0.0);
		tgpj_BuildingAccount.setCurrentEscrowFund(0.0);
		tgpj_BuildingAccount.setAllocableAmount(0.0);
		tgpj_BuildingAccount.setRecordAvgPriceOfBuildingFromPresellSystem(0.0);
		tgpj_BuildingAccount.setRecordAvgPriceOfBuilding(0.0);
//		tgpj_BuildingAccount.setLogId(logId);
		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
		
		empj_BuildingInfo.setBuildingAccount(tgpj_BuildingAccount);
		empj_BuildingInfoDao.save(empj_BuildingInfo);
		
		sm_AttachmentBatchAddService.execute(model, empj_BuildingInfo.getTableId());
		
		//有配置审批流程需要走审批流
		if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			empj_BuildingInfo.setBusiState(S_BusiState.NoRecord);
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
			
			//审批操作
			sm_approvalProcessService.execute(empj_BuildingInfo, model, sm_approvalProcess_cfg);
		}
		else
		{
			//备案人，备案日期，备案状态，审批状态
			empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
			empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
			empj_BuildingInfo.setUserRecord(userLogin);
			empj_BuildingInfo.setRecordTimeStamp(System.currentTimeMillis());
			empj_BuildingInfoDao.save(empj_BuildingInfo);
		}
		
		//范围授权
//		Sm_Permission_RangeAuthorizationForm sm_Permission_RangeAuthorizationForm = new Sm_Permission_RangeAuthorizationForm();
//       
//		sm_Permission_RangeAuthorizationForm.setSponsorId(model.getUserId());
//        sm_Permission_RangeAuthorizationForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
//        
//        properties = s_P_RAAOUFBuildingService.execute(sm_Permission_RangeAuthorizationForm);
		
		properties.put("empj_BuildingInfo", empj_BuildingInfo);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
