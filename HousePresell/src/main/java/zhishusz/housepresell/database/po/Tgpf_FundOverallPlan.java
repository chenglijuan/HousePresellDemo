package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：资金统筹
 * TODO：其他字段待补充
 * */
@ITypeAnnotation(remark="资金统筹")
public class Tgpf_FundOverallPlan implements Serializable,IApprovable
{
	private static final long serialVersionUID = 5058992213740881910L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态(统筹状态：值为：“已保存”，“已统筹”)")
	private String busiState;//1-已保存（初始状态）,2-已统筹（提交审批状态）
	
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
	@Getter @Setter @IFieldAnnotation(remark="上一天的网银日期")
	private Long lastCyberBankBillTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="统筹日期")
	private String fundOverallPlanDate;

	@Getter @Setter @IFieldAnnotation(remark="用款申请列表")
	private List<Tgpf_FundAppropriated_AF> fundAppropriated_AFList;

	@Getter @Setter @IFieldAnnotation(remark="用款计划列表")
	private List<Tgpf_FundOverallPlanDetail> fundOverallPlanDetailList;

	@Getter @Setter @IFieldAnnotation(remark="拨付计划")
	private List<Tgpf_FundAppropriated> fundAppropriatedList;
	
	@Getter @Setter @IFieldAnnotation(remark="审批状态（“待提交”、“审核中”、“已完结”）")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="申请类型（0或空：一般支付 1：保函支付）")
    private String applyType;
	
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
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		return null;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
