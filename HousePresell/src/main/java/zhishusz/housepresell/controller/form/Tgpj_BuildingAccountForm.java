package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢账户
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_BuildingAccountForm extends NormalActionForm
{
	private static final long serialVersionUID = 6157148485034560101L;
	
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
	private Sm_User userUpdate;//最后修改人人
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
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private String escrowStandard;//托管标准
	@Getter @Setter
	private Double escrowArea;//托管面积
	@Getter @Setter
	private Double buildingArea;//建筑面积
	@Getter @Setter
	private Double orgLimitedAmount;//初始受限额度（元）
	@Getter @Setter
	private String currentFigureProgress;//当前形象进度
	@Getter @Setter
	private Double currentLimitedRatio;//当前受限比例（%）
	@Getter @Setter
	private Double nodeLimitedAmount;//节点受限额度（元）
	@Getter @Setter
	private Double totalGuaranteeAmount;//累计可计入保证金额（元）
	@Getter @Setter
	private Double cashLimitedAmount;//现金受限额度（元）
	@Getter @Setter
	private Double effectiveLimitedAmount;//有效受限额度（元）
	@Getter @Setter
	private Double totalAccountAmount;//总入账金额（元）
	@Getter @Setter
	private Double spilloverAmount;//溢出金额（元）
	@Getter @Setter
	private Double payoutAmount;//已拨付金额（元）
	@Getter @Setter
	private Double appliedNoPayoutAmount;//已申请未拨付金额（元）
	@Getter @Setter
	private Double applyRefundPayoutAmount;//已申请退款未拨付金额（元）
	@Getter @Setter
	private Double refundAmount;//已退款金额（元）
	@Getter @Setter
	private Double currentEscrowFund;//当前托管余额（元）
	@Getter @Setter
	private Double allocableAmount;//可划拨金额（元）
	@Getter @Setter
	private Double recordAvgPriceOfBuildingFromPresellSystem;//预售系统楼幢住宅备案均价
	@Getter @Setter
	private Double recordAvgPriceOfBuilding;//楼幢住宅备案均价
	@Getter @Setter
	private Long logId;//关联日志Id
	@Getter @Setter
	private Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl;
	@Getter @Setter
	private Long bldLimitAmountVerDtlId;//关联受限额度版本Id
	
	
	//前台传参
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
	
	
}
