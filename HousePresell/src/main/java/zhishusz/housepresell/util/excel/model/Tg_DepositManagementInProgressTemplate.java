package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyString;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Tg_DepositManagementInProgressTemplate implements IExportExcel<Tg_DepositManagement>
{

	MyString myString = MyString.getInstance();

	MyDouble myDouble = MyDouble.getInstance();

	@IFieldAnnotation(remark="存单号")
	private String eCode;

	@Getter @Setter @IFieldAnnotation(remark="存款性质")
	private String depositProperty;

	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private String bankOfDeposit;

	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String escrowAcount;

	@Getter @Setter @IFieldAnnotation(remark="本金金额（元）")
	private String principalAmount;

	@Getter @Setter @IFieldAnnotation(remark="起息日")
	private String startDate;

	@Getter @Setter @IFieldAnnotation(remark="到期日")
	private String stopDate;

	@Getter @Setter @IFieldAnnotation(remark="存款期限")
	private String storagePeriodStr;

	@Getter @Setter @IFieldAnnotation(remark="年利率（%）")
	private String annualRate;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("eCode", "存单号");
		map.put("bankOfDeposit", "开户行");
		map.put("escrowAcount", "托管账户");
		map.put("depositProperty", "存款性质");
		map.put("principalAmount", "正在办理金额（元）");
		map.put("startDate", "办理日期");

		return map;
	}

	@Override
	public void init(Tg_DepositManagement fromClass)
	{
		this.seteCode(fromClass.geteCode());

		String depositProperty = fromClass.getDepositProperty();
		if (depositProperty.equals("01"))
		{
			this.setDepositProperty("大额存单");
		}
		else if (depositProperty.equals("02"))
		{
			this.setDepositProperty("结构性存款");
		}
		else if (depositProperty.equals("03"))
		{
			this.setDepositProperty("保本理财");
		}
		this.setBankOfDeposit(fromClass.getBankOfDeposit().getTheName());
		this.setEscrowAcount(fromClass.getEscrowAcount().getTheName());
		this.setPrincipalAmount(myDouble.pointTOThousandths(myDouble.getShort(fromClass.getPrincipalAmount(), 2)));
		this.setStartDate(MyDatetime.getInstance().dateToSimpleString(fromClass.getStartDate()));

	}

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}