package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-11-22.
 * Company: zhishusz
 */

public class Emmp_CompanyInfoTemplate extends BaseTemplate {
    @Getter @Setter
    private Emmp_CompanyInfo company;
    @Getter @Setter
    private String theType;
    @Getter @Setter @IFieldAnnotation(remark = "成立日期")
    private String setupTimeString;
    @Getter @Setter @IFieldAnnotation(remark = "所属区域")
    private String cityRegionName;
    @Getter @Setter @IFieldAnnotation(remark = "所属街道")
    private String streetName;
    @Getter @Setter @IFieldAnnotation(remark = "资质等级")
    private String qualificationGradeString;
    @Getter @Setter @IFieldAnnotation(remark = "机构列表")
    private List<Emmp_OrgMember> orgMemberList;




    public List getNeedFieldList(){
        if(theType.equals(S_CompanyType.Development)){
            return Arrays.asList("company/theName","qualificationGradeString","company/unifiedSocialCreditCode","company/address","setupTimeString","company/legalPerson",
                    "company/projectLeader","cityRegionName","streetName","company/companyGroup","orgMemberList");
        }else{
            return Arrays.asList("company/theName","company/address","setupTimeString","company/unifiedSocialCreditCode","company/legalPerson",
                    "company/contactPerson", "company/contactPhone","company/companyGroup","orgMemberList");
        }



    }



}
