package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-10-23.
 * Company: zhishusz
 */

public class Emmp_BankInfoTemplate extends BaseTemplate {
    @Getter @Setter
    private Emmp_BankInfo bank;
    @Getter @Setter @IFieldAnnotation(remark = "机构列表")
    private List<Emmp_OrgMember> orgMemberList;
    @Getter @Setter @IFieldAnnotation(remark = "资金归集模式")
    private String capitalCollectionModelName;
    @Getter @Setter @IFieldAnnotation(remark = "是否启用")
    private String isUsingName;

    public List getNeedFieldList(){
        ArrayList<String> needFieldList = new ArrayList<>();
        needFieldList.add("bank/theName");
        needFieldList.add("bank/shortName");
        needFieldList.add("capitalCollectionModelName");
        needFieldList.add("isUsingName");
        needFieldList.add("bank/contactPerson");
        needFieldList.add("bank/contactPhone");
        needFieldList.add("bank/remark");
        addOrgMemberList(needFieldList);
        return needFieldList;
    }

}
