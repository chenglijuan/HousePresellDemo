package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
 * 对象名称：办理时限配置表
 */
@ITypeAnnotation(remark="办理时限配置表")
public class Tg_HandleTimeLimitConfig implements Serializable
{
	private static final long serialVersionUID = -4959507946179552844L;
	public static final transient int Type_Tgxy_CoopAgreement = 1; // 合作协议
	public static final transient int Type_Tgxy_TripleAgreement = 2; // 三方协议
	public static final transient int Type_Empj_BldLimitAmount_AF = 3; // 受限额度
	public static final transient int Type_Empj_BldLimitAmount_CH = 4; // 工程进度节点更新
	public static final transient int Type_Empj_BldEscrowCompleted = 5; // 托管终止
	public static final transient int Type_Tgpf_DepositDetail = 6; // 资金归集
	public static final transient int Type_Tgpf_FundAppropriated = 7; // 托管资金
	public static final transient int Type_Tgpf_RefundInfo = 8; // 退房退款
	public static final transient int Type_Empj_PaymentGuaranteeApply = 9; // 支付保证
	public static final transient int Type_Tg_DepositManagement = 10; // 存单管理
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
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
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="业务类型")
	private String theType;
	
	@Getter @Setter @IFieldAnnotation(remark="办理完结标准")
	private String completionStandard;
	
	@Getter @Setter @IFieldAnnotation(remark="办理时限天数（以工作日计算）")
	private Integer limitDayNumber;

	@Getter @Setter @IFieldAnnotation(remark="角色")
	private Sm_Permission_Role role;

	@Getter @Setter @IFieldAnnotation(remark="配置人")
	private String lastCfgUser;
	
	@Getter @Setter @IFieldAnnotation(remark="配置日期")
	private Long lastCfgTimeStamp;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
