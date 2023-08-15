package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.*;

/*
 * Form表单：托管账户
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_BankAccountEscrowedViewForm extends NormalActionForm
{
	private static final long serialVersionUID = 417112701818834094L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private String userUpdate;//修改人
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
	private Emmp_CompanyInfo company;//所属机构
	@Getter @Setter
	private Long companyId;//所属机构-Id
	@Getter @Setter
	private Empj_ProjectInfo project;//所属项目
	@Getter @Setter
	private Long projectId;//所属项目-Id
	@Getter @Setter
	private Emmp_BankInfo bank;//所属银行
	@Getter @Setter
	private Long bankId;//所属银行-Id
	@Getter @Setter
	private String theNameOfBank;//开户行名称/银行名称-冗余
	@Getter @Setter
	private String shortNameOfBank;//银行简称
	@Getter @Setter
	private Emmp_BankBranch bankBranch;//所属支行
	@Getter @Setter
	private Long bankBranchId;//所属支行-Id
	@Getter @Setter
	private String theName;//托管账户名称
	@Getter @Setter
	private String theAccount;//账号

//	public String geteCode() {
//		return eCode;
//	}
//	public void seteCode(String eCode) {
//		this.eCode = eCode;
//	}
	
	
}
