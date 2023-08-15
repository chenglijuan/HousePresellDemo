package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_BankInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：合作备忘录
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_CoopMemoForm extends NormalActionForm
{
	private static final long serialVersionUID = -7378378244578293111L;
	
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
	private String eCodeOfCooperationMemo;//合作备忘录编号
	@Getter @Setter
	private Emmp_BankInfo bank;//银行
	@Getter @Setter
	private Long bankId;//银行-Id
	@Getter @Setter
	private String theNameOfBank;//银行名称-冗余
	@Getter @Setter
	private String signDate;//签署日期yyyyMMdd
	@Getter @Setter
	private String smAttachmentList;//附件信息
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfCooperationMemo() {
		return eCodeOfCooperationMemo;
	}
	public void seteCodeOfCooperationMemo(String eCodeOfCooperationMemo) {
		this.eCodeOfCooperationMemo = eCodeOfCooperationMemo;
	}
	
	
}
