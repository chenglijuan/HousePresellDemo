package zhishusz.housepresell.exportexcelvo;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：三方协议结算-主表
 * */

public class Tgxy_CoopAgreementSettleExportExcelVO
{
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	@Getter @Setter 
	private Integer ordinal;//序号
	@Getter @Setter
	private String companyName; // 公司名称	
	@Getter @Setter
	private String applySettlementDate; // 申请结算日期  	
	@Getter @Setter
	private String startSettlementDate; // 结算开始日期	
	@Getter @Setter
	private String endSettlementDate;// 结算截至日期
	@Getter @Setter
	private Integer protocolNumbers; // 三方协议号有效份数
	@Getter @Setter
	private String userUpdate; // 操作人
	@Getter @Setter
	private String lastUpdateTimeStamp; // 操作日期
	@Getter @Setter
	private String userRecord;// 结算人
	@Getter @Setter
	private String signTimeStamp;// 结算日期
	@Getter @Setter 
	private String settlementState;// 结算状态
		
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
