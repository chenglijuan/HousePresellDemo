package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：业务编号
 * 用于生成eCode
 * */
@ITypeAnnotation(remark="业务编号")
public class Sm_BusinessCode implements Serializable
{
	private static final long serialVersionUID = -8931002038131815526L;

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

	@Getter @Setter @IFieldAnnotation(remark="业务编号")
	private String busiCode;

	@Getter @Setter @IFieldAnnotation(remark="年份")
	private Integer theYear;
	
	@Getter @Setter @IFieldAnnotation(remark="月份")
	private Integer theMonth;
	
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private Integer theDay;
	
	@Getter @Setter @IFieldAnnotation(remark="业务数量")
	private Integer ticketCount;
	
	public String getStringFormat()
	{
		return String.format("%s%s%2d%02d%02d%05d", busiCode, "N", theYear%100, theMonth, theDay, ticketCount);
	}
	
	//合作协议编号生成 年+月+日+区域+两位流水号
	public String getEscrowAgreementStringFormat()
	{
		return String.format("%2d%02d%02d%s%02d", theYear%100, theMonth, theDay,busiCode ,ticketCount);
	}
	
	//三方协议编号生成 合作协议号+四位流水号（按天流水）
	public String getTripleAgreementStringFormat()
	{
		return String.format("%s%04d",busiCode ,ticketCount);
	}
	
	//三方协议编号生成 合作协议号+四位流水号（按天流水）
	
	public String getCoopAgreementSettleStringFormat()
	{
		return String.format("%s%2d%02d%02d%04d","JSQR" ,theYear%100, theMonth, theDay, ticketCount);
	}

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
