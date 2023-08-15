package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：申请-用款-主表
 * */
@ITypeAnnotation(remark="申请-用款-主表")
public class Tgpf_FundAppropriated_AF implements Serializable,IApprovable
{
	private static final long serialVersionUID = 8094078496331915670L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号 - 冗余")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="开发企业名称 - 冗余")
	private String theNameOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@IFieldAnnotation(remark="项目编号")
	public String eCodeOfProject;

	@Getter @Setter @IFieldAnnotation(remark="申请总金额")
	public Double totalApplyAmount;

	@Getter @Setter @IFieldAnnotation(remark="用款申请日期")
	private String applyDate;

	@Getter @Setter @IFieldAnnotation(remark="申请状态")
	private Integer applyState;
	/*
	【申请状态-枚举：
		1-初始（保存）
		2-已提交（点击提交）
		3-已受理（财务人员初审后）
		4-受理退回（财务人员审批不通过）
		5-已统筹（财务人员统筹后）
		6-已审批（财务总监已审批）
		7-统筹驳回（财务总监审批不通过）
	】
	*/

	@Getter @Setter @IFieldAnnotation(remark="审批状态")
	private String approvalState;

	@Getter @Setter @IFieldAnnotation(remark="关联资金统筹")
	private Tgpf_FundOverallPlan fundOverallPlan;

	@Getter @Setter @IFieldAnnotation(remark="明细列表")
	private List<Tgpf_FundAppropriated_AFDtl> fundAppropriated_AFDtlList;

	@Getter @Setter @IFieldAnnotation(remark="用款申请汇总信息")
	private List<Tgpf_FundOverallPlanDetail> fundOverallPlanDetailList;

	@Getter @Setter @IFieldAnnotation(remark="拨付计划")
	private List<Tgpf_FundAppropriated> fundAppropriatedList;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	public String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="申请类型（0或空：一般支付 1：保函支付）")
    private String applyType;
	
	@Getter
    @Setter
    @IFieldAnnotation(remark = "关联支付保函Id")
    private Long paymentBondId;
	
	@Getter
    @Setter
    @IFieldAnnotation(remark = "关联支付保函编号")
    private String paymentBondCode;

	//payState为1 表示是风险项目
	@Getter @Setter @IFieldAnnotation(remark="支付状态")
	private String payState;


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

	public String geteCodeOfProject() {
		return eCodeOfProject;
	}

	public void seteCodeOfProject(String eCodeOfProject) {
		this.eCodeOfProject = eCodeOfProject;
	}
}
