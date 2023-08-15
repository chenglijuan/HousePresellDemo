package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：业务对账列表新增操作
 * Company：ZhiShuSZ
 * 查询所有的托管账户，并根据时间查询，表中是否存在该天的记录，如果不存在，则新增，否则，修改
 * */
@Service
@Transactional
public class Tgpf_BalanceOfAccountPreAddService {

	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;	
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("static-access")
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		
		Properties properties = new MyProperties();
		
		long userId=model.getUserId();
		
		String billTimeStap = model.getBillTimeStamp();
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			model.setBillTimeStamp(billTimeStap);
		}
		
		String flag=tgpf_BalanceOfAccountDao.insert_BalanceOfAccount(userId,billTimeStap);		
		
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
//		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put(S_NormalFlag.info, flag);
		
		return properties;
	}
}
