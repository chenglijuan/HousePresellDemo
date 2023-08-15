package zhishusz.housepresell.util.template.compare;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//只能存基本类型的封装类
@ToString
public class Empj_BuildingInfoTemplate implements Serializable
{
	private static final long serialVersionUID = 5988219602524296643L;
	
	@Getter @Setter 
	private Double escrowArea;

	@Getter @Setter 
	private Double upfloorNumber;
	
	@Getter @Setter 
	private String landMortgagor;
	
	@Getter @Setter 
	private Double downfloorNumber;
	
	@Getter @Setter 
	private Double landMortgageAmount;
	
	@Getter @Setter 
	private Double buildingArea;
	
	@Getter @Setter 
	private String deliveryType;
	
	@Getter @Setter 
	private Integer landMortgageState;
	
	@SuppressWarnings("rawtypes")
	public List getNeedFieldList(){
		return Arrays.asList(
				"escrowArea", 
				"buildingArea",
				"deliveryType",
				"upfloorNumber",
				"downfloorNumber",
				"landMortgagor",
				"landMortgageAmount",
				"landMortgageState"
				);
	}

	public Empj_BuildingInfoTemplate() 
	{
		super();
	}
	
	public Empj_BuildingInfoTemplate(Empj_BuildingInfo buildingInfo, Empj_BuildingExtendInfo extendInfo) 
	{
		super();
		if(buildingInfo != null)
		{
			this.escrowArea = buildingInfo.getEscrowArea();
			this.upfloorNumber = buildingInfo.getUpfloorNumber();
			this.downfloorNumber = buildingInfo.getDownfloorNumber();
			this.buildingArea = buildingInfo.getBuildingArea();
			this.deliveryType = buildingInfo.getDeliveryType();
		}
		if(extendInfo != null)
		{
			this.landMortgagor = extendInfo.getLandMortgagor();
			this.landMortgageAmount = extendInfo.getLandMortgageAmount();
			this.landMortgageState = extendInfo.getLandMortgageState();
		}
	}
}

