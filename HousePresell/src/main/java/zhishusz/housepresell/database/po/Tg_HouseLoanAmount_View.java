package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：户入账明细报表
 * */
@ITypeAnnotation(remark="户入账明细")
public class Tg_HouseLoanAmount_View implements Serializable
{
	private static final long serialVersionUID = 8280855288329951143L;
	
	@Getter @Setter @IFieldAnnotation(remark="主表ID")
	private Long tableId;//主表ID
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;//记账日期
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String companyName;//开发企业
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称
	@IFieldAnnotation(remark="施工楼幢号")
	private String eCodeFroMconstruction;//施工楼幢号
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter @IFieldAnnotation(remark="单元")
	private String unitCode;//单元
	@Getter @Setter @IFieldAnnotation(remark="房间号")
	private String roomId;//房间号
	@Getter @Setter @IFieldAnnotation(remark="建筑面积")
	private String forEcastArea;//建筑面积
	@Getter @Setter @IFieldAnnotation(remark="合同状态")
	private Integer contractStatus;//合同状态
	@Getter @Setter @IFieldAnnotation(remark="买受人名称")
	private String buyerName;//买受人名称
	@IFieldAnnotation(remark="买受人证件号")
	private String eCodeOfcertificate;//买受人证件号
	@Getter @Setter @IFieldAnnotation(remark="合同总价")
	private Double contractSumPrice;//合同总价
	@Getter @Setter @IFieldAnnotation(remark="付款方式")
	private Integer paymentMethod;//付款方式
	@Getter @Setter @IFieldAnnotation(remark="按揭金额")
	private Double loanAmount;//按揭金额
	@IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleagreement;//三方协议号
	@Getter @Setter @IFieldAnnotation(remark="入账金额")
	private Double loanAmountIn;//入账金额
	@Getter @Setter @IFieldAnnotation(remark="资金性质")
	private Integer fundProperty;//资金性质
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String reconciliationTSFromOB;//入账日期
	@Getter @Setter @IFieldAnnotation(remark="贷款银行")
	private String loanBank;//贷款银行
	@Getter @Setter @IFieldAnnotation(remark="主借款人姓名")
	private String theNameOfCreditor;//主借款人姓名
	
	public String geteCodeFroMconstruction()
	{
		return eCodeFroMconstruction;
	}
	public void seteCodeFroMconstruction(String eCodeFroMconstruction)
	{
		this.eCodeFroMconstruction = eCodeFroMconstruction;
	}
	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	public String geteCodeOfcertificate()
	{
		return eCodeOfcertificate;
	}
	public void seteCodeOfcertificate(String eCodeOfcertificate)
	{
		this.eCodeOfcertificate = eCodeOfcertificate;
	}
	public String geteCodeOfTripleagreement()
	{
		return eCodeOfTripleagreement;
	}
	public void seteCodeOfTripleagreement(String eCodeOfTripleagreement)
	{
		this.eCodeOfTripleagreement = eCodeOfTripleagreement;
	}
	
}
