package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢查询报表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="托管楼幢明细")
public class Tg_Build_View  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;//记账日期 
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String companyName;//开发企业 
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称  
	@Getter @Setter @IFieldAnnotation(remark="楼幢")
	private String eCodeFroMconstruction;//楼幢  
	@Getter @Setter @IFieldAnnotation(remark="单元信息")
	private String unitCode;//单元信息  
	@Getter @Setter @IFieldAnnotation(remark="房间号")
	private String roomId;//房间号 
	@Getter @Setter @IFieldAnnotation(remark="户建筑面积")
	private String forEcastArea;//户建筑面积  
	@Getter @Setter @IFieldAnnotation(remark="合同状态")
	private Integer contractStatus;//合同状态  
	@Getter @Setter @IFieldAnnotation(remark="合同编号")
	private String contractNo;//合同编号   
	@Getter @Setter @IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleagreement;//三方协议号  
	@Getter @Setter @IFieldAnnotation(remark="承购人姓名")
	private String buyerName;//承购人姓名 
	@Getter @Setter @IFieldAnnotation(remark="证件号")
	private String eCodeOfCertificate;//证件号  
	@Getter @Setter @IFieldAnnotation(remark="借款人姓名")
	private String theNameOfCreditor;//借款人姓名  
	@Getter @Setter @IFieldAnnotation(remark="合同总价")
	private Double contractSumPrice;//合同总价 
	@Getter @Setter @IFieldAnnotation(remark="付款方式")
	private Integer paymentMethod;//付款方式 
	@Getter @Setter @IFieldAnnotation(remark="首付款金额")
	private Double firstPaymentAmount;//首付款金额  
	@Getter @Setter @IFieldAnnotation(remark="合同贷款金额")
	private Double loanAmount;//合同贷款金额 
	@Getter @Setter @IFieldAnnotation(remark="托管状态")
	private Integer escrowState;//托管状态 
	@Getter @Setter @IFieldAnnotation(remark="合同签订日期")
	private String contractSignDate;//合同签订日期  
	@Getter @Setter @IFieldAnnotation(remark="贷款入账金额")
	private Double loanAmountIn;//贷款入账金额
	@Getter @Setter @IFieldAnnotation(remark="留存权益")
	private Double theAmountOfRetainedequity;//留存权益   
	@Getter @Setter @IFieldAnnotation(remark="对账状态")
	private Integer statementState;//对账状态  
	
	
	public String geteCodeFroMconstruction()
	{
		return eCodeFroMconstruction;
	}
	public void seteCodeFroMconstruction(String eCodeFroMconstruction)
	{
		this.eCodeFroMconstruction = eCodeFroMconstruction;
	}
	public String geteCodeOfTripleagreement()
	{
		return eCodeOfTripleagreement;
	}
	public void seteCodeOfTripleagreement(String eCodeOfTripleagreement)
	{
		this.eCodeOfTripleagreement = eCodeOfTripleagreement;
	}
	public String geteCodeOfCertificate()
	{
		return eCodeOfCertificate;
	}
	public void seteCodeOfCertificate(String eCodeOfCertificate)
	{
		this.eCodeOfCertificate = eCodeOfCertificate;
	} 

	
	
}
