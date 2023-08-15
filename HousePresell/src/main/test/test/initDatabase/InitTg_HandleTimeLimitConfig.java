package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitTg_HandleTimeLimitConfig extends BaseJunitTest 
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;
	
	private static final String[] Types = new String[] {"合作协议", "三方协议", "受限额度变更", "托管终止", "资金归集", "托管资金一般拨付", "退房退款", "支付保证"};
	private static final String[] Standards = new String[] {"提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成", "入账到日终结算完成", "提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成"};
	private static final int[] LimitDayNumber = new int[] {1, 3, 3, 3, 3, 5, 1, 3};
	private static final String[] Departments = new String[] {"项目部分", "项目部分", "财务部分", "项目部分", "财务部分", "财务部分", "项目部分", "项目部分"};
	
	@Test
	public void execute() throws Exception 
	{
		for (int i = 0; i < Types.length; i++) {
			Tg_HandleTimeLimitConfig tg_HandleTimeLimitConfig = new Tg_HandleTimeLimitConfig();
			tg_HandleTimeLimitConfig.setTheState(0);
			tg_HandleTimeLimitConfig.setTheType(Types[i]);
			tg_HandleTimeLimitConfig.setCompletionStandard(Standards[i]);
			tg_HandleTimeLimitConfig.setLimitDayNumber(LimitDayNumber[i]);
//			tg_HandleTimeLimitConfig.setCounterpartDepartment(Departments[i]);
			tg_HandleTimeLimitConfig.setLastCfgTimeStamp(System.currentTimeMillis());
			tg_HandleTimeLimitConfigDao.save(tg_HandleTimeLimitConfig);
		}
		
	}
}
