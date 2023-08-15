package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：角色授权的数据
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RangeForm extends NormalActionForm
{
	private static final long serialVersionUID = -6672904750298279519L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="类型:区域，项目，楼幢 S_RangeType")
	private Integer theType;

	@Getter @Setter @IFieldAnnotation(remark="正泰用户，按照用户进行授权（冗余字段，方便查询）")
	private Sm_User user;

	@Getter @Setter @IFieldAnnotation(remark="区域")
	private Sm_CityRegionInfo cityRegionInfo;

	@Getter @Setter @IFieldAnnotation(remark="机构（冗余字段，方便查询）")
	private Emmp_CompanyInfo companyInfo;

	@Getter @Setter @IFieldAnnotation(remark="项目")
	private Empj_ProjectInfo projectInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢")
	private Empj_BuildingInfo buildingInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="授权起始日期（冗余字段，方便查询）")
	private Long leAuthStartTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="授权截止日期（冗余字段，方便查询）")
	private Long gtAuthEndTimeStamp;
}
