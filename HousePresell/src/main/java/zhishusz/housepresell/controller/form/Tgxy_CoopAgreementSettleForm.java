package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_CoopAgreementSettleForm extends NormalActionForm
{
	private static final long serialVersionUID = -2471511804411548888L;
	
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
	private String eCode;//编号
	@Getter @Setter
	private String signTimeStamp;//签署日期
	@Getter @Setter
	private Emmp_CompanyInfo agentCompany;//代理公司
	@Getter @Setter
	private Long agentCompanyId;//代理公司-Id
	@Getter @Setter
	private String companyName;//代理公司-Id
	@Getter @Setter
	private String applySettlementDate;//申请结算日期
	@Getter @Setter
	private String startSettlementDate;//结算开始日期
	@Getter @Setter
	private String endSettlementDate;//结算截至日期
	@Getter @Setter
	private Integer protocolNumbers;//三方协议号有效份数
	@Getter @Setter
	private Integer settlementState;//结算状态
	@Getter @Setter
	private String smAttachmentList;//附件参数
	@Getter @Setter
	private Integer beforeNumbers;//三方协议过滤前份数
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
