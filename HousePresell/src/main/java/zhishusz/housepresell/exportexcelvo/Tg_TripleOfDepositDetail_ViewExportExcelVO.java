package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 三方协议入账查询  接受Bean
 * @author ZS004
 */
public class Tg_TripleOfDepositDetail_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号
	
	@Getter @Setter 
	private String depositDatetime;//缴款记账日期
	
	@Getter @Setter 
	private String eCodeOfTripleAgreement;//托管协议号
	
	@Getter @Setter 
	private String eCodeOfContractRecord;//合同备案号
	
	@Getter @Setter 
	private String sellerName;//开发企业
	
	@Getter @Setter 
	private String theNameOfProject;//开发项目
	
	@Getter @Setter 
	private String eCodeFromConstruction;//楼幢编号
	
	@Getter @Setter 
	private String eCodeOfUnit;//单元
	
	@Getter @Setter 
	private String unitRoom;//室号
	
	@Getter @Setter 
	private String contractAmount;//合同金额
	
	@Getter @Setter 
	private String loanAmount;//贷款金额
	
	@Getter @Setter 
	private String buyerName;//购房人
	
	@Getter @Setter 
	private String theNameOfCreditor;//借款人
	
	@Getter @Setter 
	private String idType;//证件类型 S_IDType
	
	@Getter @Setter 
	private String idNumber;//证件号码
	
	@Getter @Setter 
	private String bankAccountForLoan;//银行账号（贷）
	
	@Getter @Setter 
	private String fundProperty;//资金性质
	
	@Getter @Setter 
	private String bankBranchName;//开户行
	
	@Getter @Setter 
	private String theAccountOfBAE;//托管账户
	
	@Getter @Setter 
	private String remarkFromDepositBill;//缴款记账备注
}
