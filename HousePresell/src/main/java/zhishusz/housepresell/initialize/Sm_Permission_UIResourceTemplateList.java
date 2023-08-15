package zhishusz.housepresell.initialize;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//初始化权限资源模板模型
@ToString
public class Sm_Permission_UIResourceTemplateList implements Serializable
{
	private static final long serialVersionUID = -7459581698298471764L;

	@Getter @Setter @IFieldAnnotation(remark="列表")
	private List<Sm_Permission_UIResourceTemplate> list;
}
