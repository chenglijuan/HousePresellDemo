package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.core.util.NumberUtil;

/**
 * Service新增时根据输入的三方协议号查询相关信息：退房退款-贷款已结清
 * Company：ZhiShuSZ
 * 
 * @author lei.sunn
 * @date 2018年8月10日17:06:37
 *
 */
@Service
@Transactional
public class Tgpf_RefundInfoAddQueryService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		// 三方协议号
		String eCodeOfTripleAgreement = model.geteCodeOfTripleAgreement();
		// 楼幢户主键
		Long houseInfoId = model.getHouseId();

		Long userId = model.getUserId();// 获取登陆用户Id

		if (null == eCodeOfTripleAgreement || eCodeOfTripleAgreement.trim().isEmpty())
		{
			model.seteCodeOfTripleAgreement(null);
		}

		Empj_HouseInfo house = new Empj_HouseInfo();

		if (null == houseInfoId || houseInfoId == 0)
		{
			house = null;
		}
		else
		{
			house = empj_HouseInfoDao.findById(houseInfoId);
		}

		if ((null == eCodeOfTripleAgreement || eCodeOfTripleAgreement.trim().isEmpty())
				&& (null == houseInfoId || houseInfoId == 0))
		{
			return MyBackInfo.fail(properties, "查询条件为空，请核对查询条件！");
		}

		Tgxy_TripleAgreementForm form = new Tgxy_TripleAgreementForm();

		if (null == eCodeOfTripleAgreement || eCodeOfTripleAgreement.trim().isEmpty())
		{
			form.setHouse(house);
		}
		else
		{
//			form.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
			form.setCodeFroTripleOrContract(eCodeOfTripleAgreement);
			
		}

		form.setTheState(S_TheState.Normal);

		// 查询三方协议信息
		Tgxy_TripleAgreement tgxy_TripleAgreement = tgxy_TripleAgreementDao
				.findOneByQuery_T(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), form));

		if (null == tgxy_TripleAgreement)
		{
			return MyBackInfo.fail(properties, "没有查询到有效的三方协议数据");
		}
		else
		{
			// 三方协议号 不为空 查询退房退款信息是否已经存在对应的记录
			Tgpf_RefundInfoForm refundForm = new Tgpf_RefundInfoForm();
			refundForm.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());
			refundForm.setTheState(S_TheState.Normal);

			Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao
					.findOneByQuery_T(tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL(), refundForm));
			if (tgpf_RefundInfo != null)
			{
				return MyBackInfo.fail(properties, "该三方协议或合同号：" + eCodeOfTripleAgreement + " 在数据库中已存在。");
			}

			Empj_BuildingInfo buildingInfo = tgxy_TripleAgreement.getBuildingInfo();
			if (null == buildingInfo)
			{
				return MyBackInfo.fail(properties, "三方协议关联楼幢失败");
			}

			Emmp_CompanyInfo developCompany = buildingInfo.getDevelopCompany();
			if (null == developCompany)
			{
				return MyBackInfo.fail(properties, "三方协议关联开发企业失败");
			}

			Sm_UserForm userModel = new Sm_UserForm();
			userModel.setTableId(userId);
			userModel.setCompany(developCompany);

			Sm_User sm_User = sm_UserDao.findOneByQuery_T(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), userModel));
			if (null == sm_User)
			{
				return MyBackInfo.fail(properties, "该用户没有权限操作该条三方协议");
			}
		}

		// 三方协议主键
		Long tripleAgreementId = tgxy_TripleAgreement.getTableId();

		// 三方协议号
		eCodeOfTripleAgreement = tgxy_TripleAgreement.geteCodeOfTripleAgreement();

		// 合同备案号
		String eCodeOfContractRecord = tgxy_TripleAgreement.geteCodeOfContractRecord();

		// 项目名称
		String theNameOfProject = tgxy_TripleAgreement.getTheNameOfProject();

		// 查询合同
		Tgxy_ContractInfoForm contractform = new Tgxy_ContractInfoForm();
		/* contractform.setBusiState("0"); */
		contractform.setTheState(S_TheState.Normal);
		contractform.seteCodeOfContractRecord(eCodeOfContractRecord);
		Tgxy_ContractInfo tgxy_ContractInfo = tgxy_ContractInfoDao
				.findOneByQuery_T(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), contractform));

		if (null == tgxy_ContractInfo)
		{
			return MyBackInfo.fail(properties, "预售系统买卖合同查询为空");
		}

		// 合同金额
		Double contractAmount = tgxy_ContractInfo.getContractSumPrice();

		// 贷款总金额
		Double loanAmount = tgxy_ContractInfo.getLoanAmount();

		// 留存权益总金额
		Double theAmountOfRetainedEquity = tgxy_TripleAgreement.getTheAmountOfRetainedEquity();

		// 到期留存权益金额
		Double theAmountOfInterestRetained = tgxy_TripleAgreement.getTheAmountOfInterestRetained();

		// 未到期留存权益金额
		Double theAmountOfInterestUnRetained = tgxy_TripleAgreement.getTheAmountOfInterestUnRetained();

		// 本次退款金额
		Double refundAmount = theAmountOfRetainedEquity;

		if (null != refundAmount && refundAmount > 0)
		{
			// 对本次退款金额进行四舍五入
			refundAmount = Double.parseDouble(NumberUtil.roundStr(refundAmount, 2));
		}

		// 实际退款金额
		Double actualRefundAmount = refundAmount;

		// 根据合同备案号查询买受人信息
		Tgxy_BuyerInfoForm tgxy_BuyerInfoForm = new Tgxy_BuyerInfoForm();

		tgxy_BuyerInfoForm.seteCodeOfContract(eCodeOfContractRecord);

		List<Tgxy_BuyerInfo> buyerInfos = tgxy_BuyerInfoDao
				.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), tgxy_BuyerInfoForm));

		if (null == buyerInfos || buyerInfos.size() == 0)
		{
			return MyBackInfo.fail(properties, "买受人信息查询为空");
		}

		// 买受人id
		StringBuffer theBuyerId = new StringBuffer();

		// 买受人名称
		StringBuffer theNameOfBuyer = new StringBuffer();

		// 买受人证件号码
		StringBuffer certificateNumberOfBuyer = new StringBuffer();

		// 联系电话
		StringBuffer contactPhoneOfBuyer = new StringBuffer();

		for (int i = 0; i < buyerInfos.size(); i++)
		{

			if (null != buyerInfos.get(i).getBuyerName() && !buyerInfos.get(i).getBuyerName().trim().isEmpty())
			{
				theNameOfBuyer.append(buyerInfos.get(i).getBuyerName());
			}

			if (null != buyerInfos.get(i).geteCodeOfcertificate()
					&& !buyerInfos.get(i).geteCodeOfcertificate().trim().isEmpty())
			{
				certificateNumberOfBuyer.append(buyerInfos.get(i).geteCodeOfcertificate());
			}

			if (null != buyerInfos.get(i).getContactPhone() && !buyerInfos.get(i).getContactPhone().trim().isEmpty())
			{
				contactPhoneOfBuyer.append(buyerInfos.get(i).getContactPhone());
			}

			theBuyerId.append(buyerInfos.get(i).getTableId());

			if (i != buyerInfos.size() - 1)
			{
				if (null != theNameOfBuyer && theNameOfBuyer.length() > 0)
				{
					theNameOfBuyer.append(",");
				}

				if (null != certificateNumberOfBuyer && certificateNumberOfBuyer.length() > 0)
				{
					certificateNumberOfBuyer.append(",");
				}

				if (null != contactPhoneOfBuyer && contactPhoneOfBuyer.length() > 0)
				{
					contactPhoneOfBuyer.append(",");
				}

				theBuyerId.append(",");
			}
		}
		// 主借款人

		// 查询所属项目信息
		Empj_BuildingInfo buildingInfo = tgxy_TripleAgreement.getBuildingInfo();
		if (null == buildingInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的楼幢基本信息");
		}
		Empj_ProjectInfo project = buildingInfo.getProject();
		if (null == project)
		{
			return MyBackInfo.fail(properties, "未查询到有效的项目信息");
		}
		// 查询开发企业 监管账号 开户行名称
		Emmp_CompanyInfo emmp_CompanyInfo = buildingInfo.getDevelopCompany();// 开发企业
		if (null == emmp_CompanyInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的开发企业信息");
		}

		Tgpj_BankAccountSupervisedForm bankAccountForm = new Tgpj_BankAccountSupervisedForm();
		bankAccountForm.setTheState(S_TheState.Normal);
		bankAccountForm.setDevelopCompany(emmp_CompanyInfo);

		List<Tgpj_BankAccountSupervised> bankAccountSupervisedList = tgpj_BankAccountSupervisedDao.findByPage(
				tgpj_BankAccountSupervisedDao.getQuery(tgpj_BankAccountSupervisedDao.getBasicHQL(), bankAccountForm));

		if (null == bankAccountSupervisedList || bankAccountSupervisedList.size() < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的监管账户信息");
		}

		Long buildingId = buildingInfo.getTableId();
		Long projectId = project.getTableId();

		// 根据三方协议查询户信息
		Empj_HouseInfoForm empj_HouseInfoForm = new Empj_HouseInfoForm();

		empj_HouseInfoForm.setTripleAgreement(tgxy_TripleAgreement);

		Long houseId = tgxy_TripleAgreement.getHouse().getTableId();
		Empj_HouseInfo empj_HouseInfo = empj_HouseInfoDao.findById(houseId);

		if (null == empj_HouseInfo)
		{
			return MyBackInfo.fail(properties, "户室信息查询为空");
		}

		// 房屋坐落
		String position = empj_HouseInfo.getPosition();

		properties.put("bankAccountSupervisedList", bankAccountSupervisedList);
		properties.put("developCompanyName", emmp_CompanyInfo.getTheName());

		properties.put("eCodeOfTripleAgreement", eCodeOfTripleAgreement);
		properties.put("eCodeOfContractRecord", eCodeOfContractRecord);
		properties.put("theNameOfProject", theNameOfProject);
		properties.put("contractAmount", contractAmount);
		properties.put("loanAmount", loanAmount);
		properties.put("theNameOfBuyer", theNameOfBuyer.toString());
		properties.put("certificateNumberOfBuyer", certificateNumberOfBuyer.toString());
		properties.put("contactPhoneOfBuyer", contactPhoneOfBuyer.toString());
		properties.put("position", position);
		properties.put("theNameOfCreditor", theNameOfBuyer.toString());
		properties.put("theAmountOfRetainedEquity", theAmountOfRetainedEquity);
		properties.put("theAmountOfInterestRetained", theAmountOfInterestRetained);
		properties.put("theAmountOfInterestUnRetained", theAmountOfInterestUnRetained);
		properties.put("refundAmount", refundAmount);
		properties.put("actualRefundAmount", actualRefundAmount);
		properties.put("tripleAgreementId", tripleAgreementId);
		properties.put("theBuyerId", theBuyerId.toString());
		properties.put("projectId", projectId);
		properties.put("buildingId", buildingId);
		properties.put("receiverName", theNameOfBuyer.toString());

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
