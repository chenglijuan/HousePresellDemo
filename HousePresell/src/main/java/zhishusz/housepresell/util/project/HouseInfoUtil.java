package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dechert on 2018-11-13.
 * Company: zhishusz
 */
@Service
public class HouseInfoUtil {
    @Autowired
    private Empj_HouseInfoDao houseInfoDao;
    @Autowired
    private Empj_BuildingInfoDao buildingInfoDao;

    public MsgInfo calculatePresellSystemPrice(Long buildingInfoId) {
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setSuccess(false);
        if (buildingInfoId == null) {
            msgInfo.setInfo("楼幢Id为空");
            return msgInfo;
        }
        Empj_HouseInfoForm houseInfoForm = new Empj_HouseInfoForm();
        houseInfoForm.setTheState(S_TheState.Normal);
        houseInfoForm.setBuildingId(buildingInfoId);
        double totalPrice = 0.0;
        List<Empj_HouseInfo> houseList = houseInfoDao.findByPage(houseInfoDao.getQuery(houseInfoDao.getBasicHQL(), houseInfoForm));
        if (houseList.size() == 0) {
            msgInfo.setInfo("户室数量为0");
            return msgInfo;
        }
        for (Empj_HouseInfo houseInfo : houseList) {
            Double recordPrice = houseInfo.getRecordPrice();
            if (recordPrice == null) {
                msgInfo.setInfo("户室备案价格为空");
                return msgInfo;
            }
            totalPrice += recordPrice;
        }
        Empj_BuildingInfo buildingInfo = buildingInfoDao.findById(buildingInfoId);
        if (buildingInfo.getEscrowArea() == null) {
            msgInfo.setInfo("楼幢托管面积为空");
            return msgInfo;
        }
        double result = totalPrice / buildingInfo.getEscrowArea();
        String resultString = MyDouble.getInstance().twoDecimal(result);
        msgInfo.setSuccess(true);
        msgInfo.setInfo(S_NormalFlag.success);
        msgInfo.setExtra(resultString);

        return msgInfo;

    }

}
