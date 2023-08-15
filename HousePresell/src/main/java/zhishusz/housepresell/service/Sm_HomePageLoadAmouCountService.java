package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.rebuild.Empj_ProjectInfoRebuild;

/**
 * 首页展示加载-合作机构首页-加载账户信息
 * 
 * @ClassName: Sm_HomePageLoadAmouCountService
 * @Description:TODO
 * @author: yangyu
 * @date: 2019年1月10日
 * @version V1.0
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageLoadAmouCountService
{

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	private MyDouble myDouble = MyDouble.getInstance();

	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	@Autowired
	private Empj_ProjectInfoRebuild rebuild;
	
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

	@SuppressWarnings({
			"unchecked"
	})
	public Properties execute(Tb_b_contractFrom model)
	{
		Properties properties = new Properties();

		// model.setCityRegionId(78L);
		/**
		 * 获取登陆用户进行判断登陆用户是否是合作机构用户
		 * 是 获取代理机构下的所有的项目并查询
		 * 否 返回提示消息
		 */

		Sm_User sm_User = model.getUser();

		if (null == sm_User)
		{
			return MyBackInfo.fail(properties, "登陆失败，请重新登陆");
		}

		Emmp_CompanyInfo companyInfo = sm_User.getCompany();

		if (null == companyInfo)
		{
			return MyBackInfo.fail(properties, "登陆用户关联楼幢失败，请核对。");
		}

		// 机构类型
		String companyType = companyInfo.getTheType();

		if (null != companyType && companyType.equals(S_CompanyType.Cooperation))
		{
			Long regionId = model.getCityRegionId();

			Long[] idArr = model.getCityRegionInfoIdArr();

			// 获取机构下项目坐标
			Empj_ProjectInfoForm projectForm = new Empj_ProjectInfoForm();
			projectForm.setUser(model.getUser());
			projectForm.setUserId(model.getUserId());

			projectForm.setUser(model.getUser());
			projectForm.setUserId(model.getUserId());

			if (null != regionId && regionId > 0)
			{
				projectForm.setCityRegionId(model.getCityRegionId());
			}
			else
			{
				projectForm.setCityRegionInfoIdArr(idArr);
			}
			
			/**
			 * 查询楼幢账户
			 * 根据开发企业查询楼幢账户 Tgpj_BuildingAccount
			 * 统计托管项目，托管面积，总入账金额，总拨付金额，托管余额，受限金额，可拨付金额
			 * 
			 * modified by time  2019.02.15
			 * 	增加合作协议签约量和三方协议签约量
			 */
			int projectC = 0;// 托管项目统计
			int tripleAgreementC = 0;//三方协议签约数量
			Double escrowAreaC = 0.00;// 托管面积
			Double totalAccountAmountC = 0.00;// 总入账金额
			Double payoutAmountC = 0.00;// 总拨付金额
			Double currentEscrowFundC = 0.00;// 托管余额
			Double effectiveLimitedAmountC = 0.00;// 受限金额
			Double allocableAmountC = 0.00;// 可拨付金额
			
			// 获取机构下项目坐标
			Empj_ProjectInfoForm projectForm2 = new Empj_ProjectInfoForm();
			projectForm2.setUser(model.getUser());
			projectForm2.setUserId(model.getUserId());
			
			if (null != regionId && regionId > 1)
			{
				projectForm2.setCityRegionId(model.getCityRegionId());
			}
			else
			{
				projectForm2.setCityRegionInfoIdArr(idArr);
			}

			List<Empj_ProjectInfo> empj_ProjectInfoList2 = new ArrayList<Empj_ProjectInfo>();

			empj_ProjectInfoList2 = empj_ProjectInfoDao
					.findByPage(empj_ProjectInfoDao.createNewCriteriaForList(projectForm2));

			if (null != empj_ProjectInfoList2 && empj_ProjectInfoList2.size() > 0)
			{
				projectC = rebuild.getDepositProject(empj_ProjectInfoList2).size();
				
				for (Empj_ProjectInfo empj_ProjectInfo : empj_ProjectInfoList2)
				{
					Tgxy_TripleAgreementForm tripleAgreementForm = new  Tgxy_TripleAgreementForm();
					tripleAgreementForm.setTheState(S_TheState.Normal);
					tripleAgreementForm.setProjectId(empj_ProjectInfo.getTableId());
					
					Integer tripleCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), tripleAgreementForm));
					
					tripleAgreementC += tripleCount;
				}
			}
			
			Tgxy_EscrowAgreementForm escrowAgreementForm = new Tgxy_EscrowAgreementForm();
			escrowAgreementForm.setTheState(S_TheState.Normal);
//			escrowAgreementForm.setDevelopCompany(companyInfo);
			if (null != regionId && regionId > 1)
			{
				escrowAgreementForm.setCityRegionId(model.getCityRegionId());
			}
			else
			{
				escrowAgreementForm.setCityRegionInfoIdArr(idArr);
			}
			
			
			int escorwC = tgxy_EscrowAgreementDao.findByPage_Size(tgxy_EscrowAgreementDao.getQuery_Size(tgxy_EscrowAgreementDao.getRecordTime(), escrowAgreementForm));

			Map<String, Object> buildingAccountMap = new HashMap<>();

			Tgpj_BuildingAccountForm buildingAccountForm = new Tgpj_BuildingAccountForm();
			//buildingAccountForm.setDevelopCompanyId(companyInfo.getTableId());
			buildingAccountForm.setTheState(S_TheState.Normal);
			
			if (null != regionId && regionId > 1)
			{
				buildingAccountForm.setCityRegionId(model.getCityRegionId());
			}
			else
			{
				buildingAccountForm.setCityRegionInfoIdArr(idArr);
			}

			List<Tgpj_BuildingAccount> buildingAcountList = tgpj_BuildingAccountDao.findByPage(
					tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), buildingAccountForm));

			if (null != buildingAcountList && buildingAcountList.size() > 0)
			{
				for (Tgpj_BuildingAccount buildingAccount : buildingAcountList)
				{
					if (null != buildingAccount.getEscrowArea())
					{
						escrowAreaC = myDouble.doubleAddDouble(escrowAreaC, buildingAccount.getEscrowArea());
					}

					if (null != buildingAccount.getTotalAccountAmount())
					{
						totalAccountAmountC = myDouble.doubleAddDouble(totalAccountAmountC,
								buildingAccount.getTotalAccountAmount());
					}

					if (null != buildingAccount.getPayoutAmount())
					{
						payoutAmountC = myDouble.doubleAddDouble(payoutAmountC, buildingAccount.getPayoutAmount());
					}
					
					if (null != buildingAccount.getRefundAmount())
					{
						payoutAmountC = myDouble.doubleAddDouble(payoutAmountC, buildingAccount.getRefundAmount());
					}

					if (null != buildingAccount.getCurrentEscrowFund())
					{
						currentEscrowFundC = myDouble.doubleAddDouble(currentEscrowFundC,
								buildingAccount.getCurrentEscrowFund());
					}

					if (null != buildingAccount.getEffectiveLimitedAmount())
					{
						effectiveLimitedAmountC = myDouble.doubleAddDouble(effectiveLimitedAmountC,
								buildingAccount.getEffectiveLimitedAmount());
					}

					if (null != buildingAccount.getAllocableAmount())
					{
						allocableAmountC = myDouble.doubleAddDouble(allocableAmountC,
								buildingAccount.getAllocableAmount());
					}

				}

				Double v2 = 10000.00;// 除数

				int scale = 2;// 保留小数位数

				if (escrowAreaC > 0)
				{
					escrowAreaC = myDouble.div(escrowAreaC, v2, scale);
				}

				if (totalAccountAmountC > 0)
				{
					totalAccountAmountC = myDouble.div(totalAccountAmountC, v2, scale);
				}

				if (payoutAmountC > 0)
				{
					payoutAmountC = myDouble.div(payoutAmountC, v2, scale);
				}

				if (currentEscrowFundC > 0)
				{
					currentEscrowFundC = myDouble.div(currentEscrowFundC, v2, scale);
				}

				if (effectiveLimitedAmountC > 0)
				{
					effectiveLimitedAmountC = myDouble.div(effectiveLimitedAmountC, v2, scale);
				}

				if (allocableAmountC > 0)
				{
					allocableAmountC = myDouble.div(allocableAmountC, v2, scale);
				}
			}

			buildingAccountMap.put("projectC", projectC);
			buildingAccountMap.put("escrowAreaC", escrowAreaC);
			buildingAccountMap.put("totalAccountAmountC", totalAccountAmountC);
			buildingAccountMap.put("payoutAmountC", payoutAmountC);
			buildingAccountMap.put("currentEscrowFundC", currentEscrowFundC);
			buildingAccountMap.put("effectiveLimitedAmountC", effectiveLimitedAmountC);
			buildingAccountMap.put("allocableAmountC", allocableAmountC);
			buildingAccountMap.put("escorwC", escorwC);
			buildingAccountMap.put("tripleAgreementC", tripleAgreementC);

			properties.put("buildAccountMap", buildingAccountMap);
		}
		else
		{
			return MyBackInfo.fail(properties, "调用方法失败，请核对调用方法是否正确。");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
