package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/*
 * Form表单：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BldEscrowCompletedForm extends NormalActionForm
{
	private static final long serialVersionUID = 4601007676616178024L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//项目编号
	@Getter @Setter
	private String eCodeFromDRAD;//交付备案批准文件号
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Empj_BldEscrowCompleted_DtlForm[] empj_BldEscrowCompletedAddDtltab; //托管终止明细
	@Getter @Setter
	private Long[] intersectionBuildingIdArr;//物价备案价格变更、托管面积变更、退房退款、监管账号变更(无，忽略)、受限额度变更符合托管终止条件并集
	@Getter @Setter
	private Long empj_BldEscrowCompleted_DtlId;//托管终止明细ID
	@Getter @Setter
	private String getDetailType; //获取详情类型（1、详情信息带审批流，2、详情信息不带审批流）
	@Getter @Setter
	private String isSign;//是否过滤签章
	@Getter @Setter
	public String webSite;//公示网址
	@Getter @Setter
	public String hasFormula;//是否已公示
	@Getter @Setter
	public String hasPush;//是否已推送
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfDevelopCompany()
	{
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany)
	{
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfProject()
	{
		return eCodeOfProject;
	}
	public void seteCodeOfProject(String eCodeOfProject)
	{
		this.eCodeOfProject = eCodeOfProject;
	}
	public String geteCodeFromDRAD()
	{
		return eCodeFromDRAD;
	}
	public void seteCodeFromDRAD(String eCodeFromDRAD)
	{
		this.eCodeFromDRAD = eCodeFromDRAD;
	}
}
