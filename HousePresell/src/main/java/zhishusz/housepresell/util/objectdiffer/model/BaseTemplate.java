package zhishusz.housepresell.util.objectdiffer.model;

import java.util.ArrayList;

/**
 * Created by Dechert on 2018-10-25.
 * Company: zhishusz
 */

class BaseTemplate {
    public void addOrgMemberList(ArrayList<String> needFieldList){
        needFieldList.add("orgMemberList");
        needFieldList.add("orgMemberList/theNameOfDepartment");
        needFieldList.add("orgMemberList/theName");
        needFieldList.add("orgMemberList/idNumber");
        needFieldList.add("orgMemberList/phoneNumber");
        needFieldList.add("orgMemberList/eCodeOfDepartment");
        needFieldList.add("orgMemberList/positionName");
        needFieldList.add("orgMemberList/idType");
        needFieldList.add("orgMemberList/email");
        needFieldList.add("orgMemberList/weixin");
        needFieldList.add("orgMemberList/qq");

    }


}
