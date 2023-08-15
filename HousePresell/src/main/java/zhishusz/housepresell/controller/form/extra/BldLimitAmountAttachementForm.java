package zhishusz.housepresell.controller.form.extra;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.util.IFieldAnnotation;

/**
 * 附件信息
 * @author Administrator
 *
 */
public class BldLimitAmountAttachementForm implements Serializable {

	private static final long serialVersionUID = 2269023696432402849L;
	
	@Getter @Setter @IFieldAnnotation(remark="资源类型")
	private String sourceType;//关联附件类型
	
	@Getter @Setter @IFieldAnnotation(remark="资源Id")
	private String sourceId;//关联表id
	
	@Getter @Setter @IFieldAnnotation(remark="业务类型")
	private String busiType;//关联对象业务编码
	
	@Getter @Setter @IFieldAnnotation(remark="文件类型")
	private String fileType;// jpg、xls、等

	@Getter @Setter @IFieldAnnotation(remark="文件总页数")	
	private Integer totalPage;
	
	@Getter @Setter @IFieldAnnotation(remark="链接")
	private String theLink;//OSS-server 返回的路径
	
	@Getter @Setter @IFieldAnnotation(remark="资源大小，单位：KB")
	private String theSize;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="文件MD5信息")
	private String md5Info;

	
	
	
}
