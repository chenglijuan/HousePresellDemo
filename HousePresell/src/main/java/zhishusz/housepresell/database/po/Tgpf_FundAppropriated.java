package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：资金拨付
 * */
@ITypeAnnotation(remark="资金拨付")
public class Tgpf_FundAppropriated implements Serializable,IApprovable
{
	private static final long serialVersionUID = 2565071269609410651L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

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

	@Getter @Setter @IFieldAnnotation(remark="用款申请")
	public Tgpf_FundAppropriated_AF fundAppropriated_AF;

	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="托管可拨付金额")
	private Double canPayAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="资金统筹")
	public Tgpf_FundOverallPlan fundOverallPlan;
	
	@Getter @Setter @IFieldAnnotation(remark="统筹拨付金额")
	private Double overallPlanPayoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="监管账户")
	private Tgpj_BankAccountSupervised bankAccountSupervised;

	@Getter @Setter @IFieldAnnotation(remark="实际拨款日期")
	private String actualPayoutDate;//yyyyMMdd
	
	@IFieldAnnotation(remark="打款单号")
	private String eCodeFromPayoutBill;
	
	@Getter @Setter @IFieldAnnotation(remark="本次实际拨付金额")
	private Double currentPayoutAmount;

	@Getter @Setter @IFieldAnnotation(remark="审批状态")
	private String approvalState;

	@Getter @Setter @IFieldAnnotation(remark="颜色状态")
	private Integer colorState;
	
	@Getter @Setter @IFieldAnnotation(remark="推送状态 0-发起中 1-成功 2-失败")
	private String pushState;
	
	@Getter @Setter @IFieldAnnotation(remark="反馈信息")
	private String pushInfo;

	@Override
	public String getSourceType() {
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
		return null;
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

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}

	public String geteCodeFromPayoutBill() {
		return eCodeFromPayoutBill;
	}

	public void seteCodeFromPayoutBill(String eCodeFromPayoutBill) {
		this.eCodeFromPayoutBill = eCodeFromPayoutBill;
	}
}
