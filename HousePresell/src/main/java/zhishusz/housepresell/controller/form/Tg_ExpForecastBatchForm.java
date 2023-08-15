package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：支出预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_ExpForecastBatchForm extends NormalActionForm
{
	@Getter @Setter
	private Tg_ExpForecastForm[] expForecastList;
	
}
