package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：户入账明细
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_HouseLoanAmount_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -3529177904134579855L;

	@Getter @Setter
	private Long tableId;//主表ID
	@Getter @Setter
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private String companyName;//开发企业
	@Getter @Setter
	private String projectName;//项目名称
	@Getter @Setter
	private String eCodeFroMconstruction;//施工楼幢号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private String unitCode;//单元
	@Getter @Setter
	private String roomId;//房间号
	@Getter @Setter
	private String forEcastArea;//建筑面积
	@Getter @Setter
	private Integer contractStatus;//合同状态
	@Getter @Setter
	private String buyerName;//买受人名称
	@Getter @Setter 
	private String eCodeOfcertificate;//买受人证件号
	@Getter @Setter
	private Double contractSumPrice;//合同总价
	@Getter @Setter
	private Integer paymentMethod;//付款方式
	@Getter @Setter
	private Double loanAmount;//按揭金额
	@Getter @Setter
	private String eCodeOfTripleagreement;//三方协议号
	@Getter @Setter
	private Double loanAmountIn;//入账金额
	@Getter @Setter
	private Integer fundProperty;//资金性质
	@Getter @Setter
	private String reconciliationTSFromOB;//入账日期
	@Getter @Setter 
	private String loanBank;//贷款银行
	@Getter @Setter
	private String theNameOfCreditor;//主借款人姓名
	
	
	//页面接收字段
	@Getter @Setter
	private String endBillTimeStamp;//记账结束日期;
	@Getter @Setter
	private Long companyId;//开发企业ID
	@Getter @Setter
	private Long projectId;//项目ID
	@Getter @Setter
	private Long buildId;//楼幢ID
	
	@Getter @Setter
	private String[] companyNames;//企业名称
}
