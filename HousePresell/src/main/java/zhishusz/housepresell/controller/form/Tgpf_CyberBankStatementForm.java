package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_CyberBankStatementForm extends NormalActionForm
{
	private static final long serialVersionUID = -7716544834769376104L;
	
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
	private String theNameOfBank;//对账银行
	@Getter @Setter
	private String theAccountOfBankAccountEscrowed;//托管账号
	@Getter @Setter
	private String theNameOfBankAccountEscrowed;//托管账号名称
	@Getter @Setter
	private String theNameOfBankBranch;//开户行
	@Getter @Setter
	private Integer reconciliationState;//网银对账状态
	@Getter @Setter
	private String reconciliationUser;//对账人
	@Getter @Setter
	private String orgFilePath;//原始数据文件路径
	@Getter @Setter 
	private String uploadTimeStamp;  //文件上传日期
	@Getter @Setter
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private Long bankId;//银行ID
	@Getter @Setter
	private Long theAccountOfBankAccountEscrowedId;//托管账户ID
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
