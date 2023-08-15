package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：收入预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_IncomeExpDepositForecastBatchForm extends NormalActionForm
{
	private static final long serialVersionUID = 6107904488386578760L;

	@Getter @Setter
	private Tg_IncomeExpDepositForecastForm[] incomeExpDepositForecastList;
	
}
