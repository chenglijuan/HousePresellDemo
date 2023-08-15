package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 2.4.6.3.1.留存权益拨付明细
 * 对象名称:Tg_RemainRight_Bak
 * create by li
 * 2018/09/18
 */
@ITypeAnnotation(remark = "留存权益拨付明细")
public class Tg_RetainedRightsView implements Serializable
{

	private static final long serialVersionUID = 2291746829141993636L;

	// ---------公共字段-Start---------//
	@Getter
	@Setter
	@IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;
	// ---------公共字段-Start---------//
	@Getter
	@Setter
	@IFieldAnnotation(remark = "到账日期")
	private String arrivalTimeStamp;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "入账日期")
	private String enterTimeStamp;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目名称")
	private String projectName;

	@IFieldAnnotation(remark = "楼幢施工编号")
	private String ecodeFromConstruction;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "单元信息")
	private String ecodeOfBuildingUnit;

	@IFieldAnnotation(remark = "房间号")
	private String ecodeFromRoom;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人名称")
	private String buyer;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "按揭入账金额")
	private Double depositAmountFromloan;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "累计支付金额")
	private Double cumulativeAmountPaid;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "留存权益总金额")
	private Double theAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "未到期权益金额")
	private Double amountOfInterestNotdue;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "到期权益金额")
	private Double amountOfInterestdue;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "本次支付金额")
	private Double amountOfThisPayment;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "拨付日期")
	private String fromDate;

	public String getEcodeFromConstruction()
	{
		return ecodeFromConstruction;
	}

	public void setEcodeFromConstruction(String ecodeFromConstruction)
	{
		this.ecodeFromConstruction = ecodeFromConstruction;
	}

	public String getEcodeFromRoom()
	{
		return ecodeFromRoom;
	}

	public void setEcodeFromRoom(String ecodeFromRoom)
	{
		this.ecodeFromRoom = ecodeFromRoom;
	}
}
