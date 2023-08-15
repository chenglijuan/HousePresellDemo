package zhishusz.housepresell.approvalprocess;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_building;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;

/**
 * 楼幢新增：
 * 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03010201 implements IApprovalProcessCallback
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的楼幢
			Long empj_BuildingInfoId = sm_ApprovalProcess_AF.getSourceId();
			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(empj_BuildingInfoId);

			if (empj_BuildingInfo == null)
			{
				return MyBackInfo.fail(properties, "审批的楼幢不存在");
			}

			switch (approvalProcessWork)
			{
			case "03010201001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					String jsonStr = sm_ApprovalProcess_AF.getExpectObjJson();
					if (jsonStr != null && jsonStr.length() > 0)
					{
						empj_BuildingInfo.setBusiState(S_BusiState.HaveRecord);
						empj_BuildingInfo.setApprovalState(S_ApprovalState.Completed);
						empj_BuildingInfo.setUserUpdate(approvalProcessWorkflow.getUserUpdate());// 操作人
						empj_BuildingInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
						empj_BuildingInfo.setUserRecord(approvalProcessWorkflow.getUserUpdate());// 备案人
						empj_BuildingInfo.setRecordTimeStamp(System.currentTimeMillis());

						empj_BuildingInfoDao.save(empj_BuildingInfo);
						
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

	private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_03010201.class);
	
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
		building.setAction("add");
		building.setCate("bld");
		building.setPj_title(buildingInfo.getProject().getTheName());
//		building.setTs_pj_id(buildingInfo.getProject().geteCode());
		building.setTs_pj_id(String.valueOf(buildingInfo.getProject().getTableId()));
		building.setBld_hname(buildingInfo.geteCodeFromConstruction());
		building.setBld_hmane1(null == buildingInfo.geteCodeFromPublicSecurity()?" ":buildingInfo.geteCodeFromPublicSecurity());
		building.setTs_bld_id(eCode);
		building.setBld_mj(Double.toString(null == buildingInfo.getEscrowArea() ? 0.00 : buildingInfo.getEscrowArea()));
		building.setBld_lc(Double.toString(buildingInfo.getUpfloorNumber()));
		
		building.setBld_tgtime("");
		building.setBld_endtime("");
//		building.setBld_jfbatime("");

		String deliveryType = buildingInfo.getDeliveryType();
		if (null != deliveryType && deliveryType.trim().equals("1"))
		{
			building.setBld_type("0");
		}
		else if (null != deliveryType && deliveryType.trim().equals("2"))
		{
			building.setBld_type("1");
		}

		Gson gson = new Gson();

		String jsonMap = gson.toJson(building);

		System.out.println(jsonMap);

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();

		return toFace.interfaceUtil(decodeStr,baseParameter0.getTheName());
	}
}
