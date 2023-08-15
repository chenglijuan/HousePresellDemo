package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：利息预测表
 * */
public class Tg_InterestForecast_ViewExportExcelVO 
{
	
	@Getter	@Setter
	private Integer ordinal;// 序号
	
	@Getter @Setter 
	private String depositProperty;//存款性质 S_DepositPropertyType
	
	@Getter @Setter
	private String bankName;//银行名称(存款银行)
	
	@Getter @Setter 
	private String registerTime;//登记时间
	
	@Getter @Setter
	private String startDate;//存入时间
	
	@Getter @Setter
	private String stopDate;//到期时间
	
	@Getter @Setter
	private Double principalAmount;//存款金额
	
	@Getter @Setter 
	private String storagePeriod;//存款期限   期限+时间单位（年，月，日）
	
	@Getter @Setter
	private Double annualRate;//利率
	
	@Getter @Setter 
	private String floatAnnualRate;//浮动区间
	
	@Getter @Setter 
	private Double interest;//利息
	
	@Getter @Setter
	private String openAccountCertificate;//开户证实书
}
