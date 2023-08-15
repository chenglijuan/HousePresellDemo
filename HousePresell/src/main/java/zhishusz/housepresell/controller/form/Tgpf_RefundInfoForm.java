package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：退房退款-贷款已结清
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RefundInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 9174096254020947942L;
	
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
	private Tgxy_TripleAgreement tripleAgreement;//三方协议
	@Getter @Setter
	private Long tripleAgreementId;//三方协议-Id
	@Getter @Setter
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private String eCodeOfContractRecord;//合同备案号
	@Getter @Setter
	private Empj_ProjectInfo project;//所属项目
	@Getter @Setter
	private Long projectId;//所属项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称
	@Getter @Setter
	private Empj_BuildingInfo building;//楼幢-户室
	@Getter @Setter
	private Long buildingId;//楼幢-户室-Id
	@Getter @Setter
	private String positionOfBuilding;//房屋坐落
	@Getter @Setter
	private Tgxy_BuyerInfo buyer;//买受人
	@Getter @Setter
	private String buyerId;//买受人-Id
	@Getter @Setter
	private String theNameOfBuyer;//买受人名称
	@Getter @Setter
	private String certificateNumberOfBuyer;//买受人证件号码
	@Getter @Setter
	private String contactPhoneOfBuyer;//联系电话
	@Getter @Setter
	private String theNameOfCreditor;//主借款人
	@Getter @Setter
	private Double fundOfTripleAgreement;//合同金额（元）
	@Getter @Setter
	private Double fundFromLoan;//总贷款金额（元） 
	@Getter @Setter
	private Double retainRightAmount;//留存权益总金额（元）
	@Getter @Setter
	private Double expiredAmount;//到期权益金额（元）
	@Getter @Setter
	private Double unexpiredAmount;//未到期权益金额（元）
	@Getter @Setter
	private Double refundAmount;//本次退款金额（元）
	@Getter @Setter
	private Integer receiverType;//收款人类型
	@Getter @Setter
	private String receiverName;//收款人名称
	@Getter @Setter
	private String receiverBankName;//收款银行
	@Getter @Setter
	private String refundType;//退房退款类型（贷款已结清、贷款未结清）
	@Getter @Setter
	private String receiverBankAccount;//收款账号
	@Getter @Setter
	private Double actualRefundAmount;//实际退款金额
	@Getter @Setter
	private String refundBankName;//退款银行名称
	@Getter @Setter
	private String refundBankAccount;//退款账号
	@Getter @Setter
	private String refundTimeStamp;//退款日期
	@Getter @Setter
	private Long theBankAccountEscrowedId; //退款账号id
	
	//附件参数
	@Getter @Setter
	private String smAttachmentList;
	@Getter @Setter
	private Long houseId;//楼幢户的Id
	@Getter @Setter 
	private Empj_HouseInfo house;//关联户室
	@Getter @Setter 
	private String bAccountSupervised;//收款账号
	@Getter @Setter 
	private String bBankName;//收款银行
	@Getter @Setter 
	private String developCompanyName;//收款人
	
	//审批状态
	@Getter @Setter 
	private Long workflowId;//当前节点编号
	
	@Getter @Setter 
	private Integer refundStatus;//退款状态
	
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
