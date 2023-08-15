package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * 业务对账-日终结算-后台任务
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DayEndBalancingTaskService
{
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao; // 楼幢信息表
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户	
	@Autowired
	private Tgpf_DayEndBalancingManualTaskService tgpf_DayEndBalancingManualTaskService;// 楼幢账户
	
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();

	public Properties execute(Tgpf_DayEndBalancingForm model)
	{
		
		Properties properties = new MyProperties();

		Sm_User userStart = model.getUser(); // admin
		
		String billTimeStap = model.getBillTimeStamp();
		
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			model.setBillTimeStamp(billTimeStap);
		}
		// 查询日终结算表
		// 查询条件：1.当前日期前一天 2.已对账 3. 已经申请日终结算 4. 总笔数大于1
		Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
		tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStap);
		tgpf_DayEndBalancingForm.setRecordState(1);
		tgpf_DayEndBalancingForm.setSettlementState(1);
		tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
		
		Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
		
		List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
		if(dayEndBalancingCount > 0)
		{
			tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getBalancingHQL(), tgpf_DayEndBalancingForm));
			
			Long[] idArr =new Long[tgpf_DayEndBalancingList.size()];
			
			Tgpf_DayEndBalancingForm dayEndBalancingForm = new Tgpf_DayEndBalancingForm();
			
			int index = 0 ;
			for(Tgpf_DayEndBalancing tgpf_DayEndBalancing : tgpf_DayEndBalancingList)
			{
				idArr[index] = tgpf_DayEndBalancing.getTableId();
				index ++;
			}
			
			dayEndBalancingForm.setIdArr(idArr);
			dayEndBalancingForm.setBillTimeStamp(billTimeStap);
			dayEndBalancingForm.setUser(userStart);
			
			tgpf_DayEndBalancingManualTaskService.execute(dayEndBalancingForm);
		
		}	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}

}
