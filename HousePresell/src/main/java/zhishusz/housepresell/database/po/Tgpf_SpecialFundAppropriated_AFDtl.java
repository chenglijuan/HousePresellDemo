package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：特殊拨付-申请子表
 * */
@ITypeAnnotation(remark="特殊拨付-申请子表")
public class Tgpf_SpecialFundAppropriated_AFDtl implements Serializable
{
	
	private static final long serialVersionUID = -8325929863746381785L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
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
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="关联申请主表")
	private Tgpf_SpecialFundAppropriated_AF specialAppropriated;
	
	@Getter @Setter @IFieldAnnotation(remark="主表申请编号")
	private String theCodeOfAf;
	
	@Getter @Setter @IFieldAnnotation(remark="划款账号关联类")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="划款账号")
	private String accountOfEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="划款账号名称")
	private String theNameOfEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="已申请未拨付总金额（元-->currentBalance活期余额）")
	private Double applyRefundPayoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="本次拨付申请金额（元）")
	private Double appliedAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="账户余额")
	private Double accountBalance;
	
	@Getter @Setter @IFieldAnnotation(remark="票据号")
	private String billNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="拨付渠道")
	private String payoutChannel;
	
	@Getter @Setter @IFieldAnnotation(remark="拨付日期")
	private String payoutDate;

	@Getter @Setter @IFieldAnnotation(remark="拨付状态 S_SpecialFundApplyState")
	private Integer payoutState;
	
	@Getter @Setter @IFieldAnnotation(remark="推送状态 0-发起中 1-成功 2-失败")
	private String pushState;
	
	@Getter @Setter @IFieldAnnotation(remark="反馈信息")
	private String pushInfo;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
	

}
