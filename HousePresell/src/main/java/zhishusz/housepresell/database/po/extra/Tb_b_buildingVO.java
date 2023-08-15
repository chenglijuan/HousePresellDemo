package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * 同步楼幢VO
 * 2018-9-30 15:48:25
 * 
 * @author ZS_DEV_05
 *
 */
public class Tb_b_buildingVO
{

	@Getter
	@Setter
	private String externalId;// 外来数据关联主键 rowguid
	@Getter
	@Setter
	private String eCodeOfPjFromPS;// 项目id pk_basinfo
									// eCodeOfProjectFromPresellSystem
	@Getter
	@Setter
	private String theNameFromPresellSystem;// 预售项目名称 proname
	@Getter
	@Setter
	private String eCodeFromPresellSystem;// 预售系统楼幢编号 buildingid
	@Getter
	@Setter
	private String eCodeFromConstruction;// 施工编号 constructno
	@Getter
	@Setter
	private String eCodeFromPublicSecurity;// 公安编号 buildingno
	@Getter
	@Setter
	private String unitNumber;// 单元数 unitnum
	@Getter
	@Setter
	private String buildingArea;// 建筑面积（㎡） area
	@Getter
	@Setter
	private String position;// 楼幢坐落 constructaddress
	@Getter
	@Setter
	private String upfloorNumber;// 地上楼层数 upfloor
	@Getter
	@Setter
	private String downfloorNumber;// 地下楼层数 downfloor
	@Getter
	@Setter
	private String decorationType;// 装修类型 decoratetype
	@Getter
	@Setter
	private String endDate;// 竣工日期yyyyMMdd enddate
	@Getter
	@Setter
	private String unitInfo;//单元信息
	@Getter
	@Setter
	private String certificatenumber;//预售证

}