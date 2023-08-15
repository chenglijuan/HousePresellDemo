package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.bean.BeanUtil;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_BaseDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.ApproveUtil;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

@Service
@Transactional
public class Sm_ApprovalProcessService {

	@Autowired
	private Sm_ApprovalProcess_ApplyService sm_ApprovalProcess_applyService;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private OssServerUtil ossServerUtil;
	@Autowired
	private Sm_BaseDao sm_BaseDao;
	@Autowired
	private Sm_ApprovalProcess_MessagePushletService messagePushletService;
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;// 备案价格
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;// 用款申请
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 合作协议

	@SuppressWarnings({ "rawtypes" })
	public synchronized Properties execute(IApprovable iApprovable, BaseForm baseForm,
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg) {
		Properties properties = new MyProperties();

		Long sourceId = iApprovable.getSourceId();
		String poName = iApprovable.getSourceType();
		String busiCode = sm_approvalProcess_cfg.getBusiCode();
		String buttonType = baseForm.getButtonType();
		Long loginUserId = baseForm.getUserId();
		Sm_User loginUser = baseForm.getUser();

		// 可以变更的属性列表
		List<String> peddingApprovalKeyList = iApprovable.getPeddingApprovalkey();
		String orgObjJson = null;
		String expectObjJson = null;
		if (peddingApprovalKeyList != null && !peddingApprovalKeyList.isEmpty()) {
			ApproveUtil approveUtil = new ApproveUtil();
			orgObjJson = approveUtil.getJsonData(iApprovable, peddingApprovalKeyList);// 修改前的数据这里有个缺陷就是非本表的字段上传不到OSS
			expectObjJson = approveUtil.getJsonData(baseForm, peddingApprovalKeyList);// 修改后的数据
		}

		System.out.println("orgObjJson=" + orgObjJson);
		System.out.println("expectObjJson=" + expectObjJson);

		// 查找申请单
		Sm_ApprovalProcess_AFForm sm_approvalProcess_afForm = new Sm_ApprovalProcess_AFForm();
		sm_approvalProcess_afForm.setTheState(S_TheState.Normal);
		sm_approvalProcess_afForm.setBusiCode(busiCode);
		sm_approvalProcess_afForm.setSourceId(sourceId);
		sm_approvalProcess_afForm.setBusiState(S_ApprovalState.WaitSubmit);

		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(
				sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_approvalProcess_afForm));

		/***********************************保函标题新增楼栋*****************************************************************/
		System.out.println("标题新增楼栋："+ baseForm.getKeyword());
		System.out.println("busiCode="+ busiCode);
		/****************************************************************************************************************/



		if (sm_ApprovalProcess_AF != null && !"06120501".equals(busiCode)) {
			System.out.println("sm_ApprovalProcess_AF != null");
			String orgObjJsonFilePath = null;
			String expectObjJsonFilePath = null;
			// --------------------------------上传变数据到oss------------------------------------------//
			if (orgObjJson != null && orgObjJson.length() > 100) {
				ReceiveMessage uploadOrgObjJson = ossServerUtil.stringUpload(orgObjJson);// 上传可以变更的旧数据
				if (uploadOrgObjJson.getData() != null) {
					if (uploadOrgObjJson.getData().get(0).getUrl() != null) {
						orgObjJsonFilePath = uploadOrgObjJson.getData().get(0).getUrl();// 获取文件路径
					}
				}
			}
			
			System.out.println("orgObjJsonFilePath = " + orgObjJsonFilePath);
			if (expectObjJson != null && expectObjJson.length() > 100) {
				ReceiveMessage uploadExpectObjJson = ossServerUtil.stringUpload(expectObjJson);// 上传可以变更的新数据
				if (uploadExpectObjJson.getData() != null) {
					if (uploadExpectObjJson.getData().get(0).getUrl() != null) {
						expectObjJsonFilePath = uploadExpectObjJson.getData().get(0).getUrl();// 获取文件路径
					}
				}
			}
			System.out.println("expectObjJsonFilePath = " + expectObjJsonFilePath);
			// --------------------------------上传变数据到oss------------------------------------------//

			Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_ApprovalProcess_AF.getWorkFlowList().get(0);
			if (buttonType.equals(S_ButtonType.Submit)) {
				sm_ApprovalProcess_AF.setBusiState(S_ApprovalState.Examining);
				if (sm_ApprovalProcess_AF.getWorkFlowList() != null) {
					sm_ApprovalProcess_AF.getWorkFlowList().get(0).setBusiState(S_WorkflowBusiState.Pass);
					if (sm_ApprovalProcess_AF.getWorkFlowList().size() > 1) {
						sm_ApprovalProcess_AF.getWorkFlowList().get(1).setBusiState(S_WorkflowBusiState.Examining);
					}
				}
				
				System.out.println("buttonType.equals(S_ButtonType.Submit)");
				// --------------------------------消息模板----------------------------------//
				sm_approvalProcess_workflow.setLastAction(3);
				messagePushletService.execute(sm_approvalProcess_workflow, loginUser);
				System.out.println("messagePushletService.execute");
				// --------------------------------消息模板----------------------------------//
			}

			// --------------------------------------------------------生成发起记录start-----------------------------------//
			List recordList = new ArrayList();
			Sm_ApprovalProcess_Record sm_ApprovalProcess_Record = new Sm_ApprovalProcess_Record();
			sm_ApprovalProcess_Record.setTheState(S_TheState.Normal);
			sm_ApprovalProcess_Record.setConfiguration(sm_approvalProcess_cfg); // 流程配置
			sm_ApprovalProcess_Record.setCreateTimeStamp(System.currentTimeMillis());
			sm_ApprovalProcess_Record.setLastUpdateTimeStamp(System.currentTimeMillis());
			sm_ApprovalProcess_Record.setUserStart(loginUser);
			sm_ApprovalProcess_Record.setUserUpdate(loginUser);
			sm_ApprovalProcess_Record.setUserOperate(loginUser); // 发起人
			sm_ApprovalProcess_Record.setOperateTimeStamp(System.currentTimeMillis()); // 操作时间点

			if (sm_approvalProcess_workflow.getApprovalProcess_recordList() != null
					&& sm_approvalProcess_workflow.getApprovalProcess_recordList().size() > 0) {
				sm_approvalProcess_workflow.getApprovalProcess_recordList().add(sm_ApprovalProcess_Record);
			} else {
				sm_approvalProcess_workflow.setApprovalProcess_recordList(recordList);
				recordList.add(sm_ApprovalProcess_Record);
			}
			
			System.out.println("生成发起记录end");
			// --------------------------------------------------------生成发起记录end-------------------------------------//

			/**
			 * xsz by time 2019-5-21 10:45:28 根据业务重新生成主题
			 */
			if ("03010301".equals(busiCode)) {
				// 物价备案均价
				Tgpj_BuildingAvgPrice buildingAvgPrice = tgpj_BuildingAvgPriceDao.findById(sourceId);
				if (null != buildingAvgPrice) {
					sm_ApprovalProcess_AF
							.setTheme(buildingAvgPrice.getBuildingInfo().getProject().getCityRegion().getTheName()
									+ "项目名称：" + buildingAvgPrice.getBuildingInfo().getProject().getTheName() + " 施工编号："
									+ buildingAvgPrice.getBuildingInfo().geteCodeFromConstruction() + " 备案价格均价："
									+ buildingAvgPrice.getRecordAveragePrice() + "元");// 项目名称：XXX
																						// 施工编号：XXX幢
																						// 备案价格均价:XXX元
				}
			} else if ("06120301".equals(busiCode)) {
				// 用款申请
				Tgpf_FundAppropriated_AF fundAppropriated_AF = tgpf_FundAppropriated_AFDao.findById(sourceId);
				if (null != fundAppropriated_AF && null != fundAppropriated_AF.getProject()) {
					sm_ApprovalProcess_AF.setTheme("用款申请与复核：" + fundAppropriated_AF.getProject().getTheName() + " 金额："
							+ MyDouble.pointTOThousandths(fundAppropriated_AF.getTotalApplyAmount()) + "元");// 用款申请与复核：xx项目
																											// 金额：xx元
				}
			} else if ("06110201".equals(busiCode)) {
				// 合作协议签署
				Tgxy_EscrowAgreement escrowAgreement = tgxy_EscrowAgreementDao.findById(sourceId);
				if (null != escrowAgreement) {
					sm_ApprovalProcess_AF.setTheme(escrowAgreement.getProject().getCityRegion().getTheName() + " 项目名称："
							+ escrowAgreement.getProject().getTheName() + " 施工编号："
							+ escrowAgreement.getBuildingInfoCodeList());// 项目名称：XXX
																			// 施工编号：XXX幢
				}
			}

			sm_ApprovalProcess_AF.setOrgObjJsonFilePath(orgObjJsonFilePath);
			sm_ApprovalProcess_AF.setExpectObjJsonFilePath(expectObjJsonFilePath);
			sm_ApprovalProcess_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
			sm_ApprovalProcess_AF.setStartTimeStamp(System.currentTimeMillis());
			sm_ApprovalProcess_AF.setUserStart(loginUser);
			sm_ApprovalProcess_AF.setApplicant(loginUser.getTheName());
			sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF);
			
			System.out.println("sm_ApprovalProcess_AFDao.save(sm_ApprovalProcess_AF)");
		} else {
			sm_approvalProcess_cfg.setKeyword(baseForm.getKeyword());
			Properties applyProperties = sm_ApprovalProcess_applyService.execute(iApprovable, orgObjJson, expectObjJson,
					loginUserId, buttonType, sm_approvalProcess_cfg);
			
			System.out.println("applyProperties = " + applyProperties.toString());
			if (applyProperties.getProperty("result").equals("fail")) {
				return applyProperties;
			}
		}
		
		System.out.println("applyProperties end");

		// 修改审批状态
		if (baseForm.getIsSetApprovalState()) {
			setApprovalState(buttonType, poName, sourceId);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/*
	 * @SuppressWarnings("rawtypes") public void setApprovalState(Object
	 * iApprovable, Class expectObjClass, String keyName, String buttonType) {
	 * try { BeanInfo beanInfo = Introspector.getBeanInfo(expectObjClass);
	 * PropertyDescriptor[] propertyDescriptors =
	 * beanInfo.getPropertyDescriptors();
	 * 
	 * for (PropertyDescriptor property : propertyDescriptors) { String key =
	 * property.getName(); //System.out.println(key);
	 * 
	 * // 过滤class属性 if (!key.equals("class")) { Method setter =
	 * property.getWriteMethod();
	 * 
	 * //仅在本项目中用，解决，参数命名问题导致的JavaBean获取属性类型错误 // 形如：参数gPS =>
	 * 从javaBean中解析后，获取到的属性值为：GPS // JavaBean 命名规范：属性的名称的连续两个字母：建议都小写！！！
	 * //System.out.println(key.substring(1, key.length())); if(key.length() ==
	 * 1) { key = key.substring(0, 1).toLowerCase();//将属性值的首字母小写 } else
	 * if(key.length() > 1) { key = key.substring(0,
	 * 1).toLowerCase()+key.substring(1, key.length());//将属性值的首字母小写 }
	 * //System.out.println(key);
	 * 
	 * //该参数不在Map中，需要放进去 if(keyName.equals(key)) {
	 * if(buttonType.equals(S_ButtonType.Save)) { setter.invoke(iApprovable,
	 * S_ApprovalState.WaitSubmit); } else
	 * if(buttonType.equals(S_ButtonType.Submit)) { setter.invoke(iApprovable,
	 * S_ApprovalState.Examining); }
	 * 
	 * sm_BaseDao.update(iApprovable); } } } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

	public void setApprovalState(String buttonType, String poName, Long sourceId) {
		Object queryObject = null;
		Class expectObjClass = null;
		try {

			expectObjClass = Class.forName(poName);
			queryObject = sm_BaseDao.findById(expectObjClass, sourceId);

			Map<String, Object> queryMap = BeanUtil.beanToMap(queryObject);
			if (S_ButtonType.Save.equals(buttonType)) {
				queryMap.put("approvalState", S_ApprovalState.WaitSubmit);
			} else if (S_ButtonType.Submit.equals(buttonType)) {
				queryMap.put("approvalState", S_ApprovalState.Examining);
			}
			Object oldObject = (Object) BeanUtil.mapToBean(queryMap, expectObjClass, true);
			BeanCopier beanCopier = BeanCopier.create(expectObjClass, expectObjClass, false);
			beanCopier.copy(oldObject, queryObject, null);
			sm_BaseDao.save(queryObject);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
