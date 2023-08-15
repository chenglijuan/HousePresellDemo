package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitEmmp_CompanyInfo extends BaseJunitTest 
{
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化企业信息
		Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();
		emmp_CompanyInfo.setTheName("常州五星置业发展有限公司");
		emmp_CompanyInfo.seteCode("JGB000001");
		
		emmp_CompanyInfoDao.save(emmp_CompanyInfo);
	}
}
