package zhishusz.housepresell.util.project;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_QualificationGrade;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.objectdiffer.model.Emmp_CompanyInfoTemplate;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dechert on 2018-11-22.
 * Company: zhishusz
 */
@Service
public class CompanyLogUitl {
    //    @Autowired
//    private Emmp_OrgMemberDao emmp_OrgMemberDao;
    public Emmp_CompanyInfoTemplate getCopyTemplate(Emmp_CompanyInfo emmp_CompanyInfo, List<Emmp_OrgMember> orgMemberListOrg) {
        return getCopyTemplate(emmp_CompanyInfo, orgMemberListOrg, S_CompanyType.Development);
    }

    public Emmp_CompanyInfoTemplate getCopyTemplate(Emmp_CompanyInfo emmp_CompanyInfo, List<Emmp_OrgMember> orgMemberListOrg, String companyType) {
        MyDatetime myDatetime = MyDatetime.getInstance();
        Emmp_CompanyInfoTemplate companyInfoTemplate = new Emmp_CompanyInfoTemplate();
        companyInfoTemplate.setTheType(companyType);
        companyInfoTemplate.setCompany(emmp_CompanyInfo);
        if(companyType.equals(S_CompanyType.Development)){
            Sm_CityRegionInfo cityRegion = emmp_CompanyInfo.getCityRegion();
            if (cityRegion != null) {
                companyInfoTemplate.setCityRegionName(cityRegion.getTheName());
            }
            Sm_StreetInfo street = emmp_CompanyInfo.getStreet();
            if (street != null) {
                companyInfoTemplate.setStreetName(street.getTheName());
            }
        }
        companyInfoTemplate.setQualificationGradeString(S_QualificationGrade.QUALIFICATION_GRADE_MAP.get(emmp_CompanyInfo.getQualificationGrade()));
        companyInfoTemplate.setSetupTimeString(myDatetime.dateToSimpleString(emmp_CompanyInfo.getRegisteredDate()));
//        Emmp_OrgMemberForm emmp_orgMemberForm = new Emmp_OrgMemberForm();
//        emmp_orgMemberForm.setBankId(emmp_CompanyInfo.getTableId());
//        emmp_orgMemberForm.setTheState(S_TheState.Normal);
//        List<Emmp_OrgMember> orgMemberListOrg = emmp_OrgMemberDao.findByPage(emmp_OrgMemberDao.getQuery(emmp_OrgMemberDao.getBasicHQL(), emmp_orgMemberForm));
        companyInfoTemplate.setOrgMemberList(orgMemberListOrg);
        return ObjectCopier.copy(companyInfoTemplate);
    }
}
