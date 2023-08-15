package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service更新操作：业务对账-日终结算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DayEndBalancingStateUpdateService
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();
		
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime
					.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			model.setBillTimeStamp(billTimeStap);
		}

		Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
		tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStap);
//		tgpf_DayEndBalancingForm.setRecordState(1);
		tgpf_DayEndBalancingForm.setSettlementState(0);
		tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
		
		Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
		
		List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
		if(dayEndBalancingCount > 0)
		{
			tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
		
			for(Tgpf_DayEndBalancing tgpf_DayEndBalancing : tgpf_DayEndBalancingList)
			{				
				// 对账列表
				// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
				Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
				tgpf_BalanceOfAccountForm.setTgxy_BankAccountEscrowed(tgpf_DayEndBalancing.getTgxy_BankAccountEscrowed());
				tgpf_BalanceOfAccountForm.setBillTimeStamp(billTimeStap);
				tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

				Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
						.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));

				List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
				Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();

				// 有记录，为更新
				if (totalCount > 0) {
					tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
							.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
					tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
					
				} 
				
				
				if( (tgpf_BalanceOfAccount.getCenterTotalCount() > 0 || tgpf_DayEndBalancing.getTotalCount() > 0 ) && 0 == tgpf_DayEndBalancing.getRecordState() )
				{
					return MyBackInfo.fail(properties, "请先进行网银对账！");					
				}
							
				// 日终结算状态：未申请
//				if( 0 != tgpf_DayEndBalancing.getSettlementState())
//				{
//					return MyBackInfo.fail(properties, "已申请过日终结算！");
//				}
				// 之前所有的记录必须完成日终结算
//				String billTimeStamp = tgpf_DayEndBalancing.getBillTimeStamp();				
//				Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
//				tgpf_DayEndBalancingForm.setTgxy_BankAccountEscrowed(tgpf_DayEndBalancing.getTgxy_BankAccountEscrowed());
//				tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
//				tgpf_DayEndBalancingForm.setRecordState(0);
//				tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStamp);
//				tgpf_DayEndBalancingForm.setSettlementState(2);
//				
//				Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getQueryHQL(), tgpf_DayEndBalancingForm));
	//
//				if( dayEndBalancingCount >0)
//				{
//					return MyBackInfo.fail(properties, "有未完成日终结算或对账的记录！");
//				}

			}
			for(Tgpf_DayEndBalancing tgpf_DayEndBalancing : tgpf_DayEndBalancingList)
			{									
				tgpf_DayEndBalancing.setTheState(S_TheState.Normal);
				tgpf_DayEndBalancing.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpf_DayEndBalancing.setSettlementState(1);
				
				tgpf_DayEndBalancingDao.save(tgpf_DayEndBalancing);
			}
		}
				
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
