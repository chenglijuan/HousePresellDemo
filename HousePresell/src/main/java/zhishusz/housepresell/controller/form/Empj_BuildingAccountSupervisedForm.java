package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BuildingAccountSupervisedForm extends NormalActionForm
{
	private static final long serialVersionUID = -8555750110619610230L;
	
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
	private Sm_User userUpdate;//最后修改人
	@Getter @Setter
	private Long userUpdateId;//最后修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼幢
	@Getter @Setter
	private Long buildingInfoId;//关联楼幢-Id
	@Getter @Setter
	private Tgpj_BankAccountSupervised bankAccountSupervised;//关联监管账号
	@Getter @Setter
	private Long bankAccountSupervisedId;//关联监管账号-Id
	@Getter @Setter
	private Long beginTimeStamp;//启用日期
	@Getter @Setter
	private String beginTimeStampString;//启用日期
	@Getter @Setter
	private Long endTimeStamp;//终止日期
	@Getter @Setter
	private String endTimeStampString;//终止日期
	@Getter @Setter
	private Integer isUsing;//是否启用，0：是，1：否
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
