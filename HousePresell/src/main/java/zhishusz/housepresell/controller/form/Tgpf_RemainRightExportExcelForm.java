package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：导出：留存权益
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRightExportExcelForm extends NormalActionForm
{
	private static final long serialVersionUID = 1667831190814654399L;
	@Getter @Setter
	private Long[] buildingRemainRightLogIds;//楼幢留存权益-ids
}
