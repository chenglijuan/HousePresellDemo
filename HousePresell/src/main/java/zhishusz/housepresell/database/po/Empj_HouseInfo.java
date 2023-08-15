
package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：楼幢-户室
 * */
@ITypeAnnotation(remark="楼幢-户室")
public class Empj_HouseInfo implements Serializable,ILogable,IApprovable
{
	private static final long serialVersionUID = 7254384721393292651L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态 S_HouseBusiState")
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

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;

	@Getter @Setter @IFieldAnnotation(remark="关联单元")
	private Empj_UnitInfo unitInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="单元序号")
	private String unitNumber;
	
	@IFieldAnnotation(remark="楼幢单元")
	private String eCodeOfUnitInfo;
	
	@IFieldAnnotation(remark="预售系统户编号")
	private String eCodeFromPresellSystem;
	
	@IFieldAnnotation(remark="托管系统户编号")
	private String eCodeFromEscrowSystem;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="公安坐落")
	private String addressFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="物价备案价格")
	private Double recordPrice;
	
	@Getter @Setter @IFieldAnnotation(remark="预售系统物价备案价格最后一次同步时间",columnName="lastTSSRPToPS")
	private Long lastTimeStampSyncRecordPriceToPresellSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议结算状态",columnName="settlementSOfTA")
	private Integer settlementStateOfTripleAgreement;

	@Getter @Setter @IFieldAnnotation(remark="关联三方协议")
	private Tgxy_TripleAgreement tripleAgreement;
	
	@IFieldAnnotation(remark="预售证号-来源于楼栋")
	private String eCodeFromPresellCert;
	
	@Getter @Setter @IFieldAnnotation(remark="所在楼层")
	private Double floor;
	
	@Getter @Setter @IFieldAnnotation(remark="室号")
	private String roomId;
	
	@Getter @Setter @IFieldAnnotation(remark="室号名称")
	private String theNameOfRoomId;
	
	@Getter @Setter @IFieldAnnotation(remark="所在行，合并层数中的最高层，导入时维护")
	private Integer rowNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="所在列，该户室在同一rowNumber下排第几，导入时维护")
	private Integer colNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="该户室在该层满编情况下的下标，导入时维护")
	private Integer colIndex;
	
	@Getter @Setter @IFieldAnnotation(remark="合并楼层数，纵向合并数，导入时维护 ，默认为1")
	private Integer rowSpan;
	
	@Getter @Setter @IFieldAnnotation(remark="合并同层下的户室数，横向合并数，导入时维护 ，默认为1")
	private Integer colSpan;
	
	@Getter @Setter @IFieldAnnotation(remark="是否跃层")
	private Boolean isOverFloor;
	
	@Getter @Setter @IFieldAnnotation(remark="跃层数")
	private Integer overFloors;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋坐落")
	private String position;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋用途")
	private String purpose;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋性质")
	private String property;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String deliveryType;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积（预测）")
	private Double forecastArea;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积（实测）")
	private Double actualArea;
	
	@Getter @Setter @IFieldAnnotation(remark="套内建筑面积（㎡）")
	private Double innerconsArea;
	
	@Getter @Setter @IFieldAnnotation(remark="分摊建筑面积（㎡）")
	private Double shareConsArea;
	
	@Getter @Setter @IFieldAnnotation(remark="使用面积（㎡）")
	private Double useArea;
	
	@Getter @Setter @IFieldAnnotation(remark="阳台面积（㎡）")
	private Double balconyArea;
	
	@Getter @Setter @IFieldAnnotation(remark="层高")
	private Double heigh;
	
	@Getter @Setter @IFieldAnnotation(remark="户型")
	private String unitType;
	
	@Getter @Setter @IFieldAnnotation(remark="室")
	private Integer roomNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="厅")
	private Integer hallNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="厨")
	private Integer kitchenNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="卫")
	private Integer toiletNumber;
	
	@IFieldAnnotation(remark="原房屋编号")
	private String eCodeOfOriginalHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="是否开户")
	private Boolean isOpen;
	
	@Getter @Setter @IFieldAnnotation(remark="是否预售")
	private Boolean isPresell;
	
	@Getter @Setter @IFieldAnnotation(remark="是否抵押")
	private Boolean isMortgage;
	
	@Getter @Setter @IFieldAnnotation(remark="限制状态 S_LimitState")
	private Integer limitState;
	
	@IFieldAnnotation(remark="不动产单元号")
	private String eCodeOfRealBuidingUnit;
	
	@IFieldAnnotation(remark="业务管理号1-预留字段")
	private String eCodeOfBusManage1;
	
	@IFieldAnnotation(remark="业务管理号2-预留字段")
	private String eCodeOfBusManage2;
	
	@IFieldAnnotation(remark="测绘编号")
	private String eCodeOfMapping;
	
	@IFieldAnnotation(remark="图幅号(分层分户图号)")
	private String eCodeOfPicture;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	
	@Getter @Setter @IFieldAnnotation(remark="关联预售系统买卖合同")
	private Tgxy_ContractInfo contractInfo;
	/**
	 * 1.已搭建
	 * 2.已批准预售
	 * 3.合同已签订
	 * 4.合同已备案
	 * 5.已办产权
	 */
	@Getter @Setter @IFieldAnnotation(remark="房屋状态")
	private Integer theHouseState;// 详细见： Document\原始需求资料\常量信息汇总 41
	
	@IFieldAnnotation(remark="关联日志Id")
	private Long logId;
	
	@Getter @Setter @IFieldAnnotation(remark="是否限制 0否1是")
	private String isLimit;
	
	@Getter @Setter @IFieldAnnotation(remark="是否处置 0否1是")
	private String isManagement;
	
	@Getter @Setter @IFieldAnnotation(remark="是否查封 0否1是")
	private String isSequestration;
	
	@Getter @Setter @IFieldAnnotation(remark="关联房屋拓展表")
	private Empj_HouseExtendInfo houseExtendInfo;
	
	@Override
	public Long getLogId()
	{
		return logId;
	}
	@Override
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}
	@Override
	public String getLogData()
	{
		// TODO Auto-generated method stub
		return null;
	}
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
	public String geteCodeOfUnitInfo() {
		return eCodeOfUnitInfo;
	}
	public void seteCodeOfUnitInfo(String eCodeOfUnitInfo) {
		this.eCodeOfUnitInfo = eCodeOfUnitInfo;
	}
	public String geteCodeFromPresellSystem() {
		return eCodeFromPresellSystem;
	}
	public void seteCodeFromPresellSystem(String eCodeFromPresellSystem) {
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}
	public String geteCodeFromEscrowSystem() {
		return eCodeFromEscrowSystem;
	}
	public void seteCodeFromEscrowSystem(String eCodeFromEscrowSystem) {
		this.eCodeFromEscrowSystem = eCodeFromEscrowSystem;
	}
	public String geteCodeFromPublicSecurity() {
		return eCodeFromPublicSecurity;
	}
	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	public String geteCodeFromPresellCert() {
		return eCodeFromPresellCert;
	}
	public void seteCodeFromPresellCert(String eCodeFromPresellCert) {
		this.eCodeFromPresellCert = eCodeFromPresellCert;
	}
	public String geteCodeOfOriginalHouse() {
		return eCodeOfOriginalHouse;
	}
	public void seteCodeOfOriginalHouse(String eCodeOfOriginalHouse) {
		this.eCodeOfOriginalHouse = eCodeOfOriginalHouse;
	}
	public String geteCodeOfRealBuidingUnit() {
		return eCodeOfRealBuidingUnit;
	}
	public void seteCodeOfRealBuidingUnit(String eCodeOfRealBuidingUnit) {
		this.eCodeOfRealBuidingUnit = eCodeOfRealBuidingUnit;
	}
	public String geteCodeOfBusManage1() {
		return eCodeOfBusManage1;
	}
	public void seteCodeOfBusManage1(String eCodeOfBusManage1) {
		this.eCodeOfBusManage1 = eCodeOfBusManage1;
	}
	public String geteCodeOfBusManage2() {
		return eCodeOfBusManage2;
	}
	public void seteCodeOfBusManage2(String eCodeOfBusManage2) {
		this.eCodeOfBusManage2 = eCodeOfBusManage2;
	}
	public String geteCodeOfMapping() {
		return eCodeOfMapping;
	}
	public void seteCodeOfMapping(String eCodeOfMapping) {
		this.eCodeOfMapping = eCodeOfMapping;
	}
	public String geteCodeOfPicture() {
		return eCodeOfPicture;
	}
	public void seteCodeOfPicture(String eCodeOfPicture) {
		this.eCodeOfPicture = eCodeOfPicture;
	}
	
	@Override
	public String getSourceType()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long getSourceId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey()
	{
		// TODO Auto-generated method stub
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
