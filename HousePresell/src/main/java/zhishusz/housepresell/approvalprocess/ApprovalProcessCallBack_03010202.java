package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_LandMortgageState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_building;
import zhishusz.housepresell.service.Sm_AttachmentBatchAddService;
import zhishusz.housepresell.service.Sm_BusiState_LogAddService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.ToInterface;

/**
 * 楼幢编辑：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03010202 implements IApprovalProcessCallback
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;

	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	@Autowired
	private Gson gson;

	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	@Autowired
	private Sm_BusiState_LogAddService logAddService;

	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();// 节点编码

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;// 流程编码加节点编码

			// 获取正在审批的楼幢
			Long empj_BuildingInfoId = sm_ApprovalProcess_AF.getSourceId();
			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(empj_BuildingInfoId);

			if (empj_BuildingInfo == null)
			{
				return MyBackInfo.fail(properties, "审批的楼幢不存在");
			}

			Empj_BuildingInfo empj_BuildingInfoOld = ObjectCopier.copy(empj_BuildingInfo);

			switch (approvalProcessWork)
			{
			case "03010202001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{

					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{
						Empj_BuildingInfoForm empj_BuildingInfoForm = gson.fromJson(jsonStr,
								Empj_BuildingInfoForm.class);

						empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
						empj_BuildingInfo.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
						empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
						empj_BuildingInfo.setEscrowArea(empj_BuildingInfoForm.getEscrowArea());
						empj_BuildingInfo.setBuildingArea(empj_BuildingInfoForm.getBuildingArea());
						empj_BuildingInfo.setUpfloorNumber(empj_BuildingInfoForm.getUpfloorNumber());
						empj_BuildingInfo.setDownfloorNumber(empj_BuildingInfoForm.getDownfloorNumber());
						empj_BuildingInfo.setDeliveryType(empj_BuildingInfoForm.getDeliveryType());

						Empj_BuildingExtendInfo empj_BuildingExtendInfo = empj_BuildingInfo.getExtendInfo();
						if (empj_BuildingExtendInfo == null)
						{
							empj_BuildingExtendInfo = new Empj_BuildingExtendInfo();
						}
						if (S_LandMortgageState.Yes.equals(empj_BuildingInfoForm.getLandMortgageState()))
						{
							empj_BuildingExtendInfo.setLandMortgagor(empj_BuildingInfoForm.getLandMortgagor());
							empj_BuildingExtendInfo
									.setLandMortgageAmount(empj_BuildingInfoForm.getLandMortgageAmount());
						}
						if (S_LandMortgageState.No.equals(empj_BuildingInfoForm.getLandMortgageState()))
						{
							empj_BuildingExtendInfo.setLandMortgagor(null);
							empj_BuildingExtendInfo.setLandMortgageAmount(null);
						}
						empj_BuildingExtendInfo.setLandMortgageState(empj_BuildingInfoForm.getLandMortgageState());
						empj_BuildingExtendInfo.setUserStart(sm_ApprovalProcess_AF.getUserStart());
						empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());

						empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
						empj_BuildingInfo.setExtendInfo(empj_BuildingExtendInfo);
						empj_BuildingExtendInfo.setBuildingInfo(empj_BuildingInfo);

						// 备案人备案日期
						empj_BuildingInfo.setUserRecord(baseForm.getUser());
						empj_BuildingInfo.setRecordTimeStamp(System.currentTimeMillis());

						Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();

						tgpj_BuildingAccount.setEscrowArea(empj_BuildingInfoForm.getEscrowArea());
						tgpj_BuildingAccount.setBuildingArea(empj_BuildingInfoForm.getBuildingArea());

						tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
						empj_BuildingInfoDao.save(empj_BuildingInfo);
						empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);

						empj_BuildingInfoForm.setBusiType("03010201");// 附件材料配置

						empj_BuildingInfoForm.setUserId(sm_ApprovalProcess_AF.getUserStart().getTableId());

						sm_AttachmentBatchAddService.execute(empj_BuildingInfoForm, empj_BuildingInfoId);

						Empj_BuildingInfo empj_BuildingInfoNew = ObjectCopier.copy(empj_BuildingInfo);
						if (baseForm != null)
						{
							baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
							logAddService.addLog(baseForm, empj_BuildingInfoId, empj_BuildingInfoOld,
									empj_BuildingInfoNew);
						}
					}
				}
				break;

			case "03010202002_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{

					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{
						Empj_BuildingInfoForm empj_BuildingInfoForm = gson.fromJson(jsonStr,
								Empj_BuildingInfoForm.class);

						empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
						empj_BuildingInfo.setUserUpdate(sm_ApprovalProcess_AF.getUserStart());
						empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
						empj_BuildingInfo.setEscrowArea(empj_BuildingInfoForm.getEscrowArea());
						empj_BuildingInfo.setBuildingArea(empj_BuildingInfoForm.getBuildingArea());
						empj_BuildingInfo.setUpfloorNumber(empj_BuildingInfoForm.getUpfloorNumber());
						empj_BuildingInfo.setDownfloorNumber(empj_BuildingInfoForm.getDownfloorNumber());
						empj_BuildingInfo.setDeliveryType(empj_BuildingInfoForm.getDeliveryType());

						Empj_BuildingExtendInfo empj_BuildingExtendInfo = empj_BuildingInfo.getExtendInfo();
						if (empj_BuildingExtendInfo == null)
						{
							empj_BuildingExtendInfo = new Empj_BuildingExtendInfo();
						}
						if (S_LandMortgageState.Yes.equals(empj_BuildingInfoForm.getLandMortgageState()))
						{
							empj_BuildingExtendInfo.setLandMortgagor(empj_BuildingInfoForm.getLandMortgagor());
							empj_BuildingExtendInfo
									.setLandMortgageAmount(empj_BuildingInfoForm.getLandMortgageAmount());
						}
						if (S_LandMortgageState.No.equals(empj_BuildingInfoForm.getLandMortgageState()))
						{
							empj_BuildingExtendInfo.setLandMortgagor(null);
							empj_BuildingExtendInfo.setLandMortgageAmount(null);
						}
						empj_BuildingExtendInfo.setLandMortgageState(empj_BuildingInfoForm.getLandMortgageState());
						empj_BuildingExtendInfo.setUserStart(sm_ApprovalProcess_AF.getUserStart());
						empj_BuildingExtendInfo.setLastUpdateTimeStamp(System.currentTimeMillis());

						empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
						empj_BuildingInfo.setExtendInfo(empj_BuildingExtendInfo);
						empj_BuildingExtendInfo.setBuildingInfo(empj_BuildingInfo);

						// 备案人备案日期
						empj_BuildingInfo.setUserRecord(baseForm.getUser());
						empj_BuildingInfo.setRecordTimeStamp(System.currentTimeMillis());

						Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();

						tgpj_BuildingAccount.setEscrowArea(empj_BuildingInfoForm.getEscrowArea());
						tgpj_BuildingAccount.setBuildingArea(empj_BuildingInfoForm.getBuildingArea());

						tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
						empj_BuildingInfoDao.save(empj_BuildingInfo);
						empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);

						empj_BuildingInfoForm.setBusiType("03010201");// 附件材料配置

						empj_BuildingInfoForm.setUserId(sm_ApprovalProcess_AF.getUserStart().getTableId());

						sm_AttachmentBatchAddService.execute(empj_BuildingInfoForm, empj_BuildingInfoId);

						Empj_BuildingInfo empj_BuildingInfoNew = ObjectCopier.copy(empj_BuildingInfo);
						if (baseForm != null)
						{
							baseForm.setUser(sm_ApprovalProcess_AF.getUserStart());
							logAddService.addLog(baseForm, empj_BuildingInfoId, empj_BuildingInfoOld,
									empj_BuildingInfoNew);
						}

						// 推送数据到门户网站
//						Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, empj_BuildingInfo.geteCode());
						/*Boolean interFaceAction = toInterFaceAction(empj_BuildingInfo, String.valueOf(empj_BuildingInfo.getTableId()));

						if (!interFaceAction)
						{
							properties.put(S_NormalFlag.result, S_NormalFlag.fail);
							properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
						}*/
					}
				}
				break;
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
				;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);

		}

		return properties;
	}

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_03010202.class);
	
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
