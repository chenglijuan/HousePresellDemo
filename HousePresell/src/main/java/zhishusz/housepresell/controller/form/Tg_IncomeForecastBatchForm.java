package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：收入预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_IncomeForecastBatchForm extends NormalActionForm
{
	private static final long serialVersionUID = -3218057227009552146L;
	@Getter @Setter
	private Tg_IncomeForecastForm[] incomeForecastList;
	
}
