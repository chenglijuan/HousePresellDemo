package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：推送给财务系统-凭证
 * */
@ITypeAnnotation(remark="推送给财务系统-凭证")
public class Tgpf_AccVoucher implements Serializable
{
	private static final long serialVersionUID = -7692938580224726781L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;//凭证状态【自动写入枚举：0-未推送、1-已推送】
	
	@IFieldAnnotation(remark="编号")
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
	
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="业务类型 :入账、拨付")
	private String theType;
	
	@Getter @Setter @IFieldAnnotation(remark="总笔数")
	private Integer tradeCount;
	
	@Getter @Setter @IFieldAnnotation(remark="总金额")
	private Double totalTradeAmount;

	@Getter @Setter @IFieldAnnotation(remark="凭证内容")
	private String contentJson;
	
	@Getter @Setter @IFieldAnnotation(remark="资金拨付日期")
	private String payoutTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="公司")
	private Emmp_CompanyInfo company;
	
	@Getter @Setter @IFieldAnnotation(remark="企业名称-冗余")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称-冗余")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="日终结算状态")
	private Integer DayEndBalancingState;
	
	@Getter @Setter @IFieldAnnotation(remark="托管银行")
	private Tgxy_BankAccountEscrowed bankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账号",columnName="theAccountOfBAE")
	private String theAccountOfBankAccountEscrowed;
	
	@Getter @Setter @IFieldAnnotation(remark="账户支付金额")
	private Double payoutAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢信息")
	private Empj_BuildingInfo building;
	
	@Getter @Setter @IFieldAnnotation(remark="申请-用款-主表")
	private Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF;
	
	@Getter @Setter @IFieldAnnotation(remark="关联类型（0-一般拨付，1-特殊拨付）")
	private Integer relatedType;//关联类型（0-一般拨付，1-特殊拨付）
	
	@Getter @Setter @IFieldAnnotation(remark="关联主键")
	private Long relatedTableId;//关联主键
	
	@Getter @Setter @IFieldAnnotation(remark="推送日期")
	private String sendTime;//推送日期
	
	@Getter @Setter @IFieldAnnotation(remark="凭证号")
	private String vou_No;//凭证号

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
