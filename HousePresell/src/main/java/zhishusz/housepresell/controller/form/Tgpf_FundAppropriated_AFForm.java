package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;

import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundAppropriated_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = 7973756676928288742L;
	
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
	private String theNameOfDevelopCompany; //开发企业名称
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//项目编号
	@Getter @Setter
	private String applyDate;//用款申请日期
	@Getter @Setter
	private String fundOverallPlanApplyDate;//用款申请日期（新增统筹）
	@Getter @Setter
	private String fundAppropriatedApplyDate;//用款申请日期（资金拨付）
	@Getter @Setter
	private Long startTimeStamp;//用款申请起始日期
	@Getter @Setter
	private Long endTimeStamp;//用款申请终止日期
	@Getter @Setter
	private String fundOverallPlanDate;//统筹日期
	@Getter @Setter
	private Integer applyState;//申请状态
	@Getter @Setter 
    private String applyType;//"申请类型（0或空：一般支付 1：保函支付）"
	//----------申请单状态---------- //
	@Getter @Setter
	private Integer payoutState1;// 4
	@Getter @Setter
	private Integer payoutState2;// 5
	@Getter @Setter
	private Integer payoutState3;// 6
	//----------申请单状态---------- //
	@Getter @Setter
	private Long fundOverallPlanId;//资金统筹-Id
	@Getter @Setter
	private Double totalApplyAmount;//本次申请总金额
	@Getter @Setter
	private Tgpf_FundAppropriated_AFDtlForm[] tgpf_FundAppropriated_AFAddtab; //用款申请明细
	@Getter @Setter
	private Tgpf_FundOverPlanDetaillForm[] tgpf_FundOverPlanDetailltab; //用款申请明细汇总
	
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
