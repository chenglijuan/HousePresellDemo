package zhishusz.housepresell.database.po;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/*
 * 对象名称：托管账户
 * */
@ITypeAnnotation(remark="托管账户")
public class Tgxy_BankAccountEscrowedView implements Serializable
{
	private static final long serialVersionUID = -1488084750276674873L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="所属项目")
	private Empj_ProjectInfo project;

	@Getter @Setter @IFieldAnnotation(remark="所属银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行名称/银行名称-冗余")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行简称")
	private String shortNameOfBank;

//	@Getter @Setter @IFieldAnnotation(remark="所属支行ID")
//	private Long bankBranchId;

	@Getter @Setter @IFieldAnnotation(remark="所属支行")
	private Emmp_BankBranch bankBranch;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="账号")
	private String theAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="联系人-姓名")
	private String contactPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人-手机号")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="更新日期")
	private Long updatedStamp; 

	@Getter @Setter @IFieldAnnotation(remark="托管收入")
	private Double income;

	@Getter @Setter @IFieldAnnotation(remark="托管支出")
	private Double payout;
	
	@Getter @Setter @IFieldAnnotation(remark="大额存单")
	private Double certOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="结构性存款")
	private Double structuredDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="保本理财")
	private Double breakEvenFinancial;
	
	@Getter @Setter @IFieldAnnotation(remark="活期余额")
	private Double currentBalance;

	@Getter @Setter @IFieldAnnotation(remark="托管可拨付金额")
	private Double canPayAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="大额占比")
	private Double largeRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="大额+活期占比")
	private Double largeAndCurrentRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="理财占比")
	private Double financialRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="总资金沉淀占比")
	private Double totalFundsRatio;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;//0：启用，1：不启用
	
	//zbp 2020-09-08 9:34  销户功能开发
	@Getter @Setter @IFieldAnnotation(remark="销户时间")
	private Long closingTime;
	
	@Getter @Setter @IFieldAnnotation(remark="销户人")
	private Sm_User closingPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="转出金额")
	private Double transferOutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="转入金额")
	private Double transferInAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="是否销户")
	private Integer hasClosing;
	
	@Getter @Setter  @IFieldAnnotation(remark="转出账户ID")
	private String toECode;
	
	//largeAndCurrentRatio = (certOfDeposit+currentBalance)/income//大额+活期占比=（大额存单+托管账户活期余额）/托管收入
	
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName", "eCode","userUpdate/theName","userStart/theName");
	}

}
