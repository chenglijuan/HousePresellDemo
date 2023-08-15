package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管楼幢查询报表  导出Excel
 * @author ZS004
 */
public class Tg_Build_ViewExportExcelVO
{
	@Getter @Setter 
	private Integer ordinal;//序号
	@Getter @Setter
	private String billTimeStamp;//记账日期 
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter
	private String projectName;//项目名称  
	@Getter @Setter
	private String eCodeFroMconstruction;//楼幢  
	@Getter @Setter
	private String unitCode;//单元信息  
	@Getter @Setter
	private String roomId;//房间号 
	@Getter @Setter
	private String forEcastArea;//户建筑面积  
	@Getter @Setter
	private String contractStatus;//合同状态  
	@Getter @Setter
	private String contractNo;//合同编号   
	@Getter @Setter
	private String eCodeOfTripleagreement;//三方协议号  
	@Getter @Setter
	private String buyerName;//承购人姓名 
	@Getter @Setter
	private String eCodeOfCertificate;//证件号  
	@Getter @Setter
	private String theNameOfCreditor;//借款人姓名  
	@Getter @Setter
	private Double contractSumPrice;//合同总价 
	@Getter @Setter
	private String paymentMethod;//付款方式 
	@Getter @Setter
	private Double firstPaymentAmount;//首付款金额  
	@Getter @Setter
	private Double loanAmount;//合同贷款金额 
	@Getter @Setter
	private String escrowState;//托管状态 
	@Getter @Setter
	private String contractSignDate;//合同签订日期  
	@Getter @Setter
	private Double loanAmountIn;//贷款入账金额
	@Getter @Setter
	private Double theAmountOfRetainedequity;//留存权益   
	@Getter @Setter
	private String statementState;//对账状态  
}
