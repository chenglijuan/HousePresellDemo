package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：高拍仪保存图片
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class SavePicForm extends NormalActionForm
{
	
	private static final long serialVersionUID = 1387143177224329679L;

	@Getter @Setter
	private String base64List;//图片base64编码
}
