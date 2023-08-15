package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_CoopAgreementSettleDtlForm extends NormalActionForm
{
	private static final long serialVersionUID = 1908277602554461067L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private String userUpdate;//修改人
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
	private Tgxy_CoopAgreementSettle mainTable;//关联主表 
	@Getter @Setter
	private Long mainTableId;//关联主表 -Id
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private String agreementDate;//协议日期
	@Getter @Setter
	private String seller;//出卖人
	@Getter @Setter
	private String buyer;//买受人
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼幢
	@Getter @Setter
	private Long buildingInfoId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private Empj_UnitInfo unitInfo;//单元
	@Getter @Setter
	private Long unitInfoId;//单元-Id
	@Getter @Setter
	private Empj_HouseInfo houseInfo;//户室
	@Getter @Setter
	private Long houseInfoId;//户室-Id
	@Getter @Setter
	private Tgxy_TripleAgreement tgxy_TripleAgreement;//三方协议
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfBuilding()
	{
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding)
	{
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}
