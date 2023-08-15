package zhishusz.housepresell.controller.form.extra;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Administrator
 *
 */
public class BldLimitAmountForm implements Serializable {

	private static final long serialVersionUID = 2269023696432402849L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Long buildingId;//楼幢ID
	@Getter @Setter
	private Long limitedId;//形象进度ID
	
	@Getter @Setter
	private List<BldLimitAmountAttachementForm> attachementList;//附件信息
	
}
