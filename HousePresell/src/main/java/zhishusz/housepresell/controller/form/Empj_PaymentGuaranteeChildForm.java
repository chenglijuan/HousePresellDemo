package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：支付保证申请
 * Company：ZhiShuSZ
 * create by wang
 * 2018/09/25
 * */
@ToString(callSuper=true)
public class Empj_PaymentGuaranteeChildForm extends NormalActionForm
{
	private static final long serialVersionUID = 2406487433686376094L;
	
	@Getter @Setter
	private Long tableId;//主键
	@Getter @Setter
	private Empj_BuildingInfo empj_BuildingInfo;//关联楼幢
	@Getter @Setter
	private Long empj_BuildingInfoId;//关联楼幢
	@Getter @Setter 
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter 
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private Double buildingArea;//建筑面积（㎡）
	@Getter @Setter 
	private Double escrowArea;//托管面积（㎡）
	@Getter @Setter
	private String recordAvgPriceOfBuilding;//楼幢住宅备案均价
	@Getter @Setter 
	private String escrowStandard;//托管标准
	@Getter @Setter 
	private Double orgLimitedAmount;//支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
	@Getter @Setter 
	private Double paymentProportion;// 支付保证封顶比例
	@Getter @Setter 
	private Double paymentLines; //支付保证封顶额度
	@Getter @Setter
	private Double buildAmountPaid; //楼幢项目建设已实际支付金额（元）
	@Getter @Setter 
	private String buildAmountPay;//楼幢项目建设待支付承保累计金额（元）
	@Getter @Setter
	private String totalAmountGuaranteed;//已落实支付保证累计金额（元）
	@Getter @Setter
	private Double buildProjectPaid;//楼幢项目建设已实际支付金额 
	@Getter @Setter 
	private Double buildProjectPay;//楼幢项目建设待支付承保金额（元）
	@Getter @Setter
	private Double amountGuaranteed; //已落实支付保证金额（元）
	@Getter @Setter 
	private Double cashLimitedAmount; //现金受限额度（元）
	
	@Getter @Setter 
	private Double currentFigureProgress; //当前形象进度	
	@Getter @Setter 
	private Double currentLimitedRatio; //当前受限比例（%）
	@Getter @Setter 
	private String nodeLimitedAmount;//当前节点受限额度（元）
	@Getter @Setter 
	private String effectiveLimitedAmount;//有效受限额度（元）
	@Getter @Setter
	private Double totalAccountAmount;//总入账金额（元）
	@Getter @Setter 
	private Double spilloverAmount;//楼幢项目建设待支付承保金额（元）
	@Getter @Setter 
	private Double appropriateFrozenAmount; //拨付冻结金额
	@Getter @Setter 
	private Double refundFrozenAmount; //退款冻结金额
	@Getter @Setter 
	private Double amountOverflow; //溢出金额（元）
	@Getter @Setter 
	private Double releaseTheAmount; //释放金额（元）
	
	@Getter @Setter 
	private String remark; //备注
	@Getter @Setter 
	private String oprUser;//操作人，关联用户表
	@Getter @Setter 
	private String oprTime;//操作日期 
	@Getter @Setter 
	private String auditUset;//审核人，关联用户表
	@Getter @Setter 
	private Empj_PaymentGuarantee empj_PaymentGuarantee;//关联主表
	

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
	@Getter @Setter @IFieldAnnotation(remark="版本号")
	private String versionNo;//版本号
	@Getter @Setter 
	private String condition;//条件
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	
}
