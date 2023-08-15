package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：范围授权
 * 	权限：数据自动筛选核心文件
 */
@ITypeAnnotation(remark="范围授权")
public class Sm_Permission_RangeAuthorization implements Serializable
{
	private static final long serialVersionUID = -7143461242904816419L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;//业务编码，参看“功能菜单-业务编码.xlsx”
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

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
	
	@Getter @Setter @IFieldAnnotation(remark="从关联机构中获取到，实际数据存在 Sm_BaseParameter 表中")
	private String forCompanyType;
	
	@Getter @Setter @IFieldAnnotation(remark="机构")
	private Emmp_CompanyInfo emmp_CompanyInfo;

	@Getter @Setter @IFieldAnnotation(remark="正泰用户")
	private Sm_User userInfo;

	@Getter @Setter @IFieldAnnotation(remark="授权起始日期")
	private Long authStartTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="授权截止日期")
	private Long authEndTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="授权类别 （区域、项目、楼幢） S_RangeAuthType")
	private Integer rangeAuthType;
	
	@Getter @Setter @IFieldAnnotation(remark="机构下的范围授权数据")
	private List<Sm_Permission_Range> rangeInfoList;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
