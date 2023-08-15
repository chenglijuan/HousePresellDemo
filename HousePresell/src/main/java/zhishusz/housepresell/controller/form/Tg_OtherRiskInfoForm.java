package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：其他风险信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_OtherRiskInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = -4053442508361742389L;
	
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
	private Sm_CityRegionInfo cityRegion;//所属区域
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id
	@Getter @Setter
	private String theNameOfCityRegion;//区域名称
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
	private String riskInputDate;//风险输入日期yyyyMMdd
	@Getter @Setter
	private String riskInfo;//其他风险信息
	@Getter @Setter
	private Boolean isUsed;//是否录用
	@Getter @Setter
	private String remark;//备注
	
	//附件参数
	@Getter @Setter
	private String smAttachmentList;
	
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
	
	
}
