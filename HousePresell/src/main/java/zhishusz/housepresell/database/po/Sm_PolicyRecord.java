package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：政策公告
 * */
@ITypeAnnotation(remark="政策公告")
public class Sm_PolicyRecord implements Serializable
{
	
	private static final long serialVersionUID = -7844008324977521882L;

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
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	//---------公共字段-End---------//
	@Getter @Setter @IFieldAnnotation(remark="是否轮播 0：否 1：是")
	private String policyIsbrow;
	
	@Getter @Setter @IFieldAnnotation(remark="是否置顶 0：否 1：是")
	private String policyIstop;
	
	@Getter @Setter @IFieldAnnotation(remark="发布时间")
	private String policyDate;
	
	@Getter @Setter @IFieldAnnotation(remark="政策类型 从基础参数表中读取")
	private String policyType;
	
	@Getter @Setter @IFieldAnnotation(remark="政策类型标志 theValue")
	private String policyTypeCode;
	
	@Getter @Setter @IFieldAnnotation(remark="政策标题")
	private String policyTitle;
	
	@Getter @Setter @IFieldAnnotation(remark="政策内容")
	private String policyContent;
	
	@Getter @Setter @IFieldAnnotation(remark="政策状态 S_PolicyState")
	private String policyState;
	
	@Getter @Setter @IFieldAnnotation(remark="浏览次数")
	private Integer browseTimes;
	
	@Override
	public String toString()
	{
		return "policyContent="+policyContent+";policyTitle="+policyTitle;
	}
	
}
