package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.po.state.S_TheState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：托管终止楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_BuildingInfoBldEscrowListService
{
    @Autowired
    private Empj_BuildingInfoDao empj_buildingInfoDao;
    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_buildingAvgPriceDao;
    @Autowired
    private Empj_BldLimitAmount_AFDao empj_bldLimitAmount_afDao;
    @Autowired
    private Tgpf_RefundInfoDao tgpf_refundInfoDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Empj_BldEscrowCompletedForm model)
    {
        Properties properties = new MyProperties();
        model.setTheState(S_TheState.Normal);
        model.setKeyword(null);

        List<Empj_BuildingInfo> empj_BuildingInfoAvgPriceList =
                tgpj_buildingAvgPriceDao.findByPage(tgpj_buildingAvgPriceDao.createCriteriaForBuildingAvgPriceList(model),
                        null, null);
        List<Long> buildingAvgPriceIdArr = new ArrayList<>();
        if(empj_BuildingInfoAvgPriceList != null)
        {
            for (Empj_BuildingInfo itemBuildingInfo : empj_BuildingInfoAvgPriceList)
            {
                if (itemBuildingInfo != null)
                {
                    buildingAvgPriceIdArr.add(itemBuildingInfo.getTableId());
                }
            }
        }
        List<Empj_BuildingInfo> empj_BuildingInfoBldLimitAmountList =
                empj_bldLimitAmount_afDao.findByPage(empj_bldLimitAmount_afDao.createCriteriaForBldLimitAmountList(model),
                        null, null);
        List<Long> buildingBldLimitAmountIdArr = new ArrayList<>();
        if(empj_BuildingInfoBldLimitAmountList != null)
        {
            for (Empj_BuildingInfo itemBuildingInfo : empj_BuildingInfoBldLimitAmountList)
            {
                if (itemBuildingInfo != null)
                {
                    buildingBldLimitAmountIdArr.add(itemBuildingInfo.getTableId());
                }
            }
        }
        List<Empj_BuildingInfo> empj_BuildingInfoRefundInfoList =
                tgpf_refundInfoDao.findByPage(tgpf_refundInfoDao.createCriteriaForRefundInfoList(model),
                        null, null);
        List<Long> buildingRefundInfoIdArr = new ArrayList<>();
        if(empj_BuildingInfoRefundInfoList != null)
        {
            for (Empj_BuildingInfo itemBuildingInfo : empj_BuildingInfoRefundInfoList)
            {
                if (itemBuildingInfo != null)
                {
                    buildingRefundInfoIdArr.add(itemBuildingInfo.getTableId());
                }
            }
        }

//        List<Long> intersectionBuildingIdArr = new ArrayList<>();
        buildingAvgPriceIdArr.addAll(buildingBldLimitAmountIdArr);
        buildingAvgPriceIdArr.addAll(buildingRefundInfoIdArr);
        Long[] intersectionBuildingIdArr = buildingAvgPriceIdArr.toArray(new Long[buildingAvgPriceIdArr.size()]);

        List<Empj_BuildingInfo> empj_BuildingInfoList = null;
        if (intersectionBuildingIdArr !=null && intersectionBuildingIdArr.length > 0)
        {
            model.setIntersectionBuildingIdArr(intersectionBuildingIdArr);
        }
        empj_BuildingInfoList = empj_buildingInfoDao.findByPage(empj_buildingInfoDao.createCriteriaForBuildingInfoList(model),null, null);
        if (empj_BuildingInfoList == null || empj_BuildingInfoList.size() < 1)
        {
            empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
        }

        properties.put("empj_BuildingInfoList", empj_BuildingInfoList);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
