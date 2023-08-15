package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service列表查询：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingInfoSuperviseListService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_projectInfoDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_bankAccountSupervisedDao;


	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Long tgpj_bankAccountSupervisedId=model.getTableId();
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		Tgpj_BankAccountSupervised tgpj_bankAccountSupervised = tgpj_bankAccountSupervisedDao.findById(tgpj_bankAccountSupervisedId);
//		List<Empj_BuildingInfo> buildingInfoList = tgpj_bankAccountSupervised.getBuildingInfoList();
//		String keyword = model.getKeyword();
//		Integer totalCount=buildingInfoList.size();
//		Integer totalPage = totalCount / countPerPage;


//		Empj_BuildingInfoForm empj_buildingInfoForm=new Empj_BuildingInfoForm();
//		empj_buildingInfoForm.setbank
////		Integer totalCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getBasicHQL(), model));
//
//		Integer totalPage = totalCount / countPerPage;
//		if (totalCount % countPerPage > 0) totalPage++;
//		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
//
//		List<Empj_BuildingInfo> empj_BuildingInfoList;
//		List<Tgpj_BankAccountSupervised> tgpj_bankAccountSupervisedList;
//		if(totalCount > 0)
//		{
//			buildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), model), pageNumber, countPerPage);
//		}
//		else
//		{
//			buildingInfoList = new ArrayList<Empj_BuildingInfo>();
//		}

//		properties.put("empj_BuildingInfoList", buildingInfoList);
//		properties.put(S_NormalFlag.keyword, keyword);
//		properties.put(S_NormalFlag.totalPage, totalPage);
//		properties.put(S_NormalFlag.pageNumber, pageNumber);
//		properties.put(S_NormalFlag.countPerPage, countPerPage);
//		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
