package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.dao.Tg_AccountabilityEnquiryDao;
import zhishusz.housepresell.database.po.Tg_AccountabilityEnquiry;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder : 按权责发生制查询利息情况统计表
 * */
@Service
public class Tg_AccountabilityEnquiryListRebuild extends RebuilderBase<Tg_AccountabilityEnquiry>
{
	
	@Autowired 
	private Tg_AccountabilityEnquiryDao tg_AccountabilityEnquiryDao ;
	
	@Override
	public Properties getSimpleInfo(Tg_AccountabilityEnquiry object)
	{

		if (object == null)
			return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		properties.put("bank", object.getBank());//存款银行
		properties.put("bankOfDeposit", object.getBankOfDeposit());//开户行
		properties.put("depositProperty", object.getDepositProperty());//存款性质
		properties.put("escrowAccount", object.getEscrowAccount());//托管账号
		properties.put("escrowAcountName", object.getEscrowAcountName());//托管账号名称
		properties.put("recordDate", object.getRecordDate());//登记日期
		properties.put("loadTime", object.getLoadTime());//存入时间
		
		properties.put("expirationTime", object.getExpirationTime());//到期时间
		properties.put("amountDeposited", object.getAmountDeposited());//存款金额
		properties.put("depositCeilings", object.getDepositCeilings());//存款期限
		properties.put("interestRate", object.getInterestRate());//利率
		properties.put("fate", object.getFate());//天数
		properties.put("interest", object.getInterest());//利息
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_AccountabilityEnquiry object)
	{
		
		return null;
	}

}















