package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_LoanProjectCountM_ViewForm;
import zhishusz.housepresell.database.dao.Tg_LoanProjectCountM_ViewListDao;
import zhishusz.housepresell.database.po.Tg_LoanProjectCountM_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管项目统计表（财务部）
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_LoanProjectCountM_ViewListService {
	@Autowired
	private Tg_LoanProjectCountM_ViewListDao tg_LoanProjectCountM_ViewListDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_LoanProjectCountM_ViewForm model) {
		Properties properties = new MyProperties();
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		// 获取查询条件
		String usedId =String.valueOf(model.getUserId());
		String keyword = null;
		if(null != model.getKeyword() && !model.getKeyword().trim().isEmpty())
		{
			keyword = model.getKeyword().trim();
		}
//		String keyword = String.valueOf(model.getKeyword());// 关键字
		String cityRegionId = null;
		if(null != model.getCityRegionId())
		{
			cityRegionId = String.valueOf(model.getCityRegionId());
		}
//		String cityRegionId = String.valueOf(model.getCityRegionId());// 区域Id
		String companyId = null;
		if(null != model.getCompanyId())
		{
			companyId = String.valueOf(model.getCompanyId());
		}
//		String companyId = String.valueOf(model.getCompanyId());// 开发企业ID
		String projectId = null;
		if(null != model.getCompanyId())
		{
			projectId = String.valueOf(model.getProjectId());
		}
//		String projectId = String.valueOf(model.getProjectId());// 项目ID
		String buildId = null;// 楼幢ID

		String contractSigningDate = null;
		if(null != model.getContractSigningDate() && !model.getContractSigningDate().trim().isEmpty())
		{
			contractSigningDate = model.getContractSigningDate().trim();
		}
//		String contractSigningDate = model.getContractSigningDate();
		String endContractSigningDate = null;
		if(null != model.getEndContractSigningDate() && !model.getEndContractSigningDate().trim().isEmpty())
		{
			endContractSigningDate = model.getEndContractSigningDate().trim();
		}
//		String endContractSigningDate = model.getEndContractSigningDate();
		String preSaleCardDate = null;
		if(null != model.getPreSaleCardDate() && !model.getPreSaleCardDate().trim().isEmpty())
		{
			preSaleCardDate = model.getPreSaleCardDate().trim();
		}
//		String preSaleCardDate = model.getPreSaleCardDate();
		String endPreSaleCardDate = null;
		if(null != model.getEndPreSaleCardDate() && !model.getEndPreSaleCardDate().trim().isEmpty())
		{
			endPreSaleCardDate = model.getEndPreSaleCardDate().trim();
		}
//		String endPreSaleCardDate = model.getEndPreSaleCardDate();
		String eCodeOfAgreement = null;
		if(null != model.getECodeOfAgreement() && !model.getECodeOfAgreement().trim().isEmpty())
		{
			eCodeOfAgreement = model.getECodeOfAgreement().trim();
		}
//		String eCodeOfAgreement = model.getECodeOfAgreement();// 协议编号

		List<Tg_LoanProjectCountM_View> tg_LoanProjectCountM_ViewList = new ArrayList<Tg_LoanProjectCountM_View>();
		Integer totalPage = 0;
		Integer totalCount = 0;

		Map<String, Object> retmap = new HashMap<String, Object>();
		// System.out.println("掉用存储过程开始：" + System.currentTimeMillis());
		try {
			retmap = tg_LoanProjectCountM_ViewListDao.getLoanProjectCountM_View(usedId,cityRegionId,companyId, projectId, buildId, eCodeOfAgreement,keyword,
					contractSigningDate, endContractSigningDate,preSaleCardDate, endPreSaleCardDate,pageNumber, countPerPage);
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			tg_LoanProjectCountM_ViewList = (List<Tg_LoanProjectCountM_View>) retmap.get("tg_LoanProjectCountM_ViewList");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		properties.put("tg_LoanProjectCountM_ViewList", tg_LoanProjectCountM_ViewList);

		properties.put("keyword", keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
