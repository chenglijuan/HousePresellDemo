package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dechert on 2018-11-12.
 * Company: zhishusz
 */
@Service
public class BuildingLimitAmountVersionUtil {
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao bldLimitAmountVerAfDtlDao;
    @Autowired
    private Empj_BuildingInfoDao buildingInfoDao;

    public MsgInfo getBuildingLimitAmountVersionList(Empj_BuildingInfo buildingInfo) {
//        MsgInfo msgInfo = new MsgInfo();
//        msgInfo.setSuccess(false);
//        ArrayList<Tgpj_BldLimitAmountVer_AFDtl> bldVersionList = new ArrayList<>();
//        if (buildingInfo == null) {
//            msgInfo.setInfo("该楼幢不存在");
//            return msgInfo;
//        }
//        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
//        if (buildingAccount == null) {
//            msgInfo.setInfo("该楼幢账户不存在");
//            return msgInfo;
//        }
//        Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
//        if (bldLimitAmountVerDtl == null) {
//            msgInfo.setInfo("该楼幢不存在受限额度版本节点");
//            return msgInfo;
//        }
//        Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
//        if (bldLimitAmountVerMng == null) {
//            msgInfo.setInfo("该楼幢不存在受限额度版本节点");
//            return msgInfo;
//        }

//        if (currentLimitedRatio == null) {
//            msgInfo.setInfo("该楼幢不存在当前受限额度比例");
//            return msgInfo;
//        }
//        Tgpj_BldLimitAmountVer_AFDtlForm bldLimitAmountVerAfDtlForm = new Tgpj_BldLimitAmountVer_AFDtlForm();
//        bldLimitAmountVerAfDtlForm.setTheState(S_TheState.Normal);
//        bldLimitAmountVerAfDtlForm.setBldLimitAmountVerMngId(bldLimitAmountVerMng.getTableId());
//        bldLimitAmountVerAfDtlForm.setOrderBy("limitedAmount asc");
        MsgInfo msgInfo = getAllBuildingLimitVersionList(buildingInfo);
        if(!msgInfo.isSuccess()){
            return msgInfo;
        }
        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        Double currentLimitedRatio = buildingAccount.getCurrentLimitedRatio();
        List<Tgpj_BldLimitAmountVer_AFDtl> allList= (List<Tgpj_BldLimitAmountVer_AFDtl>) msgInfo.getExtra();
        ArrayList<HashMap<String,String>> afDtlList = new ArrayList<>();
//        List<Tgpj_BldLimitAmountVer_AFDtl> byPage = bldLimitAmountVerAfDtlDao.findByPage(bldLimitAmountVerAfDtlDao.getQuery(bldLimitAmountVerAfDtlDao.getBasicHQL(), bldLimitAmountVerAfDtlForm));
        for (Tgpj_BldLimitAmountVer_AFDtl orginDtl : allList) {
            if(orginDtl.getLimitedAmount()<currentLimitedRatio){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("tableId", orginDtl.getTableId()+"");
                hashMap.put("limitedAmount", orginDtl.getLimitedAmount() + "");
                hashMap.put("stageName", orginDtl.getStageName());
                hashMap.put("theName", orginDtl.getStageName());
                afDtlList.add(hashMap);
            }
        }
        msgInfo.setSuccess(true);
        msgInfo.setInfo(S_NormalFlag.success);
        msgInfo.setExtra(afDtlList);
        return msgInfo;
    }

    public MsgInfo getZeroLimitAmountNode(Long buildingInfoId){
        Empj_BuildingInfo buildingInfo = buildingInfoDao.findById(buildingInfoId);
        MsgInfo msgInfo = getAllBuildingLimitVersionList(buildingInfo);
        if(!msgInfo.isSuccess()){
            return msgInfo;
        }
        List<Tgpj_BldLimitAmountVer_AFDtl> allList= (List<Tgpj_BldLimitAmountVer_AFDtl>) msgInfo.getExtra();
        Tgpj_BldLimitAmountVer_AFDtl afDtl = allList.get(0);
        msgInfo.setSuccess(true);
        msgInfo.setInfo(S_NormalFlag.success);
        msgInfo.setExtra(afDtl);
        return msgInfo;
    }

    public MsgInfo getAllBuildingLimitVersionList(Empj_BuildingInfo buildingInfo){
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setSuccess(false);
        ArrayList<Tgpj_BldLimitAmountVer_AFDtl> bldVersionList = new ArrayList<>();
        if (buildingInfo == null) {
            msgInfo.setInfo("该楼幢不存在");
            return msgInfo;
        }
        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if (buildingAccount == null) {
            msgInfo.setInfo("该楼幢账户不存在");
            return msgInfo;
        }
        Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = buildingAccount.getBldLimitAmountVerDtl();
        if (bldLimitAmountVerDtl == null) {
            msgInfo.setInfo("未获取到该楼幢的受限节点详细信息");
            return msgInfo;
        }
        Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng = bldLimitAmountVerDtl.getBldLimitAmountVerMng();
        if (bldLimitAmountVerMng == null) {
            msgInfo.setInfo("该楼幢不存在受限额度版本节点");
            return msgInfo;
        }
        Double currentLimitedRatio = buildingAccount.getCurrentLimitedRatio();
        if (currentLimitedRatio == null) {
            msgInfo.setInfo("该楼幢不存在当前受限额度比例");
            return msgInfo;
        }
        Tgpj_BldLimitAmountVer_AFDtlForm bldLimitAmountVerAfDtlForm = new Tgpj_BldLimitAmountVer_AFDtlForm();
        bldLimitAmountVerAfDtlForm.setTheState(S_TheState.Normal);
        bldLimitAmountVerAfDtlForm.setBldLimitAmountVerMngId(bldLimitAmountVerMng.getTableId());
        bldLimitAmountVerAfDtlForm.setOrderBy("limitedAmount asc");
//        ArrayList<HashMap<String,String>> afDtlList = new ArrayList<>();
//        List<Tgpj_BldLimitAmountVer_AFDtl> byPage = bldLimitAmountVerAfDtlDao.findByPage(bldLimitAmountVerAfDtlDao.getQuery(bldLimitAmountVerAfDtlDao.getBasicHQL(), bldLimitAmountVerAfDtlForm));
        List<Tgpj_BldLimitAmountVer_AFDtl> byPage = bldLimitAmountVerAfDtlDao.findByPage(bldLimitAmountVerAfDtlDao.createGetDtlList(bldLimitAmountVerAfDtlForm));
        msgInfo.setSuccess(true);
        msgInfo.setInfo(S_NormalFlag.success);
        msgInfo.setExtra(byPage);
        return msgInfo;
    }

    public MsgInfo getBuildingLimitAmountVersionList(Long buildingInfoId) {
        Empj_BuildingInfo byId = buildingInfoDao.findById(buildingInfoId);
        return getBuildingLimitAmountVersionList(byId);
    }
}
