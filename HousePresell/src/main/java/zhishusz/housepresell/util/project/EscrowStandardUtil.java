package zhishusz.housepresell.util.project;

import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.util.MyDouble;

import org.springframework.stereotype.Service;

/**
 * Created by Dechert on 2018-11-07.
 * Company: zhishusz
 */
@Service
public class EscrowStandardUtil {


    /**
     * 通过楼幢获取托管标准名称的方法
     * @param empj_BuildingInfo
     * @return
     */
    public String getEscrowStandardTypeName(Empj_BuildingInfo empj_BuildingInfo){
        MyDouble myDouble=MyDouble.getInstance();
        String trusteeshipContent = "";
        try {
            Tgpj_EscrowStandardVerMng escrowStandardVerMng = empj_BuildingInfo.getEscrowStandardVerMng();
            if (escrowStandardVerMng != null) {
                String theType = escrowStandardVerMng.getTheType();
                if (theType.equals(S_EscrowStandardType.StandardAmount)) {
                    Double amount = escrowStandardVerMng.getAmount();
                    trusteeshipContent = amount + "元";

                } else if (theType.equals(S_EscrowStandardType.StandardPercentage)) {
                    Double percentage = escrowStandardVerMng.getPercentage();
                    trusteeshipContent = "物价备案均价*" + myDouble.noDecimal(percentage) + "%";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trusteeshipContent;
    }

    public MsgInfo isBuildingInEscrow(Empj_BuildingInfo building){
        //托管终止判断 开始
        Empj_BuildingExtendInfo extendInfo = building.getExtendInfo();
        MsgInfo msgInfo = new MsgInfo();
        if (extendInfo != null)
        {
            String escrowState = extendInfo.getEscrowState();
            if(escrowState!=null){
                if(escrowState.equals(S_EscrowState.ApprovalEscrowState)){
//                    return MyBackInfo.fail(properties, "该楼幢正在申请托管终止，无法变更受限额度");
                    msgInfo.setSuccess(false);
                    msgInfo.setInfo("该楼幢正在申请托管终止，无法变更受限额度");
                    return msgInfo;
                }else if(escrowState.equals(S_EscrowState.EndEscrowState)){
//                    return MyBackInfo.fail(properties, "该楼幢已经托管终止，无法变更受限额度");
                    msgInfo.setSuccess(false);
                    msgInfo.setInfo("该楼幢已经托管终止，无法变更受限额度");
                    return msgInfo;
                }
            }
        }
        //托管终止判断 结束
        msgInfo.setSuccess(true);
        msgInfo.setInfo("success");
        return msgInfo;
    }

}
