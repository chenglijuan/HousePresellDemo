package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：利息预测表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_InterestForecast_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = 5326983713788521052L;
	
	@Getter @Setter 
	private Long tableId;//主表Id
	
	@Getter @Setter 
	private String depositProperty;//存款性质 S_DepositPropertyType
	
	@Getter @Setter
	private String bankName;//银行名称
	
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
	private Double floatAnnualRate;//浮动区间
	
	@Getter @Setter
	private Double interest;//利息
	
	@Getter @Setter 
	private String openAccountCertificate;//开户证实书
	
	
	//接收页面传值
	@Getter @Setter 
	private String querDate;//查询时间 格式：2018
	
	@Getter @Setter 
	private String loanInDate;//存入时间
		
	@Getter @Setter 
	private String endLoanInDate;//到期时间
}
