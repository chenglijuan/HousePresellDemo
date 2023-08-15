package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：监理报告查询视图
 * */
@ITypeAnnotation(remark="申请表-工程进度节点更新")
public class Qs_BldLimitAmount_View implements Serializable
{

	/**
     *
     */
    private static final long serialVersionUID = -1836076412666062974L;

    //---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private String tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID")
    private Long id;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="联系人")
	private String contact;
	
	@Getter @Setter @IFieldAnnotation(remark="联系方式")
	private String telephone;
	
	@Getter @Setter @IFieldAnnotation(remark="提交时间")
	private Long applyDate;
	
	@Getter @Setter @IFieldAnnotation(remark="业务办理码")
	private String businessCode;
	
	@Getter @Setter @IFieldAnnotation(remark="总包单位")
	private String countUnit;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构")
	private Emmp_CompanyInfo company;
	
	@Getter @Setter @IFieldAnnotation(remark="监理机构名称")
	private String companyName;
	
	@Getter @Setter @IFieldAnnotation(remark="预约时间")
	private Date appointmentDate;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢数")
    private Integer buildCount;
	

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
