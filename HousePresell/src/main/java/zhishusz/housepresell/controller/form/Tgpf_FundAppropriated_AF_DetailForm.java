package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：申请-用款-明细
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_FundAppropriated_AF_DetailForm extends NormalActionForm
{
	private static final long serialVersionUID = 6468510316724854166L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private Tgpf_FundAppropriated_AF mainTable;//用款主表
	@Getter @Setter
	private Long mainTableId;//用款主表-Id
	@Getter @Setter
	private Tgpj_BankAccountSupervised bankAccountSupervised;//关联监管账号
	@Getter @Setter
	private Long bankAccountSupervisedId;//关联监管账号-Id
	@Getter @Setter
	private String supervisedBankAccount;//监管账号
	@Getter @Setter
	private Double allocableAmount;//当前可划拨金额（元）
	@Getter @Setter
	private Double appliedAmount;//本次划款申请金额（元）
	@Getter @Setter
	private String escrowStandard;//托管标准（元/㎡）
	@Getter @Setter
	private Double orgLimitedAmount;//初始受限额度（元）
	@Getter @Setter
	private Double currentFigureProgress;//当前形象进度
	@Getter @Setter
	private Double currentLimitedRatio;//当前受限比例（%）
	@Getter @Setter
	private Double currentLimitedAmount;//当前受限额度（元）
	@Getter @Setter
	private Double totalAccountAmount;//总入账金额（元）
	@Getter @Setter
	private Double appliedPayoutAmount;//已申请拨付金额（元）
	@Getter @Setter
	private Double currentEscrowFund;//当前托管余额（元）
	@Getter @Setter
	private Double refundAmount;//退房退款金额（元）
	@Getter @Setter
	private Integer payoutState;//拨付状态
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}
