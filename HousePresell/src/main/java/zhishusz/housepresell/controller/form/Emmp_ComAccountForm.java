package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：机构-财务账号信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Emmp_ComAccountForm extends NormalActionForm
{
	private static final long serialVersionUID = 2748151627752875781L;
	
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
	private String officeAddress;//办公地址
	@Getter @Setter
	private String officePhone;//办公电话
	@Getter @Setter
	private Emmp_BankBranch bankBranch;//开户银行
	@Getter @Setter
	private Long bankBranchId;//开户银行-Id
	@Getter @Setter
	private String theNameOfBank;//开户银行名称-冗余
	@Getter @Setter
	private String bankAccount;//开户账号
	@Getter @Setter
	private String theNameOfBankAccount;//开户名称
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
