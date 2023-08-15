package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZS_XSZ
 * 业务关联记录表 form
 */
@ToString(callSuper=true)
public class Sm_BusinessRecordForm extends NormalActionForm
{
	
	private static final long serialVersionUID = -4650147617456581380L;
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
	private String busiCode;//业务编码
	@Getter @Setter
	private String codeOfBusiness;//业务编号
	@Getter @Setter
	private Sm_User userBegin;//单据创建人
	@Getter @Setter
	private Long dateBegin;//单据创建时间
	@Getter @Setter
	private Emmp_CompanyInfo companyInfo;//关联开发企业
	@Getter @Setter
	private Long companyId;//关联机构-Id
	@Getter @Setter
	private String codeOfCompany;//开发企业编号
	@Getter @Setter
	private Empj_ProjectInfo projectInfo;//关联项目
	@Getter @Setter
	private Long projectId;////关联项目-Id
	@Getter @Setter
	private String codeOfProject;//项目编号
	@Getter @Setter
	private Sm_CityRegionInfo cityRegion;//关联区域
	@Getter @Setter
	private Long cityRegionId;//关联区域-Id
	@Getter @Setter
	private String codeOfCityRegion;//区域编号
	@Getter @Setter
	private String theNameOfCityRegion;//区域名称
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String codeOfBuilding;//楼幢编号
	@Getter @Setter
	private Tgxy_TripleAgreement tripleAgreement;//关联三方协议
	@Getter @Setter
	private Long tripleAgreementId;//关联三方协议-Id
	@Getter @Setter
	private String codeOfTripleAgreement;//三方协议编号
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Sm_ApprovalProcess_AF approvalProcess_AF;//审批流申请单
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
