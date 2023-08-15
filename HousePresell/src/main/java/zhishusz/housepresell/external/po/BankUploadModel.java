package zhishusz.housepresell.external.po;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 网银数据接收模型
 * @author Administrator
 *
 */
@Getter
@Setter
public class BankUploadModel {
	
	private String key;//key
	
	private Long sendDate;//发送时间
	
	private String startDate;//交易起始时间
	
	private String endDate;//交易结束时间
	
	private List<BankUploadDtlModel> data;//网银数据明细

}
