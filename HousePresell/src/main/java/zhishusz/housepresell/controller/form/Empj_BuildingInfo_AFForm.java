package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BuildingInfo_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = 1803666890147079093L;
	
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
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private Double buildingArea;//建筑面积（㎡）
	@Getter @Setter
	private Double escrowArea;//托管面积
	@Getter @Setter
	private String deliveryType;//交付类型
	@Getter @Setter
	private Double upfloorNumber;//地上楼层数
	@Getter @Setter
	private Double downfloorNumber;//地下楼层数
	@Getter @Setter
	private Integer landMortgageState;//土地抵押状态
	@Getter @Setter
	private String landMortgagor;//土地抵押权人
	@Getter @Setter
	private Double landMortgageAmount;//土地抵押金额
	@Getter @Setter
	private String remark;//备注
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
	public String geteCodeOfBuilding()
	{
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding)
	{
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}
