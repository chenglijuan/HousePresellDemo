package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：附件
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_AttachmentForm extends NormalActionForm
{
	private static final long serialVersionUID = 6935756896244767643L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private String sourceType;//资源类型
	@Getter @Setter
	private String sourceId;//资源Id
	/*@Getter @Setter
	private String busiType;//关联业务类型
*/	@Getter @Setter
	private String fileType;//文件类型
	@Getter @Setter
	private Integer totalPage;//文件总页数
	@Getter @Setter
	private String theLink;//链接
	@Getter @Setter
	private String theSize;//资源大小，单位：KB
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String md5Info;//文件MD5信息
	
	@Getter @Setter @IFieldAnnotation(remark="关联附件配置表")
    private Sm_AttachmentCfg attachmentCfg;
	
	
	
	
	
	//接收页面传递参数
	@Getter @Setter
	private String smAttachmentList;//附件参数
}
