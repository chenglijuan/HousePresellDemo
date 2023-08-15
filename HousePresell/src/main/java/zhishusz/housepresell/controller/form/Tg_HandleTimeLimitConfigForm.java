package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：办理时限配置表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_HandleTimeLimitConfigForm extends NormalActionForm
{
	private static final long serialVersionUID = -9095899598906687714L;
	
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
	private String theType;//业务类型
	@Getter @Setter
	private String completionStandard;//办理完结标准
	@Getter @Setter
	private Integer limitDayNumber;//办理时限天数（以工作日计算）
	@Getter @Setter
	private Long roleId;//角色
	@Getter @Setter
	private String lastCfgUser;//配置人
	@Getter @Setter
	private Long lastCfgTimeStamp;//配置日期
	@Getter @Setter
	private Tg_HandleTimeLimitConfigForm[] tg_HandleTimeLimitConfigs;
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
