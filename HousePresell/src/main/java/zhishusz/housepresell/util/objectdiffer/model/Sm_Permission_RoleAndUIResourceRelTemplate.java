package zhishusz.housepresell.util.objectdiffer.model;

import java.util.Arrays;
import java.util.List;

import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-10-16.
 * Company: zhishusz
 */

public class Sm_Permission_RoleAndUIResourceRelTemplate extends BaseTemplate
{
    @Getter @Setter
    private Sm_Permission_Role sm_Permission_Role;
    @Getter @Setter @IFieldAnnotation(remark = "角色授权数据")
    private String menuAndBtnInfoListString;//数据格式：一级菜单->二级菜单->...->实体菜单(按钮1，按钮2...)

    @SuppressWarnings("rawtypes")
	public List getNeedFieldList(){
        return Arrays.asList("menuAndBtnInfoListString", "sm_Permission_Role/theName","sm_Permission_Role/eCode");
    }
}
