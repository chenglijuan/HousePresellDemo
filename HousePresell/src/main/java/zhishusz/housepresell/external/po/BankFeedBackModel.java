package zhishusz.housepresell.external.po;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 接受支付结果推送信息model
 * @author Administrator
 *
 */
@Getter
@Setter
public class BankFeedBackModel {
	
	private List<BankFeedBackDtlModel> data;

}
