package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：三方协议入账查询
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_TripleOfDepositDetailService
{

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_DepositDetailForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String startBillTimeStamp = model.getBillTimeStamp();// 记账日期 （起始）
		String endBillTimeStamp = model.getEndBillTimeStamp();// 记账日期 （结束）

		Sm_User user = model.getUser();

		if (null == user)
		{
			return MyBackInfo.fail(properties, "用户登陆失败！");
		}

		// 查询用户所属机构类型
		String companyType = user.getCompany().getTheType();// 企业类型

		if (null != companyType && companyType.trim().equals(S_CompanyType.Zhengtai))
		{
			model.setCompanyNames(null);
		}
		else
		{
			Long[] developCompanyInfoIdArr = model.getDevelopCompanyInfoIdArr();

			if (null == developCompanyInfoIdArr || developCompanyInfoIdArr.length == 0)
			{
				List<Tgpf_DepositDetail> tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();

				properties.put("tgpf_DepositDetailList", tgpf_DepositDetailList);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				return properties;
			}
			else
			{
				String[] companyNames = new String[developCompanyInfoIdArr.length];

				for (int i = 0; i < developCompanyInfoIdArr.length; i++)
				{
					Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(developCompanyInfoIdArr[i]);
					if (null != companyInfo)
					{
						companyNames[i] = companyInfo.getTheName();
					}
				}

				model.setCompanyNames(companyNames);
			}
		}

		Long branchId = model.getBankBranchId();// 开户行Id

		Long companyId = model.getCompanyId();

		Long projectId = model.getProjectId();

		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}
		if (null == startBillTimeStamp || startBillTimeStamp.length() == 0)
		{
			model.setBillTimeStamp(null);
		}
		if (null == endBillTimeStamp || endBillTimeStamp.length() == 0)
		{
			model.setEndBillTimeStamp(null);
		}

		if (null == branchId || branchId < 0)
		{
			model.setBankBranchId(null);
		}

		if (null == companyId || companyId < 0)
		{
			model.setCompanyId(null);
		}
		else
		{
			Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(companyId);
			if (null == companyInfo)
			{
				return MyBackInfo.fail(properties, "未查询到关联的企业信息");
			}
			model.setCompanyName(companyInfo.getTheName());
		}

		if (null == projectId || projectId < 0)
		{
			model.setProjectId(null);
		}

		model.setTheState(S_TheState.Normal);

		Integer totalCount = tgpf_DepositDetailDao
				.findByPage_Size(tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL2(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		if (totalCount > 0)
		{
			tgpf_DepositDetailList = tgpf_DepositDetailDao.findByPage(
					tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL2(), model), pageNumber,
					countPerPage);
		}
		else
		{
			tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();
		}

		properties.put("tgpf_DepositDetailList", tgpf_DepositDetailList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
