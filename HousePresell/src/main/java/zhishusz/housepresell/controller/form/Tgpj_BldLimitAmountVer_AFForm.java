package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_BldLimitAmountVer_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = -6445374382299571987L;
	
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
	private String theName;//版本名称
	@Getter @Setter
	private String theVerion;//版本号
	@Getter @Setter
	private String theType;//交付类型
	@Getter @Setter
	private String limitedAmountInfoJSON;//受限额度数据-JSON格式
	@Getter @Setter
	private Long beginExpirationDate;//启用日期
	@Getter @Setter
	private String beginExpirationDateString;//启用日期String
	@Getter @Setter
	private Long endExpirationDate;//停用日期
	@Getter @Setter
	private String endExpirationDateString;//停用日期String
	@Getter @Setter
	private Integer isUsing;//0:启用，1：未启用
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Tgpj_BldLimitAmountVer_AFDtl[] nodeVersionList;

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
