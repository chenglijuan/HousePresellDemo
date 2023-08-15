package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ZS_XSZ
 * 业务关联记录表
 */
@ITypeAnnotation(remark="业务关联记录表")
public class Sm_BusinessRecord implements Serializable
{

	private static final long serialVersionUID = -6380523861259508297L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String ecode;

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;

	@Getter @Setter @IFieldAnnotation(remark="业务编号")
	private String codeOfBusiness;

	@Getter @Setter @IFieldAnnotation(remark="单据创建人")
	private Sm_User userBegin;

	@Getter @Setter @IFieldAnnotation(remark="单据创建时间")
	private Long dateBegin;

	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo companyInfo;

	@Getter @Setter @IFieldAnnotation(remark="开发企业编号")
	private String codeOfCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	private Empj_ProjectInfo projectInfo;

	@Getter @Setter @IFieldAnnotation(remark="项目编号")
	private String codeOfProject;

	@Getter @Setter @IFieldAnnotation(remark="关联区域")
	private Sm_CityRegionInfo cityRegion;

	@Getter @Setter @IFieldAnnotation(remark="区域编号")
	private String codeOfCityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="区域名称")
	private String theNameOfCityRegion;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;

	@Getter @Setter @IFieldAnnotation(remark="楼幢编号")
	private String codeOfBuilding;

	@Getter @Setter @IFieldAnnotation(remark="关联三方协议")
	private Tgxy_TripleAgreement tripleAgreement;

	@Getter @Setter @IFieldAnnotation(remark="三方协议编号")
	private String codeOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="审批流申请单")
	private Sm_ApprovalProcess_AF approvalProcess_AF;

}
