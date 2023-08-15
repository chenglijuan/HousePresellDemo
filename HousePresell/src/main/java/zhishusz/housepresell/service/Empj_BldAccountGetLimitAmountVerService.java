package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.BuildingLimitAmountVersionUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldAccountGetLimitAmountVerService {
    @Autowired
    private Empj_BuildingInfoDao buildingInfoDao;
//    @Autowired
//    private Tgpj_BldLimitAmountVer_AFDao bldLimitAmountVer_afDao;
//    @Autowired
//    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;
    @Autowired
    private BuildingLimitAmountVersionUtil buildingLimitAmountVersionUtil;

    public Properties execute(Empj_BuildingInfoForm model) {
        Properties properties = new MyProperties();
        Long buildingId = model.getTableId();
        /*正常版本
        MsgInfo msgInfo = buildingLimitAmountVersionUtil.getBuildingLimitAmountVersionList(buildingId);
        if(!msgInfo.isSuccess()){
            return MyBackInfo.fail(properties, msgInfo.getInfo());
        }
        ArrayList<HashMap<String,String>> versionList=null;
        Object extra = msgInfo.getExtra();
        if(extra instanceof List){
            versionList= (ArrayList<HashMap<String, String>>) extra;
        }else{
            return MyBackInfo.fail(properties, "类型转换出错！");
        }*/
        
        //调用存储过程版本
        Map<String, Object> listMap;
		try {
			listMap = buildingInfoDao.getBldLimitAmountVerAfDtl(buildingId, model.getNowLimitRatio());
			
			properties.put(S_NormalFlag.result, listMap.get("sign"));
			properties.put(S_NormalFlag.info, listMap.get("info"));
			properties.put("versionList", listMap.get("versionList"));
			
		} catch (SQLException e) {
			
			properties.put(S_NormalFlag.result, "fail");
			properties.put(S_NormalFlag.info, "操作失败");
			properties.put("versionList", null);
			
		}
        

        return properties;
    }

    /**
     * 获取楼幢使用的受限额度节点版本中最后一个节点（受限额度为0）
     * @param model 楼幢表单
     * @return Properties 受限额度节点
     */
    public Properties executeZeroLimitAmountNode(Empj_BuildingInfoForm model)
    {
        Properties properties = new MyProperties();
        Long buildingId = model.getTableId();

        MsgInfo msgInfo = buildingLimitAmountVersionUtil.getZeroLimitAmountNode(buildingId);
        Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl = (Tgpj_BldLimitAmountVer_AFDtl)msgInfo.getExtra();
        properties.put("limitAmountNode", bldLimitAmountVer_afDtl);
        if (msgInfo.isSuccess())
        {
            properties.put("limitAmountNode", bldLimitAmountVer_afDtl);
            properties.put(S_NormalFlag.result, S_NormalFlag.success);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        }
        else
        {
            properties.put(S_NormalFlag.result, S_NormalFlag.fail);
            properties.put(S_NormalFlag.info, msgInfo.getInfo());
        }

        return properties;
    }
}
