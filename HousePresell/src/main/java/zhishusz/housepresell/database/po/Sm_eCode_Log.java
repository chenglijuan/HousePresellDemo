package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：eCode记录
 * */
@ITypeAnnotation(remark="eCode记录")
public class Sm_eCode_Log implements Serializable
{
	private static final long serialVersionUID = -1792092884551766315L;

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

	@Getter @Setter @IFieldAnnotation(remark="备案状态 S_RecordState")
	private Integer recordState;
	
	@Getter @Setter @IFieldAnnotation(remark="备案驳回原因")
	private String recordRejectReason;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;//业务编码，参看“功能菜单-业务编码.xlsx”
	
	@Getter @Setter @IFieldAnnotation(remark="年份")
	private Integer theYear;
	
	@Getter @Setter @IFieldAnnotation(remark="月份")
	private Integer theMonth;
	
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private Integer theDay;
	
	@Getter @Setter @IFieldAnnotation(remark="业务数量")
	private Integer ticketCount;//日流水

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
