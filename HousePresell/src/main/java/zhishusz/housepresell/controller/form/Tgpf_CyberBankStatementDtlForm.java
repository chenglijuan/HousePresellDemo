package zhishusz.housepresell.controller.form;

import java.util.List;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_CyberBankStatementDtlForm extends NormalActionForm
{
	private static final long serialVersionUID = 970043122489134961L;
	
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
	private Tgpf_CyberBankStatement mainTable;//关联‘网银对账’主表
	@Getter @Setter
	private Long mainTableId;//关联‘网银对账’主表-Id
	@Getter @Setter
	private String tradeTimeStamp;//交易日期
	@Getter @Setter
	private String recipientAccount;//对方账号
	@Getter @Setter
	private String recipientName;//对方账户名称
	@Getter @Setter
	private String remark;//备注摘要
	@Getter @Setter
	private Double income;//收入
	@Getter @Setter
	private Double payout;//支出
	@Getter @Setter
	private Double balance;//余额
	@Getter @Setter
	private Integer reconciliationState;//网银对账状态
	@Getter @Setter
	private String reconciliationUser;//对账人
	@Getter @Setter
	private String tradeAmount;//发生额
	@Getter @Setter
	private String reconciliationStamp;//网银对账时间
	@Getter @Setter
	private String coreNo;//核心流水号
	@Getter @Setter
	private String sm_attachment;
	@Getter @Setter
	private Integer accountType;//核心流水号
	@Getter @Setter
	private String cyBankTripleAgreementNum;//三方协议号
	@Getter @Setter
	private List<Tgpf_CyberBankStatementDtlForm> tgpf_CyberBankStatementDtl; //列表
	@Getter @Setter
	private String tgpf_CyberBankStatementDtlContrastDetailList;
	@Getter @Setter
	private Long detailedId;
	
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
