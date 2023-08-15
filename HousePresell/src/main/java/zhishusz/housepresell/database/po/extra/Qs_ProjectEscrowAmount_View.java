package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-项目托管资金收支情况表
 * @ClassName:  Qs_RecordAmount_View   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月6日 下午4:06:29   
 * @version V1.0 
 *
 */
@ITypeAnnotation(remark="项目托管资金收支情况表")
public class Qs_ProjectEscrowAmount_View implements Serializable
{
	
	private static final long serialVersionUID = -3722918843340781761L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="流水号")
	private String serialNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String recordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢（施工楼幢）")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="放款户数")
	private Integer loansCountHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;
	
	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="余额")
	private Double currentFund;
	
	@Getter @Setter @IFieldAnnotation(remark="溢出金额")
	private Double spilloverAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remarkNote;

	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private String cityRegionname;

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	
	
	
	

}
