package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢-户室
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_HouseInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = -7959751804473801941L;
	
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
	private Long projectId;	//关联项目-Id
	@Getter @Setter
	private String projectName;	//项目名称
	@Getter @Setter
	private Empj_BuildingInfo building;//关联楼幢
	@Getter @Setter
	private Long buildingId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private Empj_UnitInfo unitInfo;//关联单元
	@Getter @Setter
	private Long unitInfoId;//关联单元-Id
	@Getter @Setter
	private String eCodeOfUnitInfo;//楼幢单元
	@Getter @Setter
	private String eCodeFromPresellSystem;//预售系统户编号
	@Getter @Setter
	private String eCodeFromEscrowSystem;//托管系统户编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private String addressFromPublicSecurity;//公安坐落
	@Getter @Setter
	private Double recordPrice;//物价备案价格
	@Getter @Setter
	private Long lastTimeStampSyncRecordPriceToPresellSystem;//预售系统物价备案价格最后一次同步时间
	@Getter @Setter
	private Integer settlementStateOfTripleAgreement;//三方协议结算状态
	@Getter @Setter
	private Tgxy_TripleAgreement tripleAgreement;//关联三方协议
	@Getter @Setter
	private Long tripleAgreementId;//关联三方协议-Id
	@Getter @Setter
	private String eCodeFromPresellCert;//预售证号-来源于楼栋
	@Getter @Setter
	private Double floor;//所在楼层
	@Getter @Setter
	private String roomId;//室号
	@Getter @Setter
	private String theNameOfRoomId;//室号名称
	@Getter @Setter
	private String ySpan;//户室纵向跨度
	@Getter @Setter
	private String xSpan;//户室横向跨度
	@Getter @Setter
	private Boolean isMerged;//是否合并
	@Getter @Setter
	private Integer mergedNums;//合并间数
	@Getter @Setter
	private Boolean isOverFloor;//是否跃层
	@Getter @Setter
	private Integer overFloors;//跃层数
	@Getter @Setter
	private String position;//房屋坐落
	@Getter @Setter
	private String purpose;//房屋用途
	@Getter @Setter
	private String property;//房屋性质
	@Getter @Setter
	private String deliveryType;//交付类型
	@Getter @Setter
	private Double forecastArea;//建筑面积（预测）
	@Getter @Setter
	private Double actualArea;//建筑面积（实测）
	@Getter @Setter
	private Double innerconsArea;//套内建筑面积（㎡）
	@Getter @Setter
	private Double shareConsArea;//分摊建筑面积（㎡）
	@Getter @Setter
	private Double useArea;//使用面积（㎡）
	@Getter @Setter
	private Double balconyArea;//阳台面积（㎡）
	@Getter @Setter
	private Double heigh;//层高
	@Getter @Setter
	private String unitType;//户型
	@Getter @Setter
	private Integer roomNumber;//室
	@Getter @Setter
	private Integer hallNumber;//厅
	@Getter @Setter
	private Integer kitchenNumber;//厨
	@Getter @Setter
	private Integer toiletNumber;//卫
	@Getter @Setter
	private String eCodeOfOriginalHouse;//原房屋编号
	@Getter @Setter
	private Boolean isOpen;//是否开户
	@Getter @Setter
	private Boolean isPresell;//是否预售
	@Getter @Setter
	private Boolean isMortgage;//是否抵押
	@Getter @Setter
	private Integer limitState;//限制状态
	@Getter @Setter
	private String eCodeOfRealBuidingUnit;//不动产单元号
	@Getter @Setter
	private String eCodeOfBusManage1;//业务管理号1-预留字段
	@Getter @Setter
	private String eCodeOfBusManage2;//业务管理号2-预留字段
	@Getter @Setter
	private String eCodeOfMapping;//测绘编号
	@Getter @Setter
	private String eCodeOfPicture;//图幅号(分层分户图号)
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long[] buildingIdArr;//楼幢ID数组
	@Getter @Setter
	private Long logId;//关联日志Id
	@Getter @Setter
	private String houseextraid;
	@Getter @Setter
	private String externalId;//外来数据关联主键
	
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
	public String geteCodeOfUnitInfo()
	{
		return eCodeOfUnitInfo;
	}
	public void seteCodeOfUnitInfo(String eCodeOfUnitInfo)
	{
		this.eCodeOfUnitInfo = eCodeOfUnitInfo;
	}
	public String geteCodeFromPresellSystem()
	{
		return eCodeFromPresellSystem;
	}
	public void seteCodeFromPresellSystem(String eCodeFromPresellSystem)
	{
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}
	public String geteCodeFromEscrowSystem()
	{
		return eCodeFromEscrowSystem;
	}
	public void seteCodeFromEscrowSystem(String eCodeFromEscrowSystem)
	{
		this.eCodeFromEscrowSystem = eCodeFromEscrowSystem;
	}
	public String geteCodeFromPublicSecurity()
	{
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity)
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	public String geteCodeFromPresellCert()
	{
		return eCodeFromPresellCert;
	}
	public void seteCodeFromPresellCert(String eCodeFromPresellCert)
	{
		this.eCodeFromPresellCert = eCodeFromPresellCert;
	}
	public String getySpan()
	{
		return ySpan;
	}
	public void setySpan(String ySpan)
	{
		this.ySpan = ySpan;
	}
	public String getxSpan()
	{
		return xSpan;
	}
	public void setxSpan(String xSpan)
	{
		this.xSpan = xSpan;
	}
	public String geteCodeOfOriginalHouse()
	{
		return eCodeOfOriginalHouse;
	}
	public void seteCodeOfOriginalHouse(String eCodeOfOriginalHouse)
	{
		this.eCodeOfOriginalHouse = eCodeOfOriginalHouse;
	}
	public String geteCodeOfRealBuidingUnit()
	{
		return eCodeOfRealBuidingUnit;
	}
	public void seteCodeOfRealBuidingUnit(String eCodeOfRealBuidingUnit)
	{
		this.eCodeOfRealBuidingUnit = eCodeOfRealBuidingUnit;
	}
	public String geteCodeOfBusManage1()
	{
		return eCodeOfBusManage1;
	}
	public void seteCodeOfBusManage1(String eCodeOfBusManage1)
	{
		this.eCodeOfBusManage1 = eCodeOfBusManage1;
	}
	public String geteCodeOfBusManage2()
	{
		return eCodeOfBusManage2;
	}
	public void seteCodeOfBusManage2(String eCodeOfBusManage2)
	{
		this.eCodeOfBusManage2 = eCodeOfBusManage2;
	}
	public String geteCodeOfMapping()
	{
		return eCodeOfMapping;
	}
	public void seteCodeOfMapping(String eCodeOfMapping)
	{
		this.eCodeOfMapping = eCodeOfMapping;
	}
	public String geteCodeOfPicture()
	{
		return eCodeOfPicture;
	}
	public void seteCodeOfPicture(String eCodeOfPicture)
	{
		this.eCodeOfPicture = eCodeOfPicture;
	}
}
