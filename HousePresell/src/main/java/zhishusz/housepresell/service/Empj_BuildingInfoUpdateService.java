package zhishusz.housepresell.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_LandMortgageState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.toInterface.To_building;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.ToInterface;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.template.compare.Empj_BuildingInfoTemplate;

/*
 * Service更新操作：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfoUpdateService
{
	private static final String ADD_BUSI_CODE = "03010201";
	
	private static final String UPDATE_BUSI_CODE = "03010202";
	
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;
	
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	
	@Autowired
	private Sm_PoCompareResult sm_PoCompareResult;
	
	@Autowired
	private Sm_BusiState_LogAddService logAddService;
	
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User userLogin = (Sm_User)sm_UserDao.findById(model.getUserId());
		if(userLogin == null)
		{
			return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
		}
		
		Long tableId = model.getTableId();
//		String eCodeFromConstruction = model.geteCodeFromConstruction();//施工编号
		Double escrowArea = model.getEscrowArea();//托管面积
		Double upfloorNumber = model.getUpfloorNumber();//地上层数
		String landMortgagor = model.getLandMortgagor();//土地抵押权人
//		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();//公安编号
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*
		Long tgpj_EscrowStandardVerMngId = model.getTgpj_EscrowStandardVerMngId();//托管标准
		Double paymentLines = model.getPaymentLines();//支付保证上限比例		
		 */		
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		Double downfloorNumber = model.getDownfloorNumber();//地下层数
		Double landMortgageAmount = model.getLandMortgageAmount();//土地抵押金额
		Double buildingArea = model.getBuildingArea();//建筑面积
		String deliveryType = model.getDeliveryType();//交付类型
		Integer landMortgageState = model.getLandMortgageState();//土地抵押状态
		String remark = model.getRemark();
		String buttonType = model.getButtonType();//提交保存按钮
		
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要变更的楼幢");
		}
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
		else
		{
			landMortgagor = "";
			landMortgageAmount = 0.00;
		}
		if(deliveryType == null || deliveryType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择交付类型");
		}
		if(buttonType == null || buttonType.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择审批按钮");
		}
//		if(eCodeFromConstruction == null || eCodeFromConstruction.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "请输入施工编号");
//		}
//		if(eCodeFromPublicSecurity == null || eCodeFromPublicSecurity.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "请输入公安编号");
//		}
		
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*if(tgpj_EscrowStandardVerMngId == null || tgpj_EscrowStandardVerMngId < 1)
		{
			return MyBackInfo.fail(properties, "请选择托管标准");
		}
		
		Tgpj_EscrowStandardVerMng escrowStandardVerMng = tgpj_EscrowStandardVerMngDao.findById(tgpj_EscrowStandardVerMngId);
		if(escrowStandardVerMng == null)
		{
			return MyBackInfo.fail(properties, "选择的托管标准不存在");
		}
		
		if(MyDouble.getInstance().parse(paymentLines) == null)
		{
			return MyBackInfo.fail(properties, "请选择支付保证上限比例");
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
		
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findByIdWithClear(tableId);
		if(empj_BuildingInfo == null)
		{
			return MyBackInfo.fail(properties, "选择的楼幢不存在");
		}
		
		if(empj_BuildingInfo.getExtendInfo() == null)
		{
			return MyBackInfo.fail(properties, "选择的楼幢扩展信息不存在");
		}
		
		if(empj_BuildingInfo.getBuildingAccount() == null)
		{
			return MyBackInfo.fail(properties, "选择的楼幢的楼幢账户不存在");
		}
		
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
		}*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		empj_BuildingInfoDao.update(empj_BuildingInfo);
		
		Empj_BuildingExtendInfo empj_BuildingExtendInfo = empj_BuildingExtendInfoDao.findByIdWithClear(empj_BuildingInfo.getExtendInfo().getTableId());
		
		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao.findByIdWithClear(empj_BuildingInfo.getBuildingAccount().getTableId());
				
		if(S_EscrowState.ApprovalEscrowState.equals(empj_BuildingExtendInfo.getEscrowState()) ||
				S_EscrowState.EndEscrowState.equals(empj_BuildingExtendInfo.getEscrowState()))
		{
			return MyBackInfo.fail(properties, "申请托管终止、托管终止的的楼幢不能编辑");
		}
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*tgpj_BuildingAccount.setPaymentLines(paymentLines);*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		tgpj_BuildingAccountDao.update(tgpj_BuildingAccount);
		
		//修改对应的支付保证上限比例额度
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====start=====
		 */
		/*Double orgLimitedAmount = tgpj_BuildingAccount.getOrgLimitedAmount();//初始受限额度
		if(null != orgLimitedAmount)
		{
			Double a = orgLimitedAmount*paymentLines/100;
			tgpj_BuildingAccount.setPaymentProportion(a);
			tgpj_BuildingAccountDao.update(tgpj_BuildingAccount);
		}*/
		/*
		 * DEMAND#250 托管2.0需求变更
		 * =====end=====
		 */
		
		Empj_BuildingInfoTemplate empj_BuildingInfoTemplateOld = new Empj_BuildingInfoTemplate(empj_BuildingInfo, empj_BuildingExtendInfo);
		
		tgpj_BuildingAccount.setEscrowArea(escrowArea);
		tgpj_BuildingAccount.setBuildingArea(buildingArea);
		
		
		if(S_LandMortgageState.Yes.equals(landMortgageState))
		{
			empj_BuildingExtendInfo.setLandMortgagor(landMortgagor);
			empj_BuildingExtendInfo.setLandMortgageAmount(landMortgageAmount);
		}
		if(S_LandMortgageState.No.equals(landMortgageState))
		{
			empj_BuildingExtendInfo.setLandMortgagor(null);
			empj_BuildingExtendInfo.setLandMortgageAmount(null);
		}
		empj_BuildingExtendInfo.setUserUpdate(userLogin);
		empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		empj_BuildingExtendInfo.setLandMortgageState(landMortgageState);
		empj_BuildingExtendInfo.setBuildingInfo(empj_BuildingInfo);

		empj_BuildingInfo.setUserUpdate(userLogin);
		empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
//		empj_BuildingInfo.seteCodeFromConstruction(eCodeFromConstruction);
		empj_BuildingInfo.setEscrowArea(escrowArea);
		empj_BuildingInfo.setUpfloorNumber(upfloorNumber);
		
//		empj_BuildingInfo.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		
		empj_BuildingInfo.setDownfloorNumber(downfloorNumber);
		empj_BuildingInfo.setBuildingArea(buildingArea);
		empj_BuildingInfo.setDeliveryType(deliveryType);
		empj_BuildingInfo.setRemark(remark);
		
		Empj_BuildingInfoTemplate empj_BuildingInfoTemplateNew = new Empj_BuildingInfoTemplate(empj_BuildingInfo, empj_BuildingExtendInfo);
		
		Boolean flag = sm_PoCompareResult.execute(empj_BuildingInfoTemplateOld, empj_BuildingInfoTemplateNew);
		
		if (flag)
		{
			for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList())
			{
				//如果有form没有tableId，说明有新增
				if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
				{
					flag = false;//有新增不一样
					break;
				}
			}
			if (flag) //如果没有新增再看有没有删除
			{
				Integer totalCountNew = model.getGeneralAttachmentList().length;

				Sm_AttachmentForm theForm = new Sm_AttachmentForm();
				theForm.setTheState(S_TheState.Normal);
				theForm.setBusiType("030102");
				theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));

				Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));

				if (totalCountNew < totalCount)
				{
					flag = false;//有删除不一样
				}
			}
		}

		//先判断是否是未备案
		//如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
		if(S_BusiState.NoRecord.equals(empj_BuildingInfo.getBusiState()))
		{
			tgpj_BuildingAccountDao.update(tgpj_BuildingAccount);
			empj_BuildingInfoDao.update(empj_BuildingInfo);
			empj_BuildingExtendInfoDao.update(empj_BuildingExtendInfo);
			
			sm_AttachmentBatchAddService.execute(model, tableId);
			
			//如果是提交按钮则需要走新增的审批流
			if(S_ButtonType.Submit.equals(buttonType))
			{
				properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
				if("fail".equals(properties.getProperty(S_NormalFlag.result)))
				{
					return properties;
				}
				
				//没有配置审批流程无需走审批流直接保存数据库
				if(!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
				{
					//有相应的审批流程配置才走审批流程
					Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
					
					//审批操作
					sm_approvalProcessService.execute(empj_BuildingInfo, model, sm_approvalProcess_cfg);
				}
				else
				{
					empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
					empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
					empj_BuildingInfoDao.update(empj_BuildingInfo);
					
					// 推送数据到门户网站
					/*Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, empj_BuildingInfo.geteCode());*/

					/*Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, String.valueOf(empj_BuildingInfo.getTableId()));
				
					if (!interFaceAction)
					{
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
					}*/
				}
			}
		}
		else if(!flag)
		{
			properties = sm_ApprovalProcessGetService.execute(UPDATE_BUSI_CODE, model.getUserId());
			if("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}
			
			//没有配置审批流程无需走审批流直接保存数据库
			if("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
			{
				empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
				empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
				empj_BuildingInfo.setUserUpdate(userLogin);
				empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
				empj_BuildingInfo.setUserRecord(userLogin);
				empj_BuildingInfo.setRecordTimeStamp(System.currentTimeMillis());
				empj_BuildingInfoDao.update(empj_BuildingInfo);
				empj_BuildingExtendInfoDao.update(empj_BuildingExtendInfo);
				
				sm_AttachmentBatchAddService.execute(model, tableId);
				
				//日志，备案人，备案日期
				logAddService.addLog(model, tableId, empj_BuildingInfoTemplateOld, empj_BuildingInfoTemplateNew);
				
				// 推送数据到门户网站
				/*Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, empj_BuildingInfo.geteCode());*/
				/*Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, String.valueOf(empj_BuildingInfo.getTableId()));

				if (!interFaceAction)
				{
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
				}*/
			}
			else
			{
				//做一个还原操作
				try 
				{
					PropertyUtils.copyProperties(empj_BuildingInfo, empj_BuildingInfoTemplateOld);
					PropertyUtils.copyProperties(empj_BuildingExtendInfo, empj_BuildingInfoTemplateOld);
					PropertyUtils.copyProperties(tgpj_BuildingAccount, empj_BuildingInfoTemplateOld);
				} 
				catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) 
				{
					e.printStackTrace();
				}
				
				//有相应的审批流程配置才走审批流程
				Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
				
				//审批操作
				sm_approvalProcessService.execute(empj_BuildingInfo, model, sm_approvalProcess_cfg);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private Log log = LogFactory.getCurrentLogFactory().createLog(Empj_BuildingInfoUpdateService.class);
	
	/**
	 * 系统推送数据到门户网站
	 * 
	 * @param model
	 * @param eCode
	 * @param attachmentList
	 */
	public Boolean toInterFaceAction(Empj_BuildingInfo buildingInfo, String eCode)
	{
		
		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0)
		{
			log.equals("未查询到配置路径！");

			return false;
		}
				
		To_building building = new To_building();
		building.setAction("edit");
		building.setCate("bld");
		building.setPj_title(buildingInfo.getProject().getTheName());
//		building.setTs_pj_id(buildingInfo.getProject().geteCode());
		building.setTs_pj_id(String.valueOf(buildingInfo.getProject().getTableId()));
		building.setBld_hname(buildingInfo.geteCodeFromConstruction());
		building.setBld_hmane1(null == buildingInfo.geteCodeFromPublicSecurity()?" ":buildingInfo.geteCodeFromPublicSecurity());
		building.setTs_bld_id(eCode);
		building.setBld_mj(Double.toString(null == buildingInfo.getEscrowArea() ? 0.00 : buildingInfo.getEscrowArea()));
		building.setBld_lc(Double.toString(buildingInfo.getUpfloorNumber()));

		String deliveryType = buildingInfo.getDeliveryType();
		if (null != deliveryType && deliveryType.trim().equals("1"))
		{
			building.setBld_type("0");
		}
		else if (null != deliveryType && deliveryType.trim().equals("2"))
		{
			building.setBld_type("1");
		}
		
		String sql = "select * from Tgxy_EscrowAgreement where theState = 0 and businessProcessState ='7' and tableId=(select A.TGXY_ESCROWAGREEMENT from Rel_EscrowAgreement_Building A where A.EMPJ_BUILDINGINFO="
				+ buildingInfo.getTableId() + ")";

		Tgxy_EscrowAgreement tgxy_EscrowAgreement = sessionFactory.getCurrentSession()
				.createNativeQuery(sql, Tgxy_EscrowAgreement.class).uniqueResult();

		if (null != tgxy_EscrowAgreement)
		{
			building.setBld_tgtime(tgxy_EscrowAgreement.getContractApplicationDate());
		}
		else
		{
//			building.setBld_tgtime(null);
			building.setBld_tgtime("");
		}
		
		building.setBld_endtime("");
//		building.setBld_jfbatime("");

		Gson gson = new Gson();

		String jsonMap = gson.toJson(building);

		System.out.println(jsonMap);

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();

		return toFace.interfaceUtil(decodeStr,baseParameter0.getTheName());
	}
}