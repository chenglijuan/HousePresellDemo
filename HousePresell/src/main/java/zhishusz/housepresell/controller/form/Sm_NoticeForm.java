package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper=true)
public class Sm_NoticeForm extends NormalActionForm
{
	private static final long serialVersionUID = -1274938865449951355L;
	
	@Getter @Setter
	private String theTitle;//推送消息标题
	@Getter @Setter
	private String theMessage;//推送消息内容
	@Getter @Setter
	private Long tableId;//推送用户ID
}
