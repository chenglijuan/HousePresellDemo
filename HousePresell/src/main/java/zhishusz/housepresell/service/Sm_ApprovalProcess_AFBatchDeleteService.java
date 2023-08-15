package zhishusz.housepresell.service;


import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgpf_FundOverallPlanForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_BaseDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.core.bean.BeanUtil;

/*
 * Service批量删除：审批流-申请单
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_AFBatchDeleteService {
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_BaseDao sm_BaseDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;
	@Autowired
	private Tgpf_FundAppropriated_AFDeleteService fundAppropriatedAfDeleteService; //用款申请
	@Autowired
	private Tgpf_FundOverallPlanDeleteService fundOverallPlanDeleteService;//资金统筹
	@Autowired
	private Tgpf_FundAppropriatedDeleteService fundAppropriatedDeleteService;//资金拨付
	@Autowired
	private Emmp_CompanyInfoDeleteService companyInfoDeleteService;//开发企业
	@Autowired
	private Emmp_CompanyAgencyDeleteService companyAgencyDeleteService;//代理机构
	@Autowired
	private Emmp_CompanyCooperationDeleteService companyCooperationDeleteService;//合作机构
	@Autowired
	private Emmp_CompanyWitnessDeleteService companyWitnessDeleteService;//进度见证服务单位
	@Autowired
	private Empj_ProjectInfoDeleteService projectInfoDeleteService; // 项目信息注册与备案
	@Autowired
	private Empj_BuildingInfoDeleteService buildingInfoDeleteService; // 楼幢信息初始维护
	@Autowired
	private Tgpj_EscrowStandardVerMngDeleteService escrowStandardVerMngDeleteService; //托管标准版本管理
	@Autowired
	private Tgpj_BuildingAvgPriceDeleteService buildingAvgPriceDeleteService;
	@Autowired
	private Empj_BldLimitAmount_AFDeleteService bldLimitAmountAfDeleteService;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDeleteService bldLimitAmountVer_afDeleteService;
	@Autowired
	private Tgxy_TripleAgreementBatchDeleteService tgxy_TripleAgreementBatchDeleteService;//三方协议删除
	@Autowired
	private Tgxy_EscrowAgreementBatchDeleteService tgxy_EscrowAgreementBatchDeleteService;//合作协议删除
	@Autowired
	private Tgpf_RefundInfoBatchDeleteService tgpf_RefundInfoBatchDeleteService;//退房退款删除

	@Autowired
	private Empj_PaymentGuaranteeApplyBatchDeleteService empj_PaymentGuaranteeApplyBatchDeleteService;//支付保证删除
	@Autowired
	private Empj_PaymentGuaranteeDeleteService empj_PaymentGuaranteeDeleteService;//支付保证撤销删除
	@Autowired
	private Tgxy_CoopAgreementSettleBatchDeleteService tgxy_CoopAgreementSettleBatchDeleteService;//三方协议计量结算删除

	@Autowired
	private Empj_BldEscrowCompletedDeleteService empj_bldEscrowCompletedDeleteService;//托管终止
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFBatchDeleteService tgpf_SpecialFundAppropriated_AFBatchDeleteService;//特殊拨付
	



	public Properties execute(Sm_ApprovalProcess_AFForm model) {
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1) {
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}
		//TODO 根据业务编码调用各自业务的删除Service
		for (Long sm_ApprovalProcess_AFId : idArr)
		{
			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = (Sm_ApprovalProcess_AF) sm_ApprovalProcess_AFDao.findById(sm_ApprovalProcess_AFId);
			if (sm_ApprovalProcess_AF == null)
			{
				return MyBackInfo.fail(properties, "'审批流申请单(Id:" + sm_ApprovalProcess_AFId + ")'不存在");
			}
			Long sourceId = sm_ApprovalProcess_AF.getSourceId(); // 业务Id
			String busiCode = sm_ApprovalProcess_AF.getBusiCode();//业务编码
			String PoName = sm_ApprovalProcess_AF.getSourceType();//PoName
			Object queryObject = null;
			Class expectObjClass = null;
			try {
				expectObjClass =Class.forName(PoName);

				queryObject = sm_BaseDao.findById(expectObjClass, sourceId);
				if(queryObject == null)
				{
					return MyBackInfo.fail(properties, "'业务审批对象'不存在");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Map<String, Object> queryMap = BeanUtil.beanToMap(queryObject);
			//已备案的删除，修改审批状态为“已完结”
			if(queryMap.get("busiState")!=null && queryMap.get("busiState").equals(S_BusiState.HaveRecord))
			{
				queryMap.put("approvalState", S_ApprovalState.Completed);
				Object oldObject = (Object) BeanUtil.mapToBean(queryMap,expectObjClass,true);
				BeanCopier beanCopier = BeanCopier.create(expectObjClass, expectObjClass, false);
				beanCopier.copy(oldObject, queryObject, null);
				sm_BaseDao.save(queryObject);
				//删除审批流
				deleteService.execute(sourceId,busiCode);
			}
			else
			{
				switch (busiCode) {
					case "220106"://楼幢备案价格（报表）220106
						break;
					case "220104"://房屋状态预警220104
						break;
					case "220103"://楼幢信息预警220103
						break;
					case "220102"://项目名称预警220102
						break;
					case "220101"://开发企业信息预警220101
						break;
					case "21020305"://项目风险档案21020305
						break;
					case "21020304"://风险提示函21020304
						break;
					case "21020303"://项目风险日志21020303
						break;
					case "21020302"://风险等级评判21020302
						break;
					case "21020301"://项目风险评估21020301
						break;
					case "21020204"://其他风险分析21020204
						break;
					case "21020203"://预售许可指标21020203
						break;
					case "21020202"://退房退款风险指标21020202
						break;
					case "21020201"://形象进度风险分析21020201
						break;
					case "21020104"://月度风控小结21020104
						break;
					case "21020103"://风控例行结果处理21020103
						break;
					case "21020101"://工作时限检查21020101
						break;
					case "06120603"://特殊资金拨付06120603
						break;
					case "06120602"://其他支付申请与复核06120602
						break;
					case "06120502"://支付保函拨付06120502
						break;
					case "06120501"://支付保函申请与复核06120501
						break;
					case "06120401"://支付保证申请与复核06120401
						Empj_PaymentGuaranteeForm empj_PaymentGuaranteeForm = new Empj_PaymentGuaranteeForm();
						Long[] empj_PaymentGuaranteeIdArr = new Long[1];
						empj_PaymentGuaranteeIdArr[0] = sourceId;
						empj_PaymentGuaranteeForm.setIdArr(empj_PaymentGuaranteeIdArr);
						empj_PaymentGuaranteeForm.setBusiCode(busiCode);
						empj_PaymentGuaranteeApplyBatchDeleteService.execute(empj_PaymentGuaranteeForm);	
						break;
					case "06120303"://资金拨付06120303
						Tgpf_FundAppropriatedForm fundAppropriatedForm = new Tgpf_FundAppropriatedForm();
						fundAppropriatedForm.setTableId(sourceId);
						fundAppropriatedForm.setBusiCode(busiCode);
						fundAppropriatedDeleteService.execute(fundAppropriatedForm);
						break;
					case "06120302"://统筹与复核06120302
						Tgpf_FundOverallPlanForm fundOverallPlanForm = new Tgpf_FundOverallPlanForm();
						fundOverallPlanForm.setTableId(sourceId);
						fundOverallPlanForm.setBusiCode(busiCode);
						fundOverallPlanDeleteService.execute(fundOverallPlanForm);
						break;
					case "06120301"://用款申请与复核06120301
						Tgpf_FundAppropriated_AFForm fundAppropriated_afForm = new Tgpf_FundAppropriated_AFForm();
						fundAppropriated_afForm.setTableId(sourceId);
						fundAppropriated_afForm.setBusiCode(busiCode);
						fundAppropriatedAfDeleteService.execute(fundAppropriated_afForm);
						break;
					case "06120201"://退房退款申请-贷款已结清06120201
							Tgpf_RefundInfoForm tgpf_refundinfoForm = new Tgpf_RefundInfoForm();
							Long[] tgpf_refundinfoIdArr = new Long[1];
							tgpf_refundinfoIdArr[0] = sourceId;
							tgpf_refundinfoForm.setIdArr(tgpf_refundinfoIdArr);
							tgpf_refundinfoForm.setBusiCode(busiCode);
							tgpf_RefundInfoBatchDeleteService.execute(tgpf_refundinfoForm);
						break;
					case "06120102"://全额托管资金归集06120102
						break;
					case "06110304"://三方协议计量结算06110304
						Tgxy_CoopAgreementSettleForm tgxy_CoopAgreementSettleForm = new Tgxy_CoopAgreementSettleForm();
						Long[] tgxy_CoopAgreementSettleFormIdArr = new Long[1];
						tgxy_CoopAgreementSettleFormIdArr[0] = sourceId;
						tgxy_CoopAgreementSettleForm.setIdArr(tgxy_CoopAgreementSettleFormIdArr);
						tgxy_CoopAgreementSettleForm.setBusiCode(busiCode);
						tgxy_CoopAgreementSettleBatchDeleteService.execute(tgxy_CoopAgreementSettleForm);					
						break;
					case "06110303"://三方协议考评（查询报表）06110303
						break;
					case "06110301"://贷款三方托管协议签署06110301
						Tgxy_TripleAgreementForm tripleAgreementModel = new Tgxy_TripleAgreementForm();
						Long[] tripleAgreementIdArr = new Long[1];
						tripleAgreementIdArr[0] = sourceId;
						tripleAgreementModel.setIdArr(tripleAgreementIdArr);
						tripleAgreementModel.setBusiCode(busiCode);
						tripleAgreementModel.setUser(model.getUser());
						tripleAgreementModel.setUserId(model.getUserId());
						tgxy_TripleAgreementBatchDeleteService.execute(tripleAgreementModel);
						break;
					case "06110203"://全额托管合作协议签署06110203
						break;
					case "06110201"://贷款托管合作协议签署06110201
						Tgxy_EscrowAgreementForm tgxy_EscrowAgreementModel = new Tgxy_EscrowAgreementForm();
						Long[] escrowAgreementIdArr = new Long[1];
						escrowAgreementIdArr[0] = sourceId;
						tgxy_EscrowAgreementModel.setIdArr(escrowAgreementIdArr);
						tgxy_EscrowAgreementModel.setBusiCode(busiCode);
						tgxy_EscrowAgreementModel.setUser(model.getUser());
						tgxy_EscrowAgreementModel.setUserId(model.getUserId());
						tgxy_EscrowAgreementBatchDeleteService.execute(tgxy_EscrowAgreementModel);
						break;
					case "06110104"://支付保证协议06110104
						break;
					case "06110102"://合作备忘录06110102
						break;
					case "06110101"://协定存款协议06110101
						break;
					case "06010103"://三方协议版本管理06010103
						break;
					case "06010101"://托管标准版本管理06010101
						Tgpj_EscrowStandardVerMngForm tgpj_escrowStandardVerMngForm =
								new Tgpj_EscrowStandardVerMngForm();
						tgpj_escrowStandardVerMngForm.setTableId(sourceId);
						tgpj_escrowStandardVerMngForm.setBusiCode(busiCode);
						escrowStandardVerMngDeleteService.execute(tgpj_escrowStandardVerMngForm);
						break;
					case "03030201"://进度巡查预测03030201
						break;
					case "03030101"://受限额度变更03030101
                        Empj_BldLimitAmount_AFForm bldLimitAmount_afAddForm = new Empj_BldLimitAmount_AFForm();
                        bldLimitAmount_afAddForm.setTableId(sourceId);
                        bldLimitAmount_afAddForm.setBusiCode(busiCode);
                        bldLimitAmountAfDeleteService.execute(bldLimitAmount_afAddForm);
                        break;
					case "03020102"://楼盘表应用03020102
						break;
					case "03020101"://楼盘表显示03020101
						break;
					case "03010302"://备案价格变更维护03010302
						break;
					case "03010301"://备案价格初始维护03010301
						Tgpj_BuildingAvgPriceForm buildingAvgPriceAddForm = new Tgpj_BuildingAvgPriceForm();
						buildingAvgPriceAddForm.setTableId(sourceId);
						buildingAvgPriceAddForm.setBusiCode(busiCode);
						buildingAvgPriceDeleteService.execute(buildingAvgPriceAddForm);
						break;
					case "03010203"://楼幢监管账号维护03010203
						break;
					case "03010202"://楼幢信息变更维护03010202
						break;
					case "03010102"://项目信息变更与备案03010102
						break;
					case "210203"://项目风险管理210203
						break;
					case "210202"://项目风控指标210202
						break;
					case "210105"://存单提取管理210105
						break;
					case "210104"://存单存入管理210104
						break;
					case "210102"://支出预测210102
						break;
					case "210101"://收入预测210101
						break;
					case "200403"://托管与财务系统配置对照表200403
						break;
					case "200401"://入账凭证推送200401
						break;
					case "200302"://存留权益计算200302
						break;
					case "200204"://日终结算与生成凭证200204
						break;
					case "200203"://网银对账200203
						break;
					case "200202"://业务对账200202
						break;
					case "200102"://监管账号维护200102
						break;
					case "200101"://托管账户200101
						break;
					case "061206"://托管特殊拨付管理061206
						
						Tgpf_SpecialFundAppropriated_AFForm specialFundAppropriated_AFModel = new Tgpf_SpecialFundAppropriated_AFForm();
						
						Long[] specialFundAppropriated_AFIdArr = new Long[1];
						specialFundAppropriated_AFIdArr[0] = sourceId;
						specialFundAppropriated_AFModel.setIdArr(specialFundAppropriated_AFIdArr);
						specialFundAppropriated_AFModel.setBusiCode(busiCode);
						tgpf_SpecialFundAppropriated_AFBatchDeleteService.execute(specialFundAppropriated_AFModel);
						
						break;
					case "061204"://支付保证业务061204
						break;
					case "061203"://托管一般拨付管理061203
						break;
					case "061201"://资金归集管理061201
						break;
					case "061103"://三方协议061103
						break;
					case "061102"://合作协议061102
						break;
					case "061101"://银行协议061101
						break;
					case "060101"://版本管理060101
						break;
					case "030302"://工程进度巡查管理030302
						break;
					case "030301"://受限额度管理030301
						break;
					case "030201"://楼盘表管理030201
						break;
					case "030103"://备案价格030103
						break;
					case "030102"://楼幢信息030102
						break;
					case "030101"://项目信息030101
						break;
					case "020202"://开户行020202
						break;
					case "020201"://金融机构020201
						break;
					case "020103"://代理公司注册020103
						Emmp_CompanyInfoForm emmp_companyAgencyForm = new Emmp_CompanyInfoForm();
						emmp_companyAgencyForm.setTableId(sourceId);
						emmp_companyAgencyForm.setBusiCode(busiCode);
						companyAgencyDeleteService.execute(emmp_companyAgencyForm);
						break;
					case "020101"://开发企业注册020101
						Emmp_CompanyInfoForm emmp_companyInfoForm = new Emmp_CompanyInfoForm();
						emmp_companyInfoForm.setTableId(sourceId);
						emmp_companyInfoForm.setBusiCode(busiCode);
						companyInfoDeleteService.execute(emmp_companyInfoForm);
						break;
					case "010105"://范围授权010105
						break;
					case "010104"://菜单管理010104
						break;
					case "010103"://角色授权010103
						break;
					case "010102"://角色管理010102
						break;
					case "010101"://用户管理010101
						break;
					case "2302"://项目部门报表2302
						break;
					case "2301"://财务部门报表2301
						break;
					case "2201"://预警管理2201
						break;
					case "2102"://风控管理2102
						break;
					case "2101"://决策支持管理2101
						break;
					case "2004"://财务凭证2004
						break;
					case "2003"://财务计算2003
						break;
					case "2002"://对账管理2002
						break;
					case "2001"://账户管理2001
						break;
					case "0612"://资金管理0612
						break;
					case "0611"://协议管理0611
						break;
					case "0601"://基础配置管理0601
						break;
					case "0303"://工程进度管理0303
						break;
					case "0302"://楼盘表0302
						break;
					case "0301"://项目信息管理0301
						break;
					case "0202"://金融机构管理0202
						break;
					case "0201"://从业机构管理0201
						break;
					case "0101"://权限管理0101
						break;
					case "23"://查询统计23
						break;
					case "22"://预警管理22
						break;
					case "21"://辅助管理21
						break;
					case "20"://账务管理20
						break;
					case "06"://商品房资金托管06
						break;
					case "03"://项目管理03
						break;
					case "02"://机构管理02
						break;
					case "01"://系统管理01
						break;
					case "00"://首页00
						break;
					case "230119"://三方协议入账230119
						break;
					case "020203"://金融机构变更020203
						break;
					case "06120403"://支付保证撤销06120403
						Empj_PaymentGuaranteeForm empj_PaymentGuaranteeCancelForm = new Empj_PaymentGuaranteeForm();
						Long[] empj_PaymentGuaranteeCancelIdArr = new Long[1];
						empj_PaymentGuaranteeCancelIdArr[0] = sourceId;
						empj_PaymentGuaranteeCancelForm.setIdArr(empj_PaymentGuaranteeCancelIdArr);
						empj_PaymentGuaranteeCancelForm.setBusiCode(busiCode);
						empj_PaymentGuaranteeDeleteService.execute(empj_PaymentGuaranteeCancelForm);	
						break;
					case "033002"://预售合同同步033002
						break;
					case "032002"://预售证同步032002
						break;
					case "330"://预售合同0330
						break;
					case "0320"://预售证0320
						break;
					case "021020102"://风控例行抽查21020102
						break;
					case "06120601"://定向支付申请与复核06120601
						break;
					case "06120402"://支付保证拨付06120402			
						break;
					case "06120202"://退房退款申请-贷款未结清06120202、
						Tgpf_RefundInfoForm tgpf_refundinfoUnclearForm = new Tgpf_RefundInfoForm();
						Long[] tgpf_refundinfoUnclearIdArr = new Long[1];
						tgpf_refundinfoUnclearIdArr[0] = sourceId;
						tgpf_refundinfoUnclearForm.setIdArr(tgpf_refundinfoUnclearIdArr);
						tgpf_refundinfoUnclearForm.setBusiCode(busiCode);
						tgpf_RefundInfoBatchDeleteService.execute(tgpf_refundinfoUnclearForm);
						break;
					case "6120101"://贷款托管资金归集（6个接口）06120101
						break;
					case "06110302"://全额三方托管协议签署06110302
						break;
					case "06110202"://合作协议版本管理06110202
						break;
					case "06110103"://合作协议06110103
						break;
					case "06010102"://受限额度节点版本管理06010102
                        Tgpj_BldLimitAmountVer_AFForm bldLimitAmountVer_afAddForm = new Tgpj_BldLimitAmountVer_AFForm();
                        bldLimitAmountVer_afAddForm.setTableId(sourceId);
                        bldLimitAmountVer_afAddForm.setBusiCode(busiCode);
                        bldLimitAmountVer_afDeleteService.execute(bldLimitAmountVer_afAddForm);
                        break;
					case "03030102"://托管终止03030102
						Empj_BldEscrowCompletedForm empj_bldEscrowCompletedForm = new Empj_BldEscrowCompletedForm();
						empj_bldEscrowCompletedForm.setTableId(sourceId);
						empj_bldEscrowCompletedForm.setBusiCode(busiCode);
						empj_bldEscrowCompletedDeleteService.execute(empj_bldEscrowCompletedForm);

						break;
					case "03010201"://楼幢信息初始维护（含楼幢、单元、户室信息维护）03010201
						Empj_BuildingInfoForm empj_buildingInfoForm = new Empj_BuildingInfoForm();
						Long[] buildIdArr = new Long[1];
						buildIdArr[0] = sourceId;
						empj_buildingInfoForm.setIdArr(buildIdArr);
						empj_buildingInfoForm.setBusiCode(busiCode);
						buildingInfoDeleteService.execute(empj_buildingInfoForm);
						break;
					case "03010101": //项目信息注册与备案03010101
						Empj_ProjectInfoForm empj_projectInfoForm = new Empj_ProjectInfoForm();
						empj_projectInfoForm.setTableId(sourceId);
						empj_projectInfoForm.setBusiCode(busiCode);
						projectInfoDeleteService.execute(empj_projectInfoForm);
						break;
					case "210201"://内控管理210201
						break;
					case "210103"://收支存预测210103
						break;
					case "200402"://拨付凭证推送200402
						break;
					case "200301"://存留权益比对200301
						break;
					case "200201"://网银数据上传200201
						break;
					case "061205"://支付保函业务061205
						break;
					case "061202"://退房退款管理061202
						break;
					case "230206"://项目巡查预测计划表230206
						break;
					case "230205"://托管项目详情一览表230205
						break;
					case "020108"://合作机构注册020108
						Emmp_CompanyInfoForm companyCooperationForm = new Emmp_CompanyInfoForm();
						companyCooperationForm.setTableId(sourceId);
						companyCooperationForm.setBusiCode(busiCode);
						companyCooperationDeleteService.execute(companyCooperationForm);
						break;
					case "020106"://进度见证服务单位注册020106
						Emmp_CompanyInfoForm companyWitnessForm = new Emmp_CompanyInfoForm();
						companyWitnessForm.setTableId(sourceId);
						companyWitnessForm.setBusiCode(busiCode);
						companyWitnessDeleteService.execute(companyWitnessForm);
						break;
					case "010201"://档案配置010201
						break;
					case "0102"://档案管理0102
						break;
					case "220105"://合同信息预警220105
						break;
					case "230204"://托管情况分析表230204
						break;
					case "220107"://监管账户预警220107
						break;
					case "220108"://合同状态预警220108
						break;
					case "230101"://托管银行资金情况表230101
						break;
					case "230102"://楼幢账户报表230102
						break;
					case "230118"://入账金额核对表230118
						break;
					case "230103"://留存权益查询230103
						break;
					case "230104"://留存权益拨付明细230104
						break;
					case "230105"://大额到期对比表230105
						break;
					case "230106"://按权责发生制查询利息情况统计表230106
						break;
					case "230107"://利息预测表230107
						break;
					case "230108"://项目托管资金收支情况表230108
						break;
					case "230109"://托管楼幢明细表230109
						break;
					case "230110"://托管楼幢入账统计表230110
						break;
					case "230111"://托管楼幢拨付明细表230111
						break;
					case "230112"://户入账明细230112
						break;
					case "230113"://银行放款项目入账情况表230113
						break;
					case "230114"://银行付款项目出账情况表230114
						break;
					case "230115"://日记账确认统计230115
						break;
					case "230116"://托管现金流量表230116
						break;
					case "230117"://托管项目统计表230117
						break;
					case "230201"://项目风险明细表230201
						break;
					case "230202"://见证报告统计表230202
						break;
					case "230203"://托管户信息明细表230203
						break;
					default:
				}
			}
		}
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
