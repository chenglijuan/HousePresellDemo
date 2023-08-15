package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：申请表-工程进度节点更新
 * */
@ITypeAnnotation(remark="申请表-工程进度节点更新")
public class Empj_BldLimitAmount implements Serializable,IApprovable
{
	
	private static final long serialVersionUID = -1882102379385569420L;

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
	private String eCode;

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

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	//工作时限办理
	@Getter @Setter @IFieldAnnotation(remark="受理时间")
	private Long acceptTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="受理说明")
	private String acceptExplain;

	@Getter @Setter @IFieldAnnotation(remark="预约时间")
	private Long appointTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="预约说明")
	private String appointExplain;

	@Getter @Setter @IFieldAnnotation(remark="现场勘查时间")
	private Long sceneInvestigationTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="现场勘查说明")
	private String sceneInvestigationExplain;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人A")
	private String contactOne;
	
	@Getter @Setter @IFieldAnnotation(remark="A联系方式")
	private String telephoneOne;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人B")
	private String contactTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="B联系方式")
	private String telephoneTwo;
	
	
	@Getter @Setter @IFieldAnnotation(remark="回传时间A")
    private String returnTimeOne;
	
	@Getter @Setter @IFieldAnnotation(remark="回传时间B")
    private String returnTimeTwo;
    
    @Getter @Setter @IFieldAnnotation(remark="任务分配时间A")
    private String assignTasksTimeOne;
    
    @Getter @Setter @IFieldAnnotation(remark="任务分配时间B")
    private String assignTasksTimeTwo;
    
    @Getter @Setter @IFieldAnnotation(remark="签到时间A")
    private String signTimeOne;
    
    @Getter @Setter @IFieldAnnotation(remark="签到时间B")
    private String signTimeTwo;
    
    
	
	@Getter @Setter @IFieldAnnotation(remark="提交时间")
	private Long applyDate;
	
	@Getter @Setter @IFieldAnnotation(remark="业务办理码")
	private String businessCode;
	
	@Getter @Setter @IFieldAnnotation(remark="总包单位")
	private String countUnit;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构A")
	private Emmp_CompanyInfo companyOne;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构A名称")
	private String companyOneName;
	
	@Getter @Setter @IFieldAnnotation(remark="预约时间A")
	private Date appointmentDateOne;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构B")
	private Emmp_CompanyInfo companyTwo;
    
	@Getter @Setter @IFieldAnnotation(remark="监理机构B名称")
    private String companyTwoName;
    
	@Getter @Setter @IFieldAnnotation(remark="预约时间B")
    private Date appointmentDateTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢数")
    private Integer buildCount;
	
	@Getter @Setter @IFieldAnnotation(remark="上传人A")
    private String uploadOne;
	
	@Getter @Setter @IFieldAnnotation(remark="上传人B")
    private String uploadTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="审核A")
    private String approvalOne;
    
    @Getter @Setter @IFieldAnnotation(remark="审核B")
    private String approvalTwo;
	
	@Getter @Setter @IFieldAnnotation(remark="子表信息")
	private List<Empj_BldLimitAmount_Dtl> dtlList;

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
		List<String> peddingApprovalkey = new ArrayList<String>();

		peddingApprovalkey.add("expectFigureProgressId");
		peddingApprovalkey.add("remark");
		peddingApprovalkey.add("generalAttachmentList");
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

	public List getNeedFieldList(){
		return Arrays.asList("eCode");
	}

}
