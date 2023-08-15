package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢-单元
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_UnitInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 7180320055696251537L;
	
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
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private Double upfloorNumber;//地上楼层数
	@Getter @Setter
	private Integer upfloorHouseHoldNumber;//地上每层户数
	@Getter @Setter
	private Double downfloorNumber;//地下楼层数
	@Getter @Setter
	private Integer downfloorHouseHoldNumber;//地下每层户数
	@Getter @Setter
	private Integer elevatorNumber;//电梯数
	@Getter @Setter
	private Boolean hasSecondaryWaterSupply;//有无二次供水
	@Getter @Setter
	private Integer secondaryWaterSupply;//有无二次供水
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long[] buildingIdArr;//楼幢ID数组
	@Getter @Setter
	private Long logId;//关联日志Id
	@Getter @Setter
	private String empj_HouseInfoListUpfloor;//关联日志Id
	@Getter @Setter
	private String empj_HouseInfoListDownfloor;//关联日志Id

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
}
