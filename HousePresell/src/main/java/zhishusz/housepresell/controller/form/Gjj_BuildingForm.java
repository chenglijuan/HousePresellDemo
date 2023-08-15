package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;

/*
 * Form表单：参数定义
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Gjj_BuildingForm extends NormalActionForm
{
	private static final long serialVersionUID = 3691853774176879323L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private String empjBuildingId;//托管楼栋id
	@Getter @Setter
	private String gjjBuildingId;//公积金楼栋id
}
