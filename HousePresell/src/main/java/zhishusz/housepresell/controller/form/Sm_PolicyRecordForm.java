package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：政策公告
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_PolicyRecordForm extends NormalActionForm
{
	
	private static final long serialVersionUID = -7050026961724346863L;
	
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
	@Getter @Setter// @IFieldAnnotation(remark="是否轮播 0：否 1：是")
	private String policyIsbrow;
	
	@Getter @Setter// @IFieldAnnotation(remark="是否置顶 0：否 1：是")
	private String policyIstop;
	
	@Getter @Setter// @IFieldAnnotation(remark="发布时间")
	private String policyDate;
	
	@Getter @Setter// @IFieldAnnotation(remark="政策类型 从基础参数表中读取")
	private String policyType;
	
	@Getter @Setter// @IFieldAnnotation(remark="政策类型标志 theValue")
	private String policyTypeCode;
	
	@Getter @Setter// @IFieldAnnotation(remark="政策标题")
	private String policyTitle;
	
	@Getter @Setter// @IFieldAnnotation(remark="政策内容")
	private String policyContent;
	
	@Getter @Setter// @IFieldAnnotation(remark="政策状态 S_PolicyState")
	private String policyState;
	
}
