package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：三方协议结算-主表
 * */
@ITypeAnnotation(remark="三方协议结算-主表")
public class Tgxy_CoopAgreementSettle implements Serializable ,IApprovable
{
	private static final long serialVersionUID = -7877313881528109130L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

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

	@Getter @Setter @IFieldAnnotation(remark="结算日期")
	private String signTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="代理公司")
	private Emmp_CompanyInfo agentCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="公司名称")
	private String companyName;
	
	@Getter @Setter @IFieldAnnotation(remark="申请结算日期")
	private String applySettlementDate;
	
	@Getter @Setter @IFieldAnnotation(remark="结算开始日期")
	private String startSettlementDate;
	
	@Getter @Setter @IFieldAnnotation(remark="结算截至日期")
	private String endSettlementDate;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议号有效份数")
	private Integer protocolNumbers;
	
	@Getter @Setter @IFieldAnnotation(remark="结算状态")
	private Integer settlementState;
	
	@Getter @Setter @IFieldAnnotation(remark="结算状态")
	private String recordName;					// 显示字段，不存入数据库
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议过滤前份数")
	private Integer beforeNumbers;

	
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

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
}
