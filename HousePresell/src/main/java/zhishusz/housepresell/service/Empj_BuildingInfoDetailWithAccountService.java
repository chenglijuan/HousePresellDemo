package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.EscrowStandardUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfoDetailWithAccountService {
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;

    public Properties execute(Empj_BuildingInfoForm model) {
        Properties properties = new MyProperties();

        Long empj_BuildingInfoId = model.getTableId();
        Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(empj_BuildingInfoId);
        if (empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState())) {
            return MyBackInfo.fail(properties, "'Empj_BuildingInfo(Id:" + empj_BuildingInfoId + ")'不存在");
        }
        Empj_BuildingInfo buildingInfoRebuild = rebuild(empj_BuildingInfo);

//        ProjectObjectCopyier<Empj_BuildingInfo> projectObjectCopyier = new ProjectObjectCopyier<>();
//        projectObjectCopyier.setHibernateCopy(true);
//        projectObjectCopyier.setLog(false);
//
//        Empj_BuildingInfo buildingCopy = projectObjectCopyier.projectCopy(empj_BuildingInfo);
//        buildingInfoRebuild.setBuildingAccount(buildingCopy.getBuildingAccount());

//        Session currentSession = empj_BuildingInfoDao.getCurrentSession();
//        currentSession.evict(empj_BuildingInfo);
//        currentSession.evict(buildingAccount);
//        if (buildingAccount != null) {
//            buildingAccount.setUserStart(null);
//            buildingAccount.setUserRecord(null);
//            buildingAccount.setUserUpdate(null);
//            buildingAccount.setProject(null);
//            buildingAccount.setBuilding(null);
//            buildingAccount.setDevelopCompany(null);
//            buildingInfoRebuild.setBuildingAccount(buildingCopy.getBuildingAccount());
//            buildingInfoRebuild.setBuildingAccount(buildingAccount);
//        }
//        String trusteeshipContent = "";
//        try {
//            Tgpj_EscrowStandardVerMng escrowStandardVerMng = empj_BuildingInfo.getEscrowStandardVerMng();
//
//            if (escrowStandardVerMng != null) {
//                String theType = escrowStandardVerMng.getTheType();
//                if (theType.equals(S_EscrowStandardType.StandardAmount)) {
//                    Double amount = escrowStandardVerMng.getAmount();
//                    trusteeshipContent = amount + "元";
//
//                } else if (theType.equals(S_EscrowStandardType.StandardPercentage)) {
//                    Double percentage = escrowStandardVerMng.getPercentage();
//                    trusteeshipContent = "物价备案均价*" + percentage + "%";
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String escrowStandardTypeName = escrowStandardUtil.getEscrowStandardTypeName(empj_BuildingInfo);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("buildingInfo", buildingInfoRebuild);
        properties.put("trusteeshipContent", escrowStandardTypeName);

        return properties;
    }

    private Empj_BuildingInfo rebuild(Empj_BuildingInfo empj_BuildingInfo) {
        Empj_BuildingInfo buildingInfoRebuild = new Empj_BuildingInfo();
        buildingInfoRebuild.setTableId(empj_BuildingInfo.getTableId());
        buildingInfoRebuild.seteCode(empj_BuildingInfo.geteCode());
        buildingInfoRebuild.setTheType(empj_BuildingInfo.getTheType());
        buildingInfoRebuild.setDeliveryType(empj_BuildingInfo.getDeliveryType());
        Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
        Tgpj_BuildingAccount tgpj_buildingAccountRebuild = new Tgpj_BuildingAccount();
        if (buildingAccount != null) {
            tgpj_buildingAccountRebuild.setTableId(buildingAccount.getTableId());
            tgpj_buildingAccountRebuild.seteCode(buildingAccount.geteCode());
            tgpj_buildingAccountRebuild.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
            tgpj_buildingAccountRebuild.setEscrowArea(buildingAccount.getEscrowArea());
            tgpj_buildingAccountRebuild.setBuildingArea(buildingAccount.getBuildingArea());
            tgpj_buildingAccountRebuild.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
            tgpj_buildingAccountRebuild.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
            tgpj_buildingAccountRebuild.setCurrentLimitedRatio(buildingAccount.getCurrentLimitedRatio());
            tgpj_buildingAccountRebuild.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
            tgpj_buildingAccountRebuild.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
            tgpj_buildingAccountRebuild.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
            tgpj_buildingAccountRebuild.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
            tgpj_buildingAccountRebuild.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
            tgpj_buildingAccountRebuild.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());
//            tgpj_buildingAccountRebuild.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
        }
        buildingInfoRebuild.setBuildingAccount(tgpj_buildingAccountRebuild);
        return buildingInfoRebuild;
    }
}
