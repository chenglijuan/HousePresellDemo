package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：贷款托管合作协议 
 * */
@ITypeAnnotation(remark="托管合作协议")
public class Tgxy_EscrowAgreement implements Serializable,IApprovable
{
	private static final long serialVersionUID = 9063524009065945858L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;
	
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="托管机构")
	private String escrowCompany;

	@Getter @Setter @IFieldAnnotation(remark="协议版本")
	private String agreementVersion;
	
	@Getter @Setter @IFieldAnnotation(remark="托管合作协议编号")
	private String eCodeOfAgreement;//TODO 系统生成，生成规则：YY+MM+DD+区域+两位流水号（按天流水）
	
	@Getter @Setter @IFieldAnnotation(remark="签约申请日期")
	private String contractApplicationDate;
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="开发企业名称")
	private String theNameOfDevelopCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private List<Empj_BuildingInfo> buildingInfoList;
	
	@Getter @Setter @IFieldAnnotation(remark="其它约定事项")
	private String OtherAgreedMatters;
	
	@Getter @Setter @IFieldAnnotation(remark="争议解决方式")
	private String disputeResolution;
	
	@Getter @Setter @IFieldAnnotation(remark="业务流程状态")
	private String businessProcessState;
	
	@Getter @Setter @IFieldAnnotation(remark="协议状态")
	private String agreementState;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢编号-拼接")
	private String buildingInfoCodeList;
	
	@Getter @Setter @IFieldAnnotation(remark="公安编号-拼接")
	private String buildingInfoGabhList;
	
	@Getter @Setter @IFieldAnnotation(remark="托管模式  0-贷款、1-全额")
	private String escrowPattern;
	
	@Getter @Setter @IFieldAnnotation(remark="托管方式 0-按幢、1-按户")
	private String escrowMode;

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
		List<String>  peddingApprovalkey = new ArrayList<>();
		peddingApprovalkey.add("busiState");
		peddingApprovalkey.add("eCode");
		//TODO  存放需要审批的字段
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

	public String geteCodeOfAgreement() {
		return eCodeOfAgreement;
	}

	public void seteCodeOfAgreement(String eCodeOfAgreement) {
		this.eCodeOfAgreement = eCodeOfAgreement;
	}

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	
	

}
