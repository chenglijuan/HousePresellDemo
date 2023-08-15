package zhishusz.housepresell.exportexcelvo;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-楼幢账户表-导出
 * 
 * @ClassName: Qs_BuildingAccount_ViewEportExcelVO
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月18日 下午6:56:37
 * @version V1.0
 *
 */
@ITypeAnnotation(remark = "楼幢账户表")
public class Qs_BuildingAccount_ViewEportExcelVO
{

	@Getter
	@Setter
	private Integer ordinal;// 序号

	@Getter
	@Setter
	@IFieldAnnotation(remark = "开发企业")
	private String theNameOfCompany;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目")
	private String theNameOfProject;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "施工楼幢")
	private String eCodeFromConstruction;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "地上楼层数")
	private String upFloorNumber;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "托管标准")
	private String escrowStandard;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "托管面积")
	private Double escrowArea;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "建筑面积")
	private Double buildingArea;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "交付类型")
	private String deliveryType;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "初始受限额度（元）")
	private Double orgLimitedAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "当前形象进度")
	private String currentFigureProgress;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "当前受限比例（%）")
	private Double currentLimitedRatio;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "当前受限额度（元）")
	private Double currentLimitedAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "总入账金额（元）")
	private Double totalAccountAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "溢出金额（元）")
	private Double spilloverAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "已拨付金额（元）")
	private Double payoutAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "已申请未拨付金额（元）")
	private Double appliedNoPayoutAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "已申请退款未拨付金额（元）")
	private Double applyRefundPayoutAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "已退款金额（元）")
	private Double refundAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "当前托管余额（元）")
	private Double currentEscrowFund;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "可划拨金额（元）")
	private Double allocableAmount;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "预售系统楼幢住宅备案均价")
	private Double recordAvgPriceBldPS;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "楼幢住宅备案均价")
	private Double recordAvgPriceOfBuilding;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "支付保证金额")
	private Double zfbzPrice;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "现金受限金额")
	private Double xjsxPrice;
	
	@Getter
	@Setter
	@IFieldAnnotation(remark = "有效受限额度")
	private Double yxsxPrice;
	
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityregion;
	
	@Getter @Setter @IFieldAnnotation(remark="总户数")
	private Integer sumfamilyNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="签约户数")
	private Integer signhouseNum;
	
	@Getter @Setter @IFieldAnnotation(remark="备案户数")
	private Integer recordhouseNum;
	
	@Getter @Setter @IFieldAnnotation(remark="托管户数")
	private Integer deposithouseNum;
	
	@Getter @Setter @IFieldAnnotation(remark="预售证（有/无）")
	private String ispresell;
	 
	@Getter @Setter @IFieldAnnotation(remark="合作协议备案时间")
	private String escrowagrecordTime;

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

}
