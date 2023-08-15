package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：退房退款-贷款已结清
 * */
@ITypeAnnotation(remark="退房退款-贷款已结清")
public class Tgpf_RefundInfo implements Serializable,IApprovable
{
	private static final long serialVersionUID = -273263718411658414L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="乐观锁")
	private Long version;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
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

	@Getter @Setter @IFieldAnnotation(remark="退款申请编号")
	private String refundCode;//系统生成,生成规则：区域（2）+标志（1）（2-退款）+银行编号（2）+年月日（6）+按天流水（4）+验证码（1）
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议")
	private Tgxy_TripleAgreement tripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContractRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="所属项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢-户室")
	private Empj_BuildingInfo building;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋坐落")
	private String positionOfBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人名称")
	private String theNameOfBuyer;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人证件号码")
	private String certificateNumberOfBuyer;
	
	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String contactPhoneOfBuyer;
	
	@Getter @Setter @IFieldAnnotation(remark="主借款人")
	private String theNameOfCreditor;//TODO 与买受人有什么区别？信息哪里来？
	
	@Getter @Setter @IFieldAnnotation(remark="合同金额（元）")
	private Double fundOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="总贷款金额（元）")
	private Double fundFromLoan;
	
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额（元）")
	private Double retainRightAmount;//【开发企业业务人员提交申请时自动带出（托管账户中该户的留存权益金额）】
	
	@Getter @Setter @IFieldAnnotation(remark="到期权益金额（元）")
	private Double expiredAmount;//（超出当前托管额度，但是还未申请资金划拨部分资金的分摊权益金额）
	
	@Getter @Setter @IFieldAnnotation(remark="未到期权益金额（元）")
	private Double unexpiredAmount;//（当前受限额度内的分摊权益金额）
	
	@Getter @Setter @IFieldAnnotation(remark="本次退款金额（元）")
	private Double refundAmount;//【默认为留存权益总金额，不可修改】
	
	@Getter @Setter @IFieldAnnotation(remark="收款人类型")
	private Integer receiverType;//【枚举：1-买受人、2-指定收款人】
	
	@Getter @Setter @IFieldAnnotation(remark="收款人名称")
	private String receiverName;//【自动带出主借款人，可修改】
	
	@Getter @Setter @IFieldAnnotation(remark="收款银行")
	private String receiverBankName;
	
	@Getter @Setter @IFieldAnnotation(remark="退房退款类型（贷款已结清、贷款未结清）")
	private String refundType;
	
	@Getter @Setter @IFieldAnnotation(remark="收款账号")
	private String receiverBankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="实际退款金额")
	private Double actualRefundAmount;//【金额默认本次退款金额，财务统筹人员可以拆分为多个托管账号】
	
	@Getter @Setter @IFieldAnnotation(remark="退款银行名称")
	private String refundBankName;
	
	@Getter @Setter @IFieldAnnotation(remark="退款账号")
	private String refundBankAccount;
	
	@Getter @Setter @IFieldAnnotation(remark="退款日期")
	private String refundTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="退款账号")
	private Tgxy_BankAccountEscrowed theBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="推送状态 0-发起中 1-成功 2-失败")
	private String pushState;
	
	@Getter @Setter @IFieldAnnotation(remark="反馈信息")
	private String pushInfo;

	@Override
	public String getSourceType() {
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String>  peddingApprovalkey = new ArrayList<>();
		peddingApprovalkey.add("busiState");
		peddingApprovalkey.add("theNameOfProject");
		//TODO  存放需要审批的字段
		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}

	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}

	public String geteCodeOfContractRecord() {
		return eCodeOfContractRecord;
	}

	public void seteCodeOfContractRecord(String eCodeOfContractRecord) {
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}
}
