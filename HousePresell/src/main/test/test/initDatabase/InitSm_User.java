package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_IDType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_ValidState;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_User extends BaseJunitTest 
{
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化用户
		Sm_User sm_User = new Sm_User();
		sm_User.setAddress("地址");
		sm_User.setBusiState(S_ValidState.Valid);
		sm_User.setCompany(emmp_CompanyInfoDao.findById(2L));
		sm_User.setCreateTimeStamp(System.currentTimeMillis());
		sm_User.setEmail("285557830@qq.com");
		sm_User.setLoginPassword("123456");
		sm_User.setQq("285557830");
		sm_User.setWeixin("15162327456");
		sm_User.setPhoneNumber("15162327456");
		sm_User.setIdType(S_IDType.ResidentIdentityCard);
		sm_User.setRealName("朱周泉");
		sm_User.setTheName("zachary");
		sm_User.setTheState(S_TheState.Normal);
		sm_UserDao.save(sm_User);
	}
}
