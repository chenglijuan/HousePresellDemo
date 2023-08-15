package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucher;

/*
 * Form表单：基本户凭证-子
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_BasicAccountVoucherDtlForm extends NormalActionForm
{
    private static final long serialVersionUID = -9010545098308155649L;
    @Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
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
    private String billTimeStamp;//日期
    @Getter @Setter
    private String remark;//摘要
    @Getter @Setter
    private String subCode;//科目代码
    @Getter @Setter
    private Double totalTradeAmount;//金额
    @Getter @Setter
    private String sendState;//推送状态【自动写入枚举：0-未推送、1-已推送】
    @Getter @Setter
    private String sendTime;//推送日期
    @Getter @Setter
    private String vou_No;//凭证号
    @Getter @Setter
    private String isSplit;//是否拆分（0-否 1-是）
    @Getter @Setter
    private String contentJson;//凭证内容
    @Getter @Setter
    private Tgpf_BasicAccountVoucher accountVoucher;
    
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
