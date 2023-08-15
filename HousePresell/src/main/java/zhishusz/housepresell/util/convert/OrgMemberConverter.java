package zhishusz.housepresell.util.convert;

import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.po.Emmp_OrgMember;

import java.util.ArrayList;

/**
 * Created by Dechert on 2018-11-20.
 * Company: zhishusz
 */

public class OrgMemberConverter {

    public static Emmp_OrgMember orgMemberForm2OrgMember(Emmp_OrgMemberForm form) {
        Emmp_OrgMember orgMember = new Emmp_OrgMember();
        orgMember.setTableId(form.getTableId());
        orgMember.setTheNameOfDepartment(form.getTheNameOfDepartment());
        orgMember.setTheName(form.getTheName());
        orgMember.setIdNumber(form.getIdNumber());
        orgMember.setPhoneNumber(form.getPhoneNumber());
        orgMember.seteCodeOfDepartment(form.geteCodeOfDepartment());
        orgMember.setPositionName(form.getPositionName());
        orgMember.setIdType(form.getIdType());
        orgMember.setEmail(form.getEmail());
        orgMember.setWeixin(form.getWeixin());
        orgMember.setQq(form.getQq());
        return orgMember;
    }

    public static ArrayList<Emmp_OrgMember> orgMemberFormList2OrgMemberList(Emmp_OrgMemberForm[] orgMemberList) {
        if (orgMemberList == null) {
            return new ArrayList<>();
        }
        ArrayList<Emmp_OrgMember> emmp_orgMemberList = new ArrayList<>();
        for (Emmp_OrgMemberForm emmpOrgMemberForm : orgMemberList) {
            Emmp_OrgMember orgMember = OrgMemberConverter.orgMemberForm2OrgMember(emmpOrgMemberForm);
            emmp_orgMemberList.add(orgMember);
        }
        return emmp_orgMemberList;
    }

}
