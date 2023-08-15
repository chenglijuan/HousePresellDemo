package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢-扩展信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BuildingExtendInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = -4118770593191017847L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼栋
	@Getter @Setter
	private Long buildingInfoId;//关联楼栋-Id
	@Getter @Setter
	private String presellState;//预售状态
	@Getter @Setter
	private String eCodeOfPresell;//预售证号
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
	private Long presellDate;//预售日期
	@Getter @Setter
	private String limitState;//限制状态
	@Getter @Setter
	private String escrowState;//托管状态
	@Getter @Setter
	private Integer landMortgageState;//土地抵押状态
	@Getter @Setter
	private String landMortgagor;//土地抵押权人
	@Getter @Setter
	private Double landMortgageAmount;//土地抵押金额
	@Getter @Setter
	private Boolean isSupportPGS;//是否是“支付保证业务PaymentGuaranteeService”楼幢
	
	public String geteCodeOfPresell()
	{
		return eCodeOfPresell;
	}
	public void seteCodeOfPresell(String eCodeOfPresell)
	{
		this.eCodeOfPresell = eCodeOfPresell;
	}
}
