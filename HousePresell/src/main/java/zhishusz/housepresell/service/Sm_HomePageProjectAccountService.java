package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service开发企业登录楼幢托管信息加载
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_HomePageProjectAccountService
{
	private static final Double DIV = 10000.00;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;// 资金归集明细
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BuildingAccountForm model)
	{
		Properties properties = new MyProperties();
		//构建查询对象
		Tgpj_BuildingAccountForm tgpj_BuildingAccountModel = new Tgpj_BuildingAccountForm(); 
		tgpj_BuildingAccountModel.setTheState(S_TheState.Normal);
		/*
		 * xsz by time 2019-1-10 14:57:04
		 * 1.加载用户所属开发企业下的所有楼幢账户信息
		 */
		Sm_User user = model.getUser();
		Emmp_CompanyInfo company = user.getCompany();
		
		Long projectId = model.getProjectId();
		if (null != projectId)
		{
			// 带条件查询，直接查询指定项目下的楼幢信息
			model.setProjectId(projectId);
			tgpj_BuildingAccountModel.setProjectId(projectId);
			
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(projectId);
			company = projectInfo.getDevelopCompany();

		}
		else
		{
			model.setProjectId(null);
		}
		
		// 设置楼幢查询条件（根据开发企业名称加载）
		model.setDevelopCompanyId(company.getTableId());
		tgpj_BuildingAccountModel.setDevelopCompanyId(company.getTableId());
				
		// 加载托管项目楼幢相关信息
		Integer totalCount = tgpj_BuildingAccountDao.findByPage_Size(tgpj_BuildingAccountDao.getQuery_Size(tgpj_BuildingAccountDao.getBasicHQL(), tgpj_BuildingAccountModel));
		
		List<Tgpj_BuildingAccount> tgpj_BuildingAccountList;
		if (totalCount > 0)
		{
			tgpj_BuildingAccountList = tgpj_BuildingAccountDao.findByPage(tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), tgpj_BuildingAccountModel));
		}
		else
		{
			tgpj_BuildingAccountList = new ArrayList<Tgpj_BuildingAccount>();
		}
		
		Map<String, Object> projectAccount = new HashMap<>();
		projectAccount.put("kbfAmount", 0.00);// 可拨付金额（万元）
		projectAccount.put("bfzAmount", 0.00);// 拨付中（万元）
		projectAccount.put("tgyeAmount", 0.00);// 托管余额（万元）
		projectAccount.put("zrzAmount", 0.00);// 总入账金额（万元）
		projectAccount.put("zbfAmount", 0.00);// 总拨付金额（万元）
		projectAccount.put("qyCount", 0);// 协议签约量（件）
		projectAccount.put("fkCount", 0);// 贷款放款量（笔）
		
		//存在楼幢账户信息，进行数据统计
		if(tgpj_BuildingAccountList.size()>0)
		{
			// allocableAmount 可划拨金额
			// appropriateFrozenAmount 拨付冻结金额
			// currentEscrowFund 当前托管余额
			// totalAccountAmount 总入账金额
			// payoutAmount 已拨付金额

			
			/*
			 * 统计账户信息
			 */
			Double kbfAmount = 0.00;
			Double bfzAmount = 0.00;
			Double tgyeAmount = 0.00;
			Double zrzAmount = 0.00;
			Double zbfAmount = 0.00;
			
			/*
			 * double类型计算
			 * 
			 * doubleAddDouble 加
			 * doubleSubtractDouble 减
			 * doubleMultiplyDouble 乘
			 * div 除
			 * getShort() 保留小数位
			 */
			MyDouble dplan = MyDouble.getInstance();
			
			for (Tgpj_BuildingAccount po : tgpj_BuildingAccountList)
			{
				kbfAmount = dplan.doubleAddDouble(kbfAmount, null==po.getAllocableAmount()?0.00:po.getAllocableAmount());
				bfzAmount = dplan.doubleAddDouble(bfzAmount, null==po.getAppropriateFrozenAmount()?0.00:po.getAppropriateFrozenAmount());
				tgyeAmount = dplan.doubleAddDouble(tgyeAmount, null==po.getCurrentEscrowFund()?0.00:po.getCurrentEscrowFund());
				zrzAmount = dplan.doubleAddDouble(zrzAmount, null==po.getTotalAccountAmount()?0.00:po.getTotalAccountAmount());
				zbfAmount = dplan.doubleAddDouble(zbfAmount, null==po.getPayoutAmount()?0.00:po.getPayoutAmount());
				zbfAmount = dplan.doubleAddDouble(zbfAmount, null==po.getRefundAmount()?0.00:po.getRefundAmount());
			}
			
			projectAccount.put("kbfAmount", dplan.div(kbfAmount, DIV, 2));// 可拨付金额（万元）
			projectAccount.put("bfzAmount", dplan.div(bfzAmount, DIV, 2));// 拨付中（万元）
			projectAccount.put("tgyeAmount", dplan.div(tgyeAmount, DIV, 2));// 托管余额（万元）
			projectAccount.put("zrzAmount", dplan.div(zrzAmount, DIV, 2));// 总入账金额（万元）
			projectAccount.put("zbfAmount", dplan.div(zbfAmount, DIV, 2));// 总拨付金额（万元）
			
			/*
			 * 统计协议签约量
			 * 有效=setTheState(S_TheState.Normal);
			 * 已备案=setBusiState(S_BusiState.HaveRecord);
			 */
			Tgxy_TripleAgreementForm tgxy_TripleAgreementModel = new Tgxy_TripleAgreementForm();
			tgxy_TripleAgreementModel.setCompany(company);
			tgxy_TripleAgreementModel.setTheState(S_TheState.Normal);
//			tgxy_TripleAgreementModel.setBusiState(S_BusiState.HaveRecord);
			tgxy_TripleAgreementModel.setApprovalState(S_ApprovalState.Completed);
			
			if (null != projectId)
			{
				// 带条件查询，直接查询指定项目下的楼幢信息
				tgxy_TripleAgreementModel.setProjectId(projectId);

			}
			
			Integer tgxy_TripleAgreementCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getTripleAgreementAcount(), tgxy_TripleAgreementModel));
			
			/*
			 * 统计贷款放款量
			 */
			Tgpf_DepositDetailForm tgpf_DepositDetailModel = new Tgpf_DepositDetailForm();
			tgpf_DepositDetailModel.setTheState(S_TheState.Normal);
			tgpf_DepositDetailModel.setCompanyInfo(company);
			
			if (null != projectId)
			{
				// 带条件查询，直接查询指定项目下的楼幢信息
				tgpf_DepositDetailModel.setProjectId(projectId);

			}
			
			Integer tgpf_DepositDetailCount = tgpf_DepositDetailDao.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL3(), tgpf_DepositDetailModel));
			
			projectAccount.put("qyCount", tgxy_TripleAgreementCount);// 协议签约量（件）
			projectAccount.put("fkCount", tgpf_DepositDetailCount);// 贷款放款量（笔）
			
		}
		

		properties.put("projectAccount", projectAccount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
