package zhishusz.housepresell.controller.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Sm_User;

/*
 * Form表单：签章
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_SignatureForm extends NormalActionForm
{
	private static final long serialVersionUID = -4311778945885551468L;
	
	@Getter @Setter
	private Sm_User sm_User;//当前用户
	@Getter @Setter
	private String busi_code;//业务编码
	@Getter @Setter
	private Long tableId;//单据主键
	@Getter @Setter
	private String sourceType;//附件配置eCode
	@Getter @Setter
	private String signaturePath;//签章后的文件路径
	@Getter @Setter
	private Long signatureAttachmentTableId;//附件主键
	@Getter @Setter
	private String signaturePrefixPath;//签章前的文件路径OSS（网络路径）
	@Getter @Setter
	private MultipartFile files;//文件流
	@Getter @Setter
	private String fileName;//文件名
	@Getter @Setter
	private String urlPath;//文件网络路径
	
}
