package zhishusz.housepresell.database.po;
import java.io.Serializable;
import java.util.Date;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付保证申请详情
 * @author wang
 *create by 2018/09/25
 */
@ITypeAnnotation(remark="支付保证申请详情子表")
public class Empj_PaymentGuaranteeChild implements Serializable{

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo empj_BuildingInfo;//关联楼幢
	@Getter @Setter @IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter @IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter @IFieldAnnotation(remark="建筑面积（㎡） ")
	private Double buildingArea;//建筑面积（㎡）
	@Getter @Setter @IFieldAnnotation(remark="托管面积（㎡）")
	private Double escrowArea;//托管面积（㎡）
	@Getter @Setter @IFieldAnnotation(remark="楼幢住宅备案均价")
	private Double recordAvgPriceOfBuilding;//楼幢住宅备案均价
	@Getter @Setter @IFieldAnnotation(remark="托管标准")	
	private String escrowStandard;//托管标准
	@Getter @Setter @IFieldAnnotation(remark="初始受限额度（元） ")
	private Double orgLimitedAmount;//初始受限额度（元）
	@Getter @Setter @IFieldAnnotation(remark="支付保证封顶比例 ")
	private Double paymentProportion;// 支付保证封顶比例
	@Getter @Setter @IFieldAnnotation(remark="支付保证封顶额度")
	private Double paymentLines; //支付保证封顶额度
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设已实际支付金额（元） ")
	private Double buildAmountPaid; //楼幢项目建设已实际支付金额（元）
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设待支付承保累计金额（元）")
	private Double buildAmountPay;//楼幢项目建设待支付承保累计金额（元）
	@Getter @Setter @IFieldAnnotation(remark="已落实支付保证累计金额（元）")	
	private Double totalAmountGuaranteed;//已落实支付保证累计金额（元）
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设已实际支付金额  ")
	private Double buildProjectPaid;//楼幢项目建设已实际支付金额 
	@Getter @Setter @IFieldAnnotation(remark="楼幢项目建设待支付承保金额（元） ")
	private Double buildProjectPay;//楼幢项目建设待支付承保金额（元）
	@Getter @Setter @IFieldAnnotation(remark="已落实支付保证金额（元）")
	private Double amountGuaranteed; //已落实支付保证金额（元）	
	@Getter @Setter @IFieldAnnotation(remark="现金受限额度（元） ")
	private Double cashLimitedAmount; //现金受限额度（元）
	@Getter @Setter @IFieldAnnotation(remark="当前形象进度	")
	private String currentFigureProgress; //当前形象进度	
	@Getter @Setter @IFieldAnnotation(remark="当前受限比例（%）")
	private Double currentLimitedRatio; //当前受限比例（%）
	@Getter @Setter @IFieldAnnotation(remark="当前节点受限额度（元）")
	private Double nodeLimitedAmount;//当前节点受限额度（元）
	@Getter @Setter @IFieldAnnotation(remark="有效受限额度（元）")		
	private Double effectiveLimitedAmount;//有效受限额度（元）
	@Getter @Setter @IFieldAnnotation(remark="总入账金额（元）")
	private Double totalAccountAmount;//总入账金额（元）	
	@Getter @Setter @IFieldAnnotation(remark="已拨付金额（元） ")
	private Double payoutAmount;//已拨付金额
	@Getter @Setter @IFieldAnnotation(remark="溢出金额） ")
	private Double spilloverAmount;//溢出金额（元）
	@Getter @Setter @IFieldAnnotation(remark="拨付冻结金额")
	private Double appropriateFrozenAmount; //拨付冻结金额
	@Getter @Setter @IFieldAnnotation(remark="退款冻结金额 ")
	private Double appliedNoPayoutAmount; //退款冻结金额
	@Getter @Setter @IFieldAnnotation(remark="释放金额（元） ")
	private Double releaseTheAmount; //释放金额（元）
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark; //备注	

	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
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
	private Integer versionNo;//版本号
	
	@Getter @Setter @IFieldAnnotation(remark="关联支付保证主键")
	private Empj_PaymentGuarantee empj_PaymentGuarantee;//关联支付保证主键
	
	
	
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeFromPublicSecurity() {
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	
	
	

}
