package zhishusz.housepresell.initialize;

import zhishusz.housepresell.database.dao.Tg_HandleTimeLimitConfigDao;
import zhishusz.housepresell.database.po.Tg_HandleTimeLimitConfig;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/*
 * Service:工作时间检查配置初始化业务代码
 * */
@Service
@Transactional
public class Tg_HandleTimeLimitConfigInitService
{
	@Autowired
	private Tg_HandleTimeLimitConfigDao tg_HandleTimeLimitConfigDao;

	private static final String[] Types = new String[] {"合作协议", "三方协议", "受限额度变更", "托管终止", "资金归集", "托管资金一般拨付", "退房退款", "支付保证"};
	private static final String[] Standards = new String[] {"提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成", "入账到日终结算完成", "提交申请至审核完成", "提交申请至审核完成", "提交申请至审核完成"};
	private static final int[] LimitDayNumber = new int[] {1, 3, 3, 3, 3, 5, 1, 3};

	public void Step5_HandleTimeLimitConfig()
	{
		for (int i = 0; i < Types.length; i++) {
			Tg_HandleTimeLimitConfig tg_HandleTimeLimitConfig = new Tg_HandleTimeLimitConfig();
			tg_HandleTimeLimitConfig.setTheState(S_TheState.Normal);
			tg_HandleTimeLimitConfig.setTheType(Types[i]);
			tg_HandleTimeLimitConfig.setCompletionStandard(Standards[i]);
			tg_HandleTimeLimitConfig.setLimitDayNumber(LimitDayNumber[i]);
			tg_HandleTimeLimitConfig.setRole(null);
			tg_HandleTimeLimitConfig.setLastCfgTimeStamp(System.currentTimeMillis());
			tg_HandleTimeLimitConfigDao.save(tg_HandleTimeLimitConfig);
		}
	}
}
