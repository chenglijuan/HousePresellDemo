package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 预警消息推送From
 * @ClassName:  Tg_EarlyWarningFrom   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月16日 下午5:59:24   
 * @version V1.0 
 *
 */
@ToString(callSuper=true)
public class Tg_EarlyWarningFrom extends NormalActionForm
{	
	private static final long serialVersionUID = -5354935087881963430L;

	@Getter @Setter @IFieldAnnotation(remark="主键ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="关联数据表ID")
	private String orgDataId;
	
	@Getter @Setter @IFieldAnnotation(remark="关联数据表eCode-冗余")
	private String orgDataCode;
	
	@Getter @Setter
	private String otherId; //其它主键
}