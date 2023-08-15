package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.state.S_TheState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmpj_ProjectInfo extends BaseJunitTest 
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化项目信息
		Empj_ProjectInfo empj_ProjectInfo = new Empj_ProjectInfo();
		empj_ProjectInfo.setTheName("绿城玉兰广场");
		empj_ProjectInfo.seteCode("JG80101010");
		empj_ProjectInfo.setTheState(S_TheState.Normal);
		empj_ProjectInfo.setCreateTimeStamp(System.currentTimeMillis());
		empj_ProjectInfo.setDevelopCompany(emmp_CompanyInfoDao.findById(10016L));
		empj_ProjectInfo.setCityRegion(sm_CityRegionInfoDao.findById(10004L));
		empj_ProjectInfo.setAddress("测试地址");
		empj_ProjectInfo.setContactPerson("测试联系人");
		empj_ProjectInfo.setContactPhone("测试联系电话");
		empj_ProjectInfoDao.save(empj_ProjectInfo);
	}
}
