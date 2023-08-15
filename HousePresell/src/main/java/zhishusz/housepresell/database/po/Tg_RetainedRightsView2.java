package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author XSZ
 * 更新后的视图
 */
@ITypeAnnotation(remark = "留存权益拨付明细")
public class Tg_RetainedRightsView2 implements Serializable
{
	
	private static final long serialVersionUID = -120696865938176214L;
	// ---------公共字段-Start---------//
	@Getter
	@Setter
	@IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;
	// ---------公共字段-Start---------//
	@Getter
	@Setter
	@IFieldAnnotation(remark = "留存权益计算日期")
	private String billtTimeStamp;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "到账日期")
	private String arrivalTimeStamp;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "企业名称")
	private String sellerName;
	
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
	@IFieldAnnotation(remark = "借款人名称")
	private String theNameOfCreditor;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "借款人身份证")
	private String idNumberOfCreditor;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "合同备案号")
	private String ecodeOfContractRecord;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "三方协议号")
	private String ecodeoftripleagreement;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "实际入账金额")
	private Double actualDepositAmount;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "按揭入账金额")
	private Double depositAmountFromloan;

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
	@IFieldAnnotation(remark = "留存权益系数 ")
	private String remaincoefficient ;
	
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
