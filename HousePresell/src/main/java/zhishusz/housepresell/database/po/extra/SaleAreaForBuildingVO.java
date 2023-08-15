package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目部报表-楼幢销售面积VO
 * @author Administrator
 *
 */
@Getter
@Setter
public class SaleAreaForBuildingVO implements Serializable {

	private static final long serialVersionUID = -1276414547215116346L;
	
	private Integer serialNumber;//序号
	private String theNameOfCompany;//开发企业
	private String theNameOfProject;//项目
	private String theNameOfBuilding;//施工编号
	private Double price;//楼幢备案均价
	private Double escrowarea;//楼幢托管面积
	private Double salemj;//楼幢已销售面积
	private Double mj;//楼幢备案面积

}
