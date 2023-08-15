package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="楼幢-扩展信息")
public class Empj_BuildingExtendInfo implements Serializable
{
	private static final long serialVersionUID = -8142419659847111874L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼栋")
	private Empj_BuildingInfo buildingInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="预售状态")
	private String presellState;
	
	@IFieldAnnotation(remark="预售证号")
	private String eCodeOfPresell;

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
	
	@Getter @Setter @IFieldAnnotation(remark="预售日期")
	private Long presellDate;
	
	@Getter @Setter @IFieldAnnotation(remark="限制状态")
	private String limitState;
	
	@Getter @Setter @IFieldAnnotation(remark="托管状态")
	private String escrowState;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押状态")
	private Integer landMortgageState;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押权人")
	private String landMortgagor;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押金额")
	private Double landMortgageAmount;

	@Getter @Setter @IFieldAnnotation(remark="是否是“支付保证业务PaymentGuaranteeService”楼幢")
	private Boolean isSupportPGS;

	public String geteCodeOfPresell() {
		return eCodeOfPresell;
	}

	public void seteCodeOfPresell(String eCodeOfPresell) {
		this.eCodeOfPresell = eCodeOfPresell;
	}
}
