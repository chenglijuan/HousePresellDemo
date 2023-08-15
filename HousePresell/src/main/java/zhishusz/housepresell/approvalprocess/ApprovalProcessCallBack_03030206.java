package zhishusz.housepresell.approvalprocess;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_ProjProgForcastPhoto;
import zhishusz.housepresell.service.CommonService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;

import java.text.DecimalFormat;
import java.util.Properties;

/**
 * 工程进度巡查推送： 审批过后-业务逻辑处理
 */
@Transactional
public class ApprovalProcessCallBack_03030206 implements IApprovalProcessCallback {
	@Autowired
	private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
	@Autowired
	private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
	@Autowired
	private Empj_ProjProgForcast_ManageDao empj_ProjProgForcast_ManageDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

	@Autowired
	private CommonService commonService;

	/**
	 * 202306 巡查数据审核完结以后去掉 推送公积金的功能
	 * @param approvalProcessWorkflow
	 * @param baseForm
	 * @return
	 */


	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm) {
		Properties properties = new MyProperties();

		try {
			String workflowEcode = approvalProcessWorkflow.geteCode();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = sm_ApprovalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的信息
			Long sourceId = sm_ApprovalProcess_AF.getSourceId();
			Empj_ProjProgForcast_Manage manage = empj_ProjProgForcast_ManageDao.findById(sourceId);
			if (manage == null) {
				return MyBackInfo.fail(properties, "审批的单据不存在");
			}

			switch (approvalProcessWork) {
			case "03030206001_ZS":
				if (S_ApprovalState.Completed.equals(sm_ApprovalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState())) {
					
					//更新网站
					projProgForcastApproval(manage);

					manage.setBusiState("已备案");
					manage.setApprovalState("已完结");
					manage.setRecordTimeStamp(System.currentTimeMillis());
					manage.setUserRecord(baseForm.getUser());
					
					manage.setWebHandelState("1");
					empj_ProjProgForcast_ManageDao.update(manage);

					// 主表信息
					Empj_ProjProgForcast_AF progForcast = manage.getAfEntity();

					// 如果审核的是项目信息，则直接更新备案状态
					if (null == manage.getDtlEntity()) {
						progForcast.setBusiState("已备案");
						progForcast.setApprovalState("已完结");
						progForcast.setRecordTimeStamp(System.currentTimeMillis());
						progForcast.setUserRecord(baseForm.getUser());
						progForcast.setWebHandelState("1");
						empj_ProjProgForcast_AFDao.update(progForcast);
					}

					CommonForm commonModel;
					Empj_ProjProgForcast_DTL dtl = manage.getDtlEntity();
					// 审核的是楼幢信息-更新楼幢相关信息
					if (null != dtl) {
						dtl.setBusiState("已备案");
						dtl.setApprovalState("已完结");
						dtl.setRecordTimeStamp(System.currentTimeMillis());
						// 已通过
						dtl.setWebHandelState("1");
						dtl.setUserRecord(baseForm.getUser());
						empj_ProjProgForcast_DTLDao.update(dtl);

						/*
						 * 更新预测时间
						 */
						commonModel = new CommonForm();
						commonModel.setForcastTime(progForcast.getForcastTime());
						commonModel.setBuildId(dtl.getBuildInfo().getTableId());
						commonModel.setNowBuildNum(
								Integer.valueOf(null == dtl.getBuildProgress() ? "0" : dtl.getBuildProgress()));
						commonModel.setBuildCountNum(
								Integer.parseInt(new DecimalFormat("0").format(dtl.getFloorUpNumber())));
						commonModel.setUser(baseForm.getUser());
						commonModel.setNowNode(dtl.getNowNode());

						commonModel.setBuildProgressType(
								StrUtil.isBlank(dtl.getBuildProgressType()) ? "" : dtl.getBuildProgressType());
						commonModel.setBuildProgress(
								StrUtil.isBlank(dtl.getBuildProgress()) ? "" : dtl.getBuildProgress());
						commonService.ProjProgForecastExecute(commonModel);

						//推送公积金   20230626   推送任务由3.0系统提供的接口统一获取 2.0推送功能可以关闭
//						CommonForm comm = new CommonForm();
//						comm.setTableId(dtl.getTableId());
//
//						commonService.PushApprovalInfo(comm);

					}
				}
				break;
				
			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
			}
		} catch (Exception e) {
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}
	
	public Properties projProgForcastApproval(Empj_ProjProgForcast_Manage manage) {

		/*
		 * { "cate":"ts_review", "ts_id":"ts_id  单据号", "ts_bld_id":"托管系统楼栋号",
		 * "actionType":"0/1  0:未审，1:已审" }
		 */
		Properties properties = new MyProperties();
		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0) {
			return MyBackInfo.fail(properties, "未查询到配置路径！");
		}

		To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();

		Empj_ProjProgForcast_DTL dtlEntity = manage.getDtlEntity();
		Empj_ProjProgForcast_AF afEntity = manage.getAfEntity();

		pushVo.setCate("ts_review");
		pushVo.setTs_pj_id(String.valueOf(afEntity.getProject().getTableId()));
		pushVo.setActionType("1");

		if (null != dtlEntity) {
			pushVo.setTs_bld_id(String.valueOf(dtlEntity.getBuildInfo().getTableId()));
			pushVo.setTs_id(String.valueOf(dtlEntity.getTableId()));
		} else {
			pushVo.setTs_id(String.valueOf(afEntity.getTableId()));
		}

		Gson gson = new Gson();
		String jsonMap = gson.toJson(pushVo);
		System.out.println(jsonMap);
		String decodeStr = Base64Encoder.encode(jsonMap);
		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();
		boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

		// 记录接口交互信息
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgStatus(1);
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_APPROVAL");
		tgpf_SocketMsg.setMsgContentArchives(jsonMap);
		if (interfaceUtil) {
			tgpf_SocketMsg.setReturnCode("200");
		} else {
			tgpf_SocketMsg.setReturnCode("300");
		}
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}

}
