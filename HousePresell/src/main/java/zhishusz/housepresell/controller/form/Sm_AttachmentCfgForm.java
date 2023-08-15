package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：附件配置
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_AttachmentCfgForm extends NormalActionForm
{
	private static final long serialVersionUID = 4648100646832679979L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
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
	private String sourceId;//业务类型id
	@Getter @Setter
	private String theName;//附件类型名称
	@Getter @Setter
	private String acceptFileType;//可接受文件类型
	@Getter @Setter
	private Integer acceptFileCount;//可接受文件数量
	@Getter @Setter
	private Integer maxFileSize;//单个文件大小限制（最大）
	@Getter @Setter
	private Integer minFileSize;//单个文件大小限制（最小）
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Boolean isImage;//是否是图片
	@Getter @Setter
	private Boolean isNeeded;//是否必须
	@Getter @Setter
	private String listType;//附件列表类型
	@Getter @Setter
	private String paramenterType;//枚举值类型
	@Getter @Setter
	private String approvalCode;//审批业务编码
	@Getter @Setter
	private String isCfgSignature;//是否需要签章 0：否 1：是
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
