package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgDataInfo;
import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：附件配置
 * */
@ITypeAnnotation(remark="附件配置")
public class Sm_AttachmentCfg implements Serializable
{
	private static final long serialVersionUID = -4918214260926098235L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="业务类型")
	private String busiType;//如：机构信息
	
	@Getter @Setter @IFieldAnnotation(remark="附件类型名称")
	private String theName;//如：营业执照
	
	@Getter @Setter @IFieldAnnotation(remark="可接受文件类型")
	private String acceptFileType;//类型，可复选：图片、压缩文件、WORD、EXCEL、其他
									//--》accept:"image/jpeg,image/gif,image/png"

	@Getter @Setter @IFieldAnnotation(remark="可接受文件数量")
	private Integer acceptFileCount;//默认为10

	@Getter @Setter @IFieldAnnotation(remark="单个文件大小限制（最大）")
	private Integer maxFileSize;//默认为0，单位KB

	@Getter @Setter @IFieldAnnotation(remark="单个文件大小限制（最小）")
	private Integer minFileSize;//默认51200（50MB），单位KB
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;//页面备注提示，如：jpg、png格式的图片，单张不超过2M

	@Getter @Setter @IFieldAnnotation(remark="是否是图片")
	private Boolean isImage;//前端页面逻辑需要
	
	@Getter @Setter @IFieldAnnotation(remark="是否必须")
	private Boolean isNeeded;
	
	@Getter @Setter @IFieldAnnotation(remark="附件列表类型")
	private String listType;//前端页面需要，可选值："picture-card","text"
	
	@Getter @Setter @IFieldAnnotation(remark="参数名")
	private String basetheName;
	
	@Getter @Setter @IFieldAnnotation(remark="文件是否需要签章 0：否 1：是 ")
	private String isCfgSignature;
	
	@Getter @Setter @IFieldAnnotation(remark="额外配置信息 ")
	private Sm_AttachmentCfgDataInfo data;
	
	//前台接收参数
	@Getter @Setter 
	private List<Sm_Attachment> smAttachmentList;//附件

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
