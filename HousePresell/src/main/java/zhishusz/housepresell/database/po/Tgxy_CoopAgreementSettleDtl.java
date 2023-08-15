package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：三方协议结算-字表
 * */
@ITypeAnnotation(remark="三方协议结算-子表")
public class Tgxy_CoopAgreementSettleDtl implements Serializable ,IApprovable
{
	private static final long serialVersionUID = 6999722847773978015L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@IFieldAnnotation(remark="三方协议号")
	private String eCode;//三方协议号

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
	
	@Getter @Setter @IFieldAnnotation(remark="关联主表")
    private Tgxy_CoopAgreementSettle mainTable;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议")
    private Tgxy_TripleAgreement tgxy_TripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="协议日期")
    private String agreementDate;
	
	@Getter @Setter @IFieldAnnotation(remark="出卖人")
    private String seller;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人")
    private String buyer;
	
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="单元")
	private Empj_UnitInfo unitInfo;
   
	@Getter @Setter @IFieldAnnotation(remark="户室")
	private Empj_HouseInfo houseInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="单元名称")
	private String unitInfoName;
	
	@Getter @Setter @IFieldAnnotation(remark="户室名称")
	private String houseInfoName;

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	@Override
	public String getSourceType() {
		return null;
	}

	@Override
	public Long getSourceId() {
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		return null;
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
