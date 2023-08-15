package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.util.MyBackInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：预加载当前登录用户机构
 * 属于开发企业，则直接加载开发企业信息
 * 不属于开发企业，加载所有开发企业列表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_SpecialFundCompanyPreService
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_UserDao sm_userDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Emmp_CompanyInfoForm model)
	{
		Properties properties = new MyProperties();

		String isList;

		Long loginUserId = model.getUserId();

		if (null == loginUserId || loginUserId < 0)
		{
			loginUserId = 148204L;
		}

		Sm_User loginUser = sm_userDao.findById(loginUserId);
		if (loginUser.getCompany() == null)
		{
			return MyBackInfo.fail(properties, "登录用户所属企业不能为空");
		}

		Emmp_CompanyInfo emmp_companyInfo = loginUser.getCompany();
		if (emmp_companyInfo.getTheType() == null)
		{
			return MyBackInfo.fail(properties, "机构类型不能为空");
		}

		// 判断所属机构是否是开发企业：1
		if (S_CompanyType.Development.equals(emmp_companyInfo.getTheType()))
		{
			properties.put("companyInfo", emmp_companyInfo);

			isList = "0";
		}
		else
		{

			// 不是开发企业，加载所有开发企业列表
			model = new Emmp_CompanyInfoForm();
			model.setTheState(S_TheState.Normal);
			model.setTheType(S_CompanyType.Development);
			model.setKeyword(null);

			Integer totalCount = emmp_CompanyInfoDao
					.findByPage_Size(emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.getBasicHQL(), model));

			List<Emmp_CompanyInfo> emmp_CompanyInfoList;
			if (totalCount > 0)
			{
				emmp_CompanyInfoList = emmp_CompanyInfoDao
						.findByPage(emmp_CompanyInfoDao.getQuery(emmp_CompanyInfoDao.getBasicHQL(), model));
			}
			else
			{
				emmp_CompanyInfoList = new ArrayList<Emmp_CompanyInfo>();
			}

			properties.put("emmp_CompanyInfoList", emmp_CompanyInfoList);

			properties.put(S_NormalFlag.totalCount, totalCount);

			isList = "1";
		}

		properties.put("isList", isList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
