package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：机构-财务账号
 * */
@ITypeAnnotation(remark="机构-财务账号信息")
public class Emmp_ComAccount implements Serializable
{
	private static final long serialVersionUID = -1666211870875372281L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="办公地址")
	private String officeAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="办公电话")
	private String officePhone;
	
	@Getter @Setter @IFieldAnnotation(remark="开户银行")
	private Emmp_BankBranch bankBranch;
	
	@Getter @Setter @IFieldAnnotation(remark="开户银行名称-冗余")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="开户账号")
	private String bankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="开户名称")
	private String theNameOfBankAccount;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
