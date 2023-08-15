package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Empj_PaymentGuaranteeListService
{

	@Autowired
	private Empj_PaymentGuaranteeDao empj_PaymentGuaranteeDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PaymentGuaranteeForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String busiState = model.getBusiState();
		
//		String companyName = model.getCompanyName();
//		String projectName = model.getProjectName();
		String applyDate = model.getApplyDate();
		
		if (applyDate == null || applyDate.trim().isEmpty() || applyDate.trim().equals(""))
		{
			model.setApplyDate(null);
		}
		
		if(null !=  model.getApplyDate() && model.getApplyDate().length() > 0)
		{
			
		}
		else
		{
			model.setApplyDate(null);
		}
		
		if (busiState != null && !"".equals(busiState)) 
		{
			model.setBusiState(busiState);
		}
		else
		{
			model.setBusiState(null);
		}

		if (keyword != null && keyword.length() > 0)
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		// 根据开发企业查询
		if (null != model.getCompanyId() && model.getCompanyId() > 0)
		{
			Long companyId = model.getCompanyId();
			
			Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(companyId);
			if (emmp_CompanyInfo == null || S_TheState.Deleted.equals(emmp_CompanyInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "企业信息有误！");
			}

			model.setEmmp_CompanyInfo(emmp_CompanyInfo);
		}

		// 根据项目查询
		if (null != model.getProjectId() && model.getProjectId() > 0)
		{
			Long empj_ProjectInfoId = model.getProjectId();
			Empj_ProjectInfo empj_ProjectInfo = (Empj_ProjectInfo) empj_ProjectInfoDao.findById(empj_ProjectInfoId);
			if (empj_ProjectInfo == null || S_TheState.Deleted.equals(empj_ProjectInfo.getTheState()))
			{
				return MyBackInfo.fail(properties, "项目信息有误！");
			}

			model.setEmpj_ProjectInfo(empj_ProjectInfo);
		}
		model.setTheState(S_TheState.Normal);
		
		model.setCancel("支付保证撤销");
		
		Integer totalCount = empj_PaymentGuaranteeDao
				.findByPage_Size(empj_PaymentGuaranteeDao.getQuery_Size(empj_PaymentGuaranteeDao.getBasicHQL(), model));
		Integer totalPage = totalCount / countPerPage;

		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Empj_PaymentGuarantee> empj_PaymentGuaranteeList;
		if (totalCount > 0)
		{
			empj_PaymentGuaranteeList = empj_PaymentGuaranteeDao.findByPage(
					empj_PaymentGuaranteeDao.getQuery(empj_PaymentGuaranteeDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		}
		else
		{
			empj_PaymentGuaranteeList = new ArrayList<Empj_PaymentGuarantee>();
		}
		
		System.out.println("size = " + empj_PaymentGuaranteeList.size());
		properties.put("empj_PaymentGuaranteeList", empj_PaymentGuaranteeList);
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
