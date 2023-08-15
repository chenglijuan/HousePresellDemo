package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldGetLimitAmountVerService {
    @Autowired
    private Empj_BuildingInfoDao buildingInfoDao;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDao bldLimitAmountVer_afDao;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;

    public Properties execute(Empj_BuildingInfoForm model) {
        Properties properties = new MyProperties();
        Long buildingId = model.getTableId();
        String nowLimitRatio = model.getNowLimitRatio();
        Empj_BuildingInfo buildingInfo = buildingInfoDao.findById(buildingId);
        if(buildingInfo==null){
            return MyBackInfo.fail(properties, "该楼幢不存在");
        }
        String theType = buildingInfo.getDeliveryType();
        if(theType==null){
            return MyBackInfo.fail(properties, "该楼幢没有楼幢类型");
        }
        if (nowLimitRatio == null)
        {
            return MyBackInfo.fail(properties, "目前受限额度比例不存在");
        }
        double nowLimitRatioDouble=0;
        if(nowLimitRatio.contains("%")){
            nowLimitRatioDouble = Double.parseDouble(nowLimitRatio.substring(0, nowLimitRatio.length() - 1));
        }else{
            nowLimitRatioDouble = Double.parseDouble(nowLimitRatio);
        }
        Tgpj_BldLimitAmountVer_AF nowLimitAmountVer = bldLimitAmountVer_afDao.getNowLimitAmountVer(theType);
        if(nowLimitAmountVer==null){
            return MyBackInfo.fail(properties, "目前没有对应的受限额度版本信息");
        }
        Long limitAmountVerId = nowLimitAmountVer.getTableId();
        Tgpj_BldLimitAmountVer_AFDtlForm tgpj_bldLimitAmountVer_afDtlForm = new Tgpj_BldLimitAmountVer_AFDtlForm();
        tgpj_bldLimitAmountVer_afDtlForm.setBldLimitAmountVerMngId(limitAmountVerId);
        tgpj_bldLimitAmountVer_afDtlForm.setOrderBy("limitedAmount asc");
        tgpj_bldLimitAmountVer_afDtlForm.setTheState(S_TheState.Normal);
        List<Tgpj_BldLimitAmountVer_AFDtl> list = tgpj_BldLimitAmountVer_AFDtlDao.findByPage(tgpj_BldLimitAmountVer_AFDtlDao.getQuery(tgpj_BldLimitAmountVer_AFDtlDao.getBasicHQL(), tgpj_bldLimitAmountVer_afDtlForm));


//		Long empj_BldLimitAmount_AFId = model.getTableId();
//		Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF)empj_BldLimitAmount_AFDao.findById(empj_BldLimitAmount_AFId);
//		if(empj_BldLimitAmount_AF == null || S_TheState.Deleted.equals(empj_BldLimitAmount_AF.getTheState()))
//		{
//			return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
//		}
        ArrayList<HashMap<String,String>> tgpj_bldLimitAmountVer_afDtls = new ArrayList<>();
        for (Tgpj_BldLimitAmountVer_AFDtl orginDtl : list) {
            Double limitedAmount = orginDtl.getLimitedAmount();
            if(limitedAmount>=nowLimitRatioDouble){
                continue;
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("tableId", orginDtl.getTableId()+"");
            hashMap.put("limitedAmount", orginDtl.getLimitedAmount() + "");
            hashMap.put("stageName", orginDtl.getStageName());
            hashMap.put("theName", orginDtl.getStageName());
//            Tgpj_BldLimitAmountVer_AFDtl rebuildDtl = new Tgpj_BldLimitAmountVer_AFDtl();
//            rebuildDtl.setTableId(orginDtl.getTableId());
//            rebuildDtl.setLimitedAmount(orginDtl.getLimitedAmount());
//            rebuildDtl.setStageName(orginDtl.getStageName());
            tgpj_bldLimitAmountVer_afDtls.add(hashMap);
        }

//
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("versionList", tgpj_bldLimitAmountVer_afDtls);

        return properties;
    }
}
