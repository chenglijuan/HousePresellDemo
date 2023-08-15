package zhishusz.housepresell.util.excel.model;

import java.util.List;

import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

public class Empj_BuildingTableTemplate
{
	@Getter @Setter @IFieldAnnotation(remark="所在楼层")
	private Double floor;
	
	@Getter @Setter @IFieldAnnotation(remark="单元列表")
	private List<Empj_UnitInfo> empj_UnitInfoList;
	
	@Getter @Setter @IFieldAnnotation(remark="户室列表")
	private List<Empj_HouseInfo> empj_HouseInfoList;
}