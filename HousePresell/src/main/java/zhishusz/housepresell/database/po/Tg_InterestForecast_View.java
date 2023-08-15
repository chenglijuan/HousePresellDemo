package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：利息预测表
 * */
@ITypeAnnotation(remark="利息预测表")
public class Tg_InterestForecast_View implements Serializable
{

	private static final long serialVersionUID = -7816383309405831569L;
	
	@Getter @Setter @IFieldAnnotation(remark="主表ID")
	private Long tableId;//主表Id
	
	@Getter @Setter @IFieldAnnotation(remark="存款性质 S_DepositPropertyType")
	private String depositProperty;//存款性质 S_DepositPropertyType
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称")
	private String bankName;//银行名称
	
	@Getter @Setter @IFieldAnnotation(remark="登记时间")
	private String registerTime;//登记时间
	
	@Getter @Setter @IFieldAnnotation(remark="存入时间")
	private String startDate;//存入时间
	
	@Getter @Setter @IFieldAnnotation(remark="到期时间")
	private String stopDate;//到期时间
	
	@Getter @Setter @IFieldAnnotation(remark="存款金额")
	private Double principalAmount;//存款金额
	
	@Getter @Setter @IFieldAnnotation(remark="存款期限   期限+时间单位（年，月，日）")
	private String storagePeriod;//存款期限   期限+时间单位（年，月，日）
	
	@Getter @Setter @IFieldAnnotation(remark="利率")
	private Double annualRate;//利率
	
	@Getter @Setter @IFieldAnnotation(remark="浮动区间")
	private String floatAnnualRate;//浮动区间
	
	@Getter @Setter @IFieldAnnotation(remark="利息")
	private Double interest;//利息
	
	@Getter @Setter @IFieldAnnotation(remark="开户证实书")
	private String openAccountCertificate;//开户证实书
}
