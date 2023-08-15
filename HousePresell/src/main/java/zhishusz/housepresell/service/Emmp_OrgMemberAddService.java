package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.dao.Emmp_QualificationInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service添加操作：机构成员
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_OrgMemberAddService
{
    @Autowired
    private Emmp_OrgMemberDao emmp_OrgMemberDao;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
    @Autowired
    private Emmp_BankInfoDao emmp_BankInfoDao;
    @Autowired
    private Emmp_QualificationInfoDao emmp_QualificationInfoDao;
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;

    public Properties execute(Emmp_OrgMemberForm model)
    {
        Properties properties = new MyProperties();
        int type = 0;

        Long companyId = model.getCompanyId();
        String theNameOfDepartment = model.getTheNameOfDepartment();
        String theName = model.getTheName();
        String idType = model.getIdType();
        String idNumber = model.getIdNumber();
        Long parameterId = model.getParameterId();
        String positionName = model.getPositionName();
        String phoneNumber = model.getPhoneNumber();
        String email = model.getEmail();
        String weixin = model.getWeixin();
        String qq = model.getQq();
        Long bankId = model.getBankId();
        if (companyId != null)
        {
            type |= 1;
        }
        if (bankId != null)
        {
            type |= 2;
        }

//		if(companyId == null || companyId < 1)
//		{
//			return MyBackInfo.fail(properties, "'company'不能为空");
//		}
//        if (theNameOfDepartment == null || theNameOfDepartment.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'theNameOfDepartment'不能为空");
//        }
//        if (theName == null || theName.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'theName'不能为空");
//        }
//        if (idType == null || idType.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'idType'不能为空");
//        }


//        if (positionName == null || positionName.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'positionName'不能为空");
//        }

//        if (idNumber == null || idNumber.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'idNumber'不能为空");
//        }
//        if (phoneNumber == null || phoneNumber.length() == 0)
//        {
//            return MyBackInfo.fail(properties, "'phoneNumber'不能为空");
//        }
        Sm_BaseParameter parameter = (Sm_BaseParameter) sm_BaseParameterDao.findById(parameterId);
        Emmp_CompanyInfo company=null;
        Emmp_BankInfo bank=null;
        if(type==1)
        {
            company = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(companyId);
            if (company == null) {
                return MyBackInfo.fail(properties, "'company'不能为空");
            }
        }
        else if(type==2)
        {
            bank = emmp_BankInfoDao.findById(bankId);
            if (bank == null) {
                return MyBackInfo.fail(properties, "'bank'不能为空");
            }
        }

		
        Emmp_OrgMember emmp_OrgMember = new Emmp_OrgMember();
        emmp_OrgMember.setTheState(S_TheState.Normal);
//		emmp_OrgMember.setBusiState(busiState);
//		emmp_OrgMember.seteCode(eCode);
//		emmp_OrgMember.setUserStart(userStart);
		emmp_OrgMember.setCreateTimeStamp(System.currentTimeMillis());
        emmp_OrgMember.setLastUpdateTimeStamp(System.currentTimeMillis());
        emmp_OrgMember.setCompany(company);
        emmp_OrgMember.setBank(bank);
        emmp_OrgMember.setTheNameOfDepartment(theNameOfDepartment);
        emmp_OrgMember.setTheName(theName);
        emmp_OrgMember.setIdType(idType);
        emmp_OrgMember.setIdNumber(idNumber);
        emmp_OrgMember.setParameter(parameter);
        emmp_OrgMember.setPositionName(positionName);
        emmp_OrgMember.setPhoneNumber(phoneNumber);
        emmp_OrgMember.setEmail(email);
        emmp_OrgMember.setWeixin(weixin);
        emmp_OrgMember.setQq(qq);
        emmp_OrgMemberDao.save(emmp_OrgMember);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
