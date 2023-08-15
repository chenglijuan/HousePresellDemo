package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回参数模型
 * @author ZS
 *
 */
public class ResponseContractResult {

	@Getter
	@Setter
	private String success;
	@Getter
	@Setter
	private ResponseContract result;
	
	@Override
	public String toString() {
		return "ResponseResult [success=" + success + ", result=" + result + "]";
	}
	
	

}
