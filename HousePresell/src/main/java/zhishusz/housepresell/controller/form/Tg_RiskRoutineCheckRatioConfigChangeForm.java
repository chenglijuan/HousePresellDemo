package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：风控例行抽查比例配置变更表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_RiskRoutineCheckRatioConfigChangeForm
{
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Long roleId;//角色ID
	@Getter @Setter
	private Integer theRatio;//抽查比例（%）
}
