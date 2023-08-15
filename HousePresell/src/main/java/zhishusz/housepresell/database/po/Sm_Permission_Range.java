package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：机构下的范围授权数据
 * 
 */
@ITypeAnnotation(remark="机构下的范围授权数据")
public class Sm_Permission_Range implements Serializable
{
	private static final long serialVersionUID = 2502753537639856045L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="类型:区域，项目，楼幢 S_RangeAuthType")
	private Integer theType;

	@Getter @Setter @IFieldAnnotation(remark="正泰用户，按照用户进行授权（冗余字段，方便查询）")
	private Sm_User userInfo;

	@Getter @Setter @IFieldAnnotation(remark="区域")
	private Sm_CityRegionInfo cityRegionInfo;

	@Getter @Setter @IFieldAnnotation(remark="机构（冗余字段，方便查询）")
	private Emmp_CompanyInfo companyInfo;

	@Getter @Setter @IFieldAnnotation(remark="项目")
	private Empj_ProjectInfo projectInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢")
	private Empj_BuildingInfo buildingInfo;

	@Getter @Setter @IFieldAnnotation(remark="关联范围授权（冗余字段，方便查询）")
	private Sm_Permission_RangeAuthorization rangeAuth;
	
	@Getter @Setter @IFieldAnnotation(remark="授权起始日期（冗余字段，方便查询）")
	private Long authStartTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="授权截止日期（冗余字段，方便查询）")
	private Long authEndTimeStamp;
}
