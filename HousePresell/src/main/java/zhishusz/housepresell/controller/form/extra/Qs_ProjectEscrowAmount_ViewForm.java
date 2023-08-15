package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：项目托管资金收支情况表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Qs_ProjectEscrowAmount_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = 3811304996518989739L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="流水号")
	private String serialNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String recordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢（施工楼幢）")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="放款户数")
	private Integer loansCountHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;
	
	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="余额")
	private Double currentFund;
	
	@Getter @Setter @IFieldAnnotation(remark="溢出金额")
	private Double spilloverAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remarkNote;
	
	@Getter @Setter @IFieldAnnotation(remark="入账起始日期")
	private String recordDateStart;
	
	@Getter @Setter @IFieldAnnotation(remark="入账结束日期")
	private String recordDateEnd;

	//接收页面传递的参数
	@Getter @Setter@IFieldAnnotation(remark="区域Id")
	private String cityRegionId;//区域Id

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	
}
