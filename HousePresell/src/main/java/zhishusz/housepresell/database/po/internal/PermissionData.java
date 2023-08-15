package zhishusz.housepresell.database.po.internal;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：权限-数据
 * 业务逻辑：分配权限后，将用户权限保存为一个json文件；
 *        用户登录后，获取该json文件，在过滤器中只过滤出相应权限范围内的数据
 * */
public class PermissionData implements Serializable,Comparable<PermissionData>
{
	private static final long serialVersionUID = 7416395267827743038L;
	
	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;

	@Getter @Setter @IFieldAnnotation(remark="排序")
	private Integer orderNumber;

	@Getter @Setter @IFieldAnnotation(remark="级别：1城市区域级、2街道级、3集团级、4机构级、5项目级")
	private Integer theLevel;

	@Getter @Setter @IFieldAnnotation(remark="所属城市区域")
	private Long cityRegionId;

	@Getter @Setter @IFieldAnnotation(remark="所属街道")
	private Long streetId;

	@Getter @Setter @IFieldAnnotation(remark="所属集团")
	private Long companyGroupId;

	@Getter @Setter @IFieldAnnotation(remark="所属机构")
	private Long companyInfoId;

	@Getter @Setter @IFieldAnnotation(remark="所属项目")
	private Long projectInfoId;
	
	@Override
	public int compareTo(PermissionData permissionData)
	{
		return this.orderNumber - permissionData.orderNumber;
	}
}
