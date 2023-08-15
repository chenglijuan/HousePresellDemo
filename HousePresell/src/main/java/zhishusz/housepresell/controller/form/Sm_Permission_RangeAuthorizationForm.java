package zhishusz.housepresell.controller.form;

import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：角色与UI权限对应关系
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Permission_RangeAuthorizationForm extends NormalActionForm
{
	private static final long serialVersionUID = -1659005083291557428L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="不包括表ID", isPrimarykey=true)
	private Long exceptTableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
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

	@Getter @Setter @IFieldAnnotation(remark="机构Id")
	private Long emmp_CompanyInfoId;
	
	@Getter @Setter @IFieldAnnotation(remark="正泰用户，按照用户进行授权（冗余字段，方便查询）")
	private Sm_User userInfo;

	@Getter @Setter @IFieldAnnotation(remark="正泰用户Id，按照用户进行授权（冗余字段，方便查询）")
	private Long userInfoId;
	
	@Getter @Setter @IFieldAnnotation(remark="授权起始日期")
	private Long authStartTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="授权截止日期")
	private Long authEndTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="授权日期")
	private String authTimeStampRange;

	@Getter @Setter @IFieldAnnotation(remark="授权类别 （区域、项目、楼幢） S_RangeAuthType")
	private Integer rangeAuthType;
	
	@Getter @Setter @IFieldAnnotation(remark="机构下的范围授权数据")
	private List<Sm_Permission_Range> rangeInfoList;

	@Getter @Setter @IFieldAnnotation(remark="楼幢Id")
	private Long buildingInfoId;

	@Getter @Setter @IFieldAnnotation(remark="项目Id")
	private Long projectInfoId;

	@Getter @Setter @IFieldAnnotation(remark="添加项目或者添加楼幢，审批发起人Id")
	private Long sponsorId;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
