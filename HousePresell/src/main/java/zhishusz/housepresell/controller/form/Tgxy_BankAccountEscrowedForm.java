package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管账户
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_BankAccountEscrowedForm extends NormalActionForm
{
	private static final long serialVersionUID = 417112701818834094L;
	
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
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String contactPerson;//联系人-姓名
	@Getter @Setter
	private String contactPhone;//联系人-手机号
	@Getter @Setter
	private Long updatedStamp;//更新日期
	@Getter @Setter
	private Double income;//托管收入
	@Getter @Setter
	private Double payout;//托管支出
	@Getter @Setter
	private Double certOfDeposit;//大额存单
	@Getter @Setter
	private Double structuredDeposit;//结构性存款
	@Getter @Setter
	private Double breakEvenFinancial;//保本理财
	@Getter @Setter
	private Double currentBalance;//活期余额
	@Getter @Setter
	private Double canPayAmount; //托管可拨付金额
	@Getter @Setter
	private Double largeRatio;//大额占比
	@Getter @Setter
	private Double largeAndCurrentRatio;//大额+活期占比
	@Getter @Setter
	private Double financialRatio;//理财占比
	@Getter @Setter
	private Double totalFundsRatio;//总资金沉淀占比
	@Getter @Setter
	private Integer isUsing;//0：启用，1：不启用
	
	//zbp 2020-09-08 9:34  销户功能开发
	@Getter @Setter
	private Long closingTime; //销户时间
	
	@Getter @Setter
	private Integer hasClosing; //是否销户
	
	@Getter @Setter
	private Sm_User closingPerson; //销户人
	
	@Getter @Setter
	private Double transferOutAmount;//转出金额
	
	@Getter @Setter
	private Double transferInAmount;//转入金额
	
	@Getter @Setter
	private String toECode;//转出账户ecode
	
	@Getter @Setter
	private Long toTableId;//转出账户id
	
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
