package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_BankInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：存单管理
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_DepositManagementForm extends NormalActionForm
{
	private static final long serialVersionUID = 3967044573132089948L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
//	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private String depositState;//存单状态 S_DepositState
	@Getter @Setter
	private String exceptDepositState;//存单状态 S_DepositState
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
	private String depositProperty;//存款性质
	@Getter @Setter
	private Emmp_BankInfo bank;//存款银行
	@Getter @Setter
	private Long bankId;//存款银行-Id
	@Getter @Setter
	private String bankOfDeposit;//开户行
	@Getter @Setter
	private Long bankOfDepositId;//开户行-Id
	@Getter @Setter
	private String escrowAcount;//托管账户
	@Getter @Setter
	private Long escrowAcountId;//托管账户-Id
	@Getter @Setter
	private String escrowAcountShortName;//托管账户简称
	@Getter @Setter
	private String agent;//经办人
	@Getter @Setter
	private Double principalAmount;//本金金额（元）
	@Getter @Setter
	private Integer storagePeriod;//存期
	@Getter @Setter
	private String storagePeriodCompany;//存期单位
	@Getter @Setter
	private Double annualRate;//年利率（%）
	@Getter @Setter
	private Long startDate;//开始日期
	@Getter @Setter
	private String startDateStr;//开始日期Str
	@Getter @Setter
	private Long stopDate;//截至日期
	@Getter @Setter
	private String stopDateStr;//截至日期Str
	@Getter @Setter
	private String openAccountCertificate;//开户证实书
	@Getter @Setter
	private Double expectedInterest;//预计利息
	@Getter @Setter
	private String floatAnnualRate;//浮动年利率（%）
	@Getter @Setter
	private Long extractDate;//提取日期
	@Getter @Setter
	private String extractDateStr;//提取日期Str
	@Getter @Setter
	private Double realInterest;//实际利息
	@Getter @Setter
	private Double realInterestRate;//实际利率


	//附件相关
	@Getter @Setter
	private String sourceId;
//	@Getter @Setter
//	private Sm_AttachmentForm[] attachmentList;

	//接收字段
	@Getter @Setter
	private String listDateStrEnd; //结束日期
	@Getter @Setter
	private Long listDateStrEndLon;//结束日期long
	
	@Getter @Setter
	private Integer calculationRule;//计算规则
	
	@Getter @Setter
	private String remark;//备注
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}
