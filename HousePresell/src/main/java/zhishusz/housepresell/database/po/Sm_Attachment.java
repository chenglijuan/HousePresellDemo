package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：附件
 * */
@ITypeAnnotation(remark="附件")
public class Sm_Attachment implements Serializable
{
	private static final long serialVersionUID = 2102518318347724903L;

//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="排序信息")
	private String sortNum;
	
	@Getter @Setter @IFieldAnnotation(remark="银行主键")
	private Long bankId;
	
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="关联附件配置表")
	private Sm_AttachmentCfg attachmentCfg;
}
