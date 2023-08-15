package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：楼幢-单元
 * */
@ITypeAnnotation(remark="楼幢-单元")
public class Empj_UnitInfo implements Serializable,ILogable
{
	private static final long serialVersionUID = 2056197214770095582L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;

	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;

	@Getter @Setter @IFieldAnnotation(remark="地上楼层数")
	private Double upfloorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="地上每层户数")
	private Integer upfloorHouseHoldNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="地下楼层数")
	private Double downfloorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="地下每层户数")
	private Integer downfloorHouseHoldNumber;

	@Getter @Setter @IFieldAnnotation(remark="电梯数")
	private Integer elevatorNumber;//默认为0
	
	@Getter @Setter @IFieldAnnotation(remark="有无二次供水")
	private Boolean hasSecondaryWaterSupply;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@IFieldAnnotation(remark="关联日志Id")
	private Long logId;
	@Override
	public Long getLogId()
	{
		return logId;
	}
	@Override
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}
	@Override
	public String getLogData()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}
