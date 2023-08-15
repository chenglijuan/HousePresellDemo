package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_BuildingRemainRightLogForm extends NormalActionForm
{
	private static final long serialVersionUID = -8120679683538808679L;
	
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
	private Long developCompanyId;//开发企业-Id
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//项目编号-冗余
	@Getter @Setter
	private Empj_BuildingInfo building;//所属楼栋
	@Getter @Setter
	private Long buildingId;//所属楼栋-Id
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private Tgpj_BuildingAccount buildingAccount;//关联楼幢账户-冗余
	@Getter @Setter
	private Long buildingAccountId;//关联楼幢账户-冗余-Id
	@Getter @Setter
	private Empj_BuildingExtendInfo buildingExtendInfo;//关联楼幢扩展信息-冗余
	@Getter @Setter
	private Long buildingExtendInfoId;//关联楼幢扩展信息-冗余-Id
	@Getter @Setter
	private String currentFigureProgress;//当前形象进度
	@Getter @Setter
	private Double currentLimitedRatio;//当前受限比例（%）
	@Getter @Setter
	private Double nodeLimitedAmount;//节点受限额度（元）
	@Getter @Setter
	private Double totalAccountAmount;//总入账金额（元）
	@Getter @Setter
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private String srcBusiType;//来源业务类型
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfProject()
	{
		return eCodeOfProject;
	}
	public void seteCodeOfProject(String eCodeOfProject)
	{
		this.eCodeOfProject = eCodeOfProject;
	}
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
}
