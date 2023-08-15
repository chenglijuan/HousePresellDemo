package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.CapitalCollectionModelUtil;
import zhishusz.housepresell.util.IsUsingUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.convert.OrgMemberConverter;
import zhishusz.housepresell.util.objectdiffer.model.Emmp_BankInfoTemplate;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.hutool.core.lang.Validator;

/*
 * Service更新操作：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_BankInfoUpdateService {
    @Autowired
    private Emmp_BankInfoDao emmp_BankInfoDao;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;
    @Autowired
    private Sm_UserDao sm_UserDao;
    //机构成员
    @Autowired
    private Emmp_OrgMemberDao orgMemberDao;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

    //附件相关
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Emmp_OrgMemberAddService emmp_OrgMemberAddService;

    public Properties execute(Emmp_BankInfoForm model) {
        Properties properties = new MyProperties();
        //初始化数据
        String busiState = model.getBusiState();
        String eCode = model.geteCode();
        String theName = model.getTheName();
        String shortName = model.getShortName();
        String leader = model.getLeader();
        String address = model.getAddress();
        String capitalCollectionModel = model.getCapitalCollectionModel();
        String theType = model.getTheType();
        String postalAddress = model.getPostalAddress();
        String postalPort = model.getPostalPort();
        String contactPerson = model.getContactPerson();
        String contactPhone = model.getContactPhone();
        String ftpDirAddress = model.getFtpDirAddress();
        String ftpAddress = model.getFtpAddress();
        String ftpPort = model.getFtpPort();
        String ftpUserName = model.getFtpUserName();
        String ftpPwd = model.getFtpPwd();
        String financialInstitution = model.getFinancialInstitution();
        String theTypeOfPOS = model.getTheTypeOfPOS();
        String eCodeOfSubject = model.geteCodeOfSubject();
        String eCodeOfProvidentFundCenter = model.geteCodeOfProvidentFundCenter();
        String remark = model.getRemark();
        String bankNo = model.getBankNo();
        String bankCode = model.getBankCode();
        Long tableId = model.getTableId();
        Emmp_OrgMemberForm[] orgMemberList = model.getOrgMemberList();
        Emmp_BankInfo emmp_BankInfo = emmp_BankInfoDao.findById(tableId);
        //非空判断，输入判断
        if (theName == null || theName.length() == 0) {
            return MyBackInfo.fail(properties, "银行名称不能为空");
        }
        if (shortName == null || shortName.length() == 0) {
            return MyBackInfo.fail(properties, "金融机构简称不能为空");
        }
        if (capitalCollectionModel == null || capitalCollectionModel.length() == 0) {
            return MyBackInfo.fail(properties, "资金归集模式不能为空");
        }
        if (contactPerson == null || contactPerson.length() == 0) {
            return MyBackInfo.fail(properties, "联系人不能为空");
        }
        if (contactPhone == null || contactPhone.length() == 0) {
            return MyBackInfo.fail(properties, "联系电话不能为空");
        }
        MyString myString = MyString.getInstance();
        if (!Validator.isMobile(contactPhone) && !myString.checkFixedNumber(contactPhone)) {
            return MyBackInfo.fail(properties, S_NormalFlag.info_PhoneNumberFail);
        }
        if (emmp_BankInfo == null) {
            return MyBackInfo.fail(properties, "该金融机构不存在");
        }
        MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
        if (!msgInfo.isSuccess()) {
            return MyBackInfo.fail(properties, msgInfo.getInfo());
        }
        //逻辑开始
        Emmp_BankInfoTemplate emmp_bankInfoTemplate = new Emmp_BankInfoTemplate();

        Emmp_BankInfo emmp_BankInfoOld = ObjectCopier.copy(emmp_BankInfo);
        Emmp_OrgMemberForm emmp_orgMemberForm = new Emmp_OrgMemberForm();
        emmp_orgMemberForm.setBankId(tableId);
        emmp_orgMemberForm.setTheState(S_TheState.Normal);

        List<Emmp_OrgMember> orgMemberListOrg = orgMemberDao.findByPage(orgMemberDao.getQuery(orgMemberDao.getBasicHQL(), emmp_orgMemberForm));
//        List<Emmp_OrgMember> orgMemberListOrgCopy = ObjectCopier.copy(orgMemberListOrg);
        emmp_bankInfoTemplate.setCapitalCollectionModelName(CapitalCollectionModelUtil.number2Name(emmp_BankInfo.getCapitalCollectionModel()));
        emmp_bankInfoTemplate.setIsUsingName(IsUsingUtil.number2Name(emmp_BankInfo.getIsUsing()));
        emmp_bankInfoTemplate.setBank(emmp_BankInfoOld);
        emmp_bankInfoTemplate.setOrgMemberList(orgMemberListOrg);

        Emmp_BankInfoTemplate emmp_bankInfoTemplateOld = ObjectCopier.copy(emmp_bankInfoTemplate);

        //后续set
        emmp_BankInfo.setBusiState(busiState);
        emmp_BankInfo.seteCode(eCode);
        emmp_BankInfo.setUserUpdate(model.getUser());
        emmp_BankInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
        emmp_BankInfo.setTheName(theName);
        emmp_BankInfo.setShortName(shortName);
        emmp_BankInfo.setLeader(leader);
        emmp_BankInfo.setAddress(address);
        emmp_BankInfo.setCapitalCollectionModel(capitalCollectionModel);
        emmp_BankInfo.setTheType(theType);
        emmp_BankInfo.setPostalAddress(postalAddress);
        emmp_BankInfo.setPostalPort(postalPort);
        emmp_BankInfo.setContactPerson(contactPerson);
        emmp_BankInfo.setContactPhone(contactPhone);
        emmp_BankInfo.setFtpDirAddress(ftpDirAddress);
        emmp_BankInfo.setFtpAddress(ftpAddress);
        emmp_BankInfo.setFtpPort(ftpPort);
        emmp_BankInfo.setFtpUserName(ftpUserName);
        emmp_BankInfo.setFtpPwd(ftpPwd);
        emmp_BankInfo.setFinancialInstitution(financialInstitution);
        emmp_BankInfo.setTheTypeOfPOS(theTypeOfPOS);
        emmp_BankInfo.seteCodeOfSubject(eCodeOfSubject);
        emmp_BankInfo.seteCodeOfProvidentFundCenter(eCodeOfProvidentFundCenter);
        emmp_BankInfo.setRemark(remark);
        emmp_BankInfo.setBankNo(bankNo);
        emmp_BankInfo.setBankCode(bankCode);
        Emmp_BankInfo emmp_BankInfoNew = ObjectCopier.copy(emmp_BankInfo);
        emmp_BankInfoDao.save(emmp_BankInfo);
        sm_AttachmentBatchAddService.execute(model, model.getTableId());

        emmp_bankInfoTemplate.setCapitalCollectionModelName(CapitalCollectionModelUtil.number2Name(capitalCollectionModel));
        emmp_bankInfoTemplate.setIsUsingName(IsUsingUtil.number2Name(emmp_BankInfo.getIsUsing()));
        ArrayList<Emmp_OrgMember> emmp_orgMemberList = OrgMemberConverter.orgMemberFormList2OrgMemberList(model.getOrgMemberList());
//        if (orgMemberList != null) {
//            for (Emmp_OrgMemberForm emmpOrgMemberForm : orgMemberList) {
//                Emmp_OrgMember orgMember = OrgMemberConverter.orgMemberForm2OrgMember(emmpOrgMemberForm);
//                emmp_orgMemberList.add(orgMember);
//            }
//        }

        emmp_bankInfoTemplate.setOrgMemberList(emmp_orgMemberList);
        for (Emmp_OrgMember orgMember : orgMemberListOrg) {
            orgMember.setTheState(S_TheState.Deleted);
            orgMemberDao.save(orgMember);
        }
        if (orgMemberList != null) {
            for (Emmp_OrgMemberForm emmpOrgMemberForm : orgMemberList) {
                emmpOrgMemberForm.setBankId(model.getTableId());
                emmp_OrgMemberAddService.execute(emmpOrgMemberForm);
            }
        }

        emmp_bankInfoTemplate.setBank(emmp_BankInfoNew);
        Emmp_BankInfoTemplate emmp_bankInfoTemplateNew = ObjectCopier.copy(emmp_bankInfoTemplate);
        logAddService.addLog(model, tableId, emmp_bankInfoTemplateOld, emmp_bankInfoTemplateNew);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("tableId", emmp_BankInfo.getTableId());

        return properties;
    }
}
