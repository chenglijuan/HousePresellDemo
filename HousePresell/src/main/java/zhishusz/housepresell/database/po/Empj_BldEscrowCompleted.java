package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：申请表-项目托管终止（审批）-主表
 * */
@ITypeAnnotation(remark="申请表-项目托管终止（审批）-主表")
public class Empj_BldEscrowCompleted implements Serializable, ILogable, IApprovable
{
	private static final long serialVersionUID = 4547987583246363835L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;

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
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
		
	@IFieldAnnotation(remark="项目编号")
	public String eCodeOfProject;
	
	@IFieldAnnotation(remark="交付备案批准文件号")
	private String eCodeFromDRAD;//交付备案批准文件号，DRAD：DeliveryForRecordApprovalDocument
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	public String remark;

	
	@Getter @Setter @IFieldAnnotation(remark="公式网址")
	public String webSite;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已公示（0-否 1-是）")
	public String hasFormula;
	
	@Getter @Setter @IFieldAnnotation(remark="是否推送（0-否 1-是）")
	public String hasPush;
	
	@Getter @Setter @IFieldAnnotation(remark="托管终止楼幢列表")
	public List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList;

	@Override
	public String getSourceType()
	{
		return getClass().getName();
	}

	@Override
	public Long getSourceId()
	{
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey()
	{
		List<String> peddingApprovalkey = new ArrayList<String>();
		peddingApprovalkey.add("remark");
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

	public String geteCodeFromDRAD() {
		return eCodeFromDRAD;
	}

	public void seteCodeFromDRAD(String eCodeFromDRAD) {
		this.eCodeFromDRAD = eCodeFromDRAD;
	}

	@Override
	public Long getLogId() {
		return null;
	}

	@Override
	public void setLogId(Long logId) {

	}

	@Override
	public String getLogData() {
		return null;
	}
}
