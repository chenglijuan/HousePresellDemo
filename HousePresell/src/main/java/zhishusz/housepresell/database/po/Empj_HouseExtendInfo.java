
package zhishusz.housepresell.database.po;

import java.io.Serializable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：楼幢-户室-拓展表
 * */
@ITypeAnnotation(remark="楼幢-户室-拓展表")
public class Empj_HouseExtendInfo implements Serializable
{
	
	private static final long serialVersionUID = -1053597686077215965L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态 S_HouseBusiState")
	private String busiState;
	
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

	@Getter @Setter @IFieldAnnotation(remark="关联户室")
	private Empj_HouseInfo houseInfo;
	
	@IFieldAnnotation(remark="户室编号")
	private String eCodeOfHouseInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="操作时间")
	private String setDate;

	@Getter @Setter @IFieldAnnotation(remark="设定人")
	private String setUser;
	
	@Getter @Setter @IFieldAnnotation(remark="设定原因")
	private String setReason;

	@Getter @Setter @IFieldAnnotation(remark="操作类型(字典668：1.限制2.解除限制3.房屋处置,4.解除处置，5.续封，6.查封,7.解除查封)")
	private String operateType;
	
	@Getter @Setter @IFieldAnnotation(remark="针对guid(针对哪个限制业务)")
	private String aimedGuid;
	
	@Getter @Setter @IFieldAnnotation(remark="是否解除(0有效，1限制解除；默认0)")
	private String is_relieve;
	
	@Getter @Setter @IFieldAnnotation(remark="限制类型（673）")
	private String limitType;

	@Getter @Setter @IFieldAnnotation(remark="项目guid")
	private String projectGuid;
	
	@Getter @Setter @IFieldAnnotation(remark="限制开始日期")
	private String startDate;
	
	@IFieldAnnotation(remark="限制结束日期")
	private String endDate;
	
	@Getter @Setter @IFieldAnnotation(remark="解除限制类型")
	private String removeLimitType;

	@Getter @Setter @IFieldAnnotation(remark="流水号(受理编号)")
	private String serialNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="法院")
	private String court;

	@Getter @Setter @IFieldAnnotation(remark="案号")
	private String ah;
	
	@Getter @Setter @IFieldAnnotation(remark="经办人")
	private String jbr;
	
	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String lxdh;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}

	public String geteCodeOfHouseInfo()
	{
		return eCodeOfHouseInfo;
	}

	public void seteCodeOfHouseInfo(String eCodeOfHouseInfo)
	{
		this.eCodeOfHouseInfo = eCodeOfHouseInfo;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	@Getter @Setter @IFieldAnnotation(remark="受理(送达)日期")
	private String acceptDate;
	
	@Getter @Setter @IFieldAnnotation(remark="上次查封guid")
	private String lastlimitGuid;
	
	@Getter @Setter @IFieldAnnotation(remark="预(销)售证号")
	private String certificateNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="预(销)售范围")
	private String saleRange;

	@Getter @Setter @IFieldAnnotation(remark="房屋限制表主键guid")
	private String rowGuid;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	

}