package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
 * 对象名称: 用款申请信息汇总 - 监管账号
 * TODO：其他字段待补充
 * */
@ITypeAnnotation(remark="资金统筹")
public class Tgpf_FundOverallPlanDetail implements Serializable
{
	private static final long serialVersionUID = 5210453663767276054L;
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态(统筹状态)")
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

	@Getter @Setter @IFieldAnnotation(remark="用款主表")
	private Tgpf_FundAppropriated_AF mainTable;

	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;

	@Getter @Setter @IFieldAnnotation(remark="开户行名称")
	private String theNameOfBankBranch;

	@Getter @Setter @IFieldAnnotation(remark="关联监管账号")
	private Tgpj_BankAccountSupervised bankAccountSupervised;

	@Getter @Setter @IFieldAnnotation(remark="监管账户名称")
	private String theNameOfAccount;

	@Getter @Setter @IFieldAnnotation(remark="监管账号")
	private String supervisedBankAccount;

	@Getter @Setter @IFieldAnnotation(remark="本次划款申请金额（元）")
	private Double appliedAmount;

	@Getter @Setter @IFieldAnnotation(remark="关联资金统筹")
	private Tgpf_FundOverallPlan fundOverallPlan;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
