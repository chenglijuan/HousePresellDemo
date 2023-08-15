package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：存单管理
 */
@ITypeAnnotation(remark="存单管理")
public class Tg_DepositManagement implements Serializable,IApprovable
{
	private static final long serialVersionUID = 2820716076598287742L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String approvalState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	
	@Getter @Setter @IFieldAnnotation(remark="存单状态 S_DepositState")
	private String depositState;

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="存款性质 S_DepositPropertyType")
	private String depositProperty;
	
	@Getter @Setter @IFieldAnnotation(remark="存款银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private Emmp_BankBranch bankOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private Tgxy_BankAccountEscrowed escrowAcount;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户简称")
	private String escrowAcountShortName;
	
	@Getter @Setter @IFieldAnnotation(remark="经办人")
	private String agent;
	
	@Getter @Setter @IFieldAnnotation(remark="本金金额（元）")
	private Double principalAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="存期")
	private Integer storagePeriod;

	@Getter @Setter @IFieldAnnotation(remark="存期单位 S_StoragePeriodCompanyType")
	private String storagePeriodCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="年利率（%）")
	private Double annualRate;
	
	@Getter @Setter @IFieldAnnotation(remark="开始日期")
	private Long startDate;
	
	@Getter @Setter @IFieldAnnotation(remark="截至日期")
	private Long stopDate;
	
	@Getter @Setter @IFieldAnnotation(remark="开户证实书")
	private String openAccountCertificate;
	
	@Getter @Setter @IFieldAnnotation(remark="预计利息")
	private Double expectedInterest;
	
	@Getter @Setter @IFieldAnnotation(remark="浮动年利率（%）")
	private String floatAnnualRate;
//	private Double floatAnnualRate;

	@Getter @Setter @IFieldAnnotation(remark="提取日期")
	private Long extractDate;
	
	@Getter @Setter @IFieldAnnotation(remark="实际利息")
	private Double realInterest;
	
	@Getter @Setter @IFieldAnnotation(remark="实际利率")
	private Double realInterestRate;
	
	@Getter @Setter @IFieldAnnotation(remark="计算规则")
	private Integer calculationRule;
	
	@Getter @Setter @IFieldAnnotation(remark="备注1")
	private String remarkIn;
	
	@Getter @Setter @IFieldAnnotation(remark="备注2")
	private String remarkOut;
	
	@Getter @Setter @IFieldAnnotation(remark="存入推送状态 0-未推送 1-已推送")
	private String stateIn;
	
	@Getter @Setter @IFieldAnnotation(remark="提取推送状态 0-未推送 1-已推送")
	private String stateOut;

	@Override
	public String getSourceType()
	{
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String>  peddingApprovalkey = new ArrayList<>();

		/*if (!S_DepositState.InProgress.equals(this.depositState))
		{
			peddingApprovalkey.add("extractDateStr");
			peddingApprovalkey.add("realInterest");
			peddingApprovalkey.add("realInterestRate");
		}

		peddingApprovalkey.add("generalAttachmentList");*/
		peddingApprovalkey.add("eCode");

		//TODO  存放可变更的字段
		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
