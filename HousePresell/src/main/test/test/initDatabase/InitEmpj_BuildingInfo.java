package test.initDatabase;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_BuildingInfo extends BaseJunitTest 
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化楼幢信息
		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), new BaseForm()));
		for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
		{
			empj_BuildingInfo.seteCodeFromConstruction("施工编号");
			empj_BuildingInfo.setTheNameFromPresellSystem("预售项目名称");
			empj_BuildingInfo.seteCodeFromPublicSecurity("公安编号");
			empj_BuildingInfo.seteCodeFromPresellCert("预售证号");
			empj_BuildingInfo.setUpfloorNumber(3.0);
			empj_BuildingInfo.setDeliveryType("交付类型");
			empj_BuildingInfo.setEscrowStandard("托管标准");
			empj_BuildingInfoDao.save(empj_BuildingInfo);
		}
	}
}
