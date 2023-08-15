package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_BldLimitAmountVer_AFRebuild extends RebuilderBase<Tgpj_BldLimitAmountVer_AF> {
    @Autowired
    private Sm_BaseParameterGetService baseParameterGetService;

    @Override
    public Properties getSimpleInfo(Tgpj_BldLimitAmountVer_AF object) {
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("tableId", object.getTableId());

        return properties;
    }

    @Override
    public Properties getDetail(Tgpj_BldLimitAmountVer_AF object) {
        MyDatetime myDatetime = MyDatetime.getInstance();
        if (object == null)
            return null;
        Properties properties = new MyProperties();

        properties.put("theState", object.getTheState());
        properties.put("busiState", object.getBusiState());
        properties.put("eCode", object.geteCode());
        Sm_User userStart = object.getUserStart();
        if (userStart != null) {
            properties.put("userStartName", userStart.getTheName());
            properties.put("userStartId", userStart.getTableId());
        }
        Sm_User userRecord = object.getUserRecord();
        if (userRecord != null) {
            properties.put("userRecordId", userRecord.getTableId());
            properties.put("userRecordName", userRecord.getTheName());
        }
        Long createTimeStamp = object.getCreateTimeStamp();
        if (createTimeStamp != null) {
            properties.put("createTime", myDatetime.dateToSimpleString(createTimeStamp));
        }
        properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
        properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
        Sm_User userUpdate = object.getUserUpdate();
        if (userUpdate != null) {
            properties.put("userUpdateId", userUpdate.getTableId());
            properties.put("userUpdateName", userUpdate.getTheName());
        }
        properties.put("recordTimeStampString", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
        properties.put("theName", object.getTheName());
        properties.put("theType", object.getTheType());
        Long beginExpirationDate = object.getBeginExpirationDate();
        if (beginExpirationDate != null) {
            String beginString = myDatetime.dateToSimpleString(beginExpirationDate);
            properties.put("timeDuring", beginString);
        }
        properties.put("beginExpirationDate", object.getBeginExpirationDate());
        properties.put("endExpirationDate", object.getEndExpirationDate());
        properties.put("isUsing", object.getIsUsing());
        properties.put("approvalState", object.getApprovalState());


        return properties;
    }

    @SuppressWarnings("rawtypes")
    public List getDetailForAdmin(
            List<Tgpj_BldLimitAmountVer_AF> tgpj_BldLimitAmountVer_AFList) {
        MyDatetime myDatetime = MyDatetime.getInstance();
        List<Properties> list = new ArrayList<Properties>();
        if (tgpj_BldLimitAmountVer_AFList != null) {
            for (Tgpj_BldLimitAmountVer_AF object : tgpj_BldLimitAmountVer_AFList) {
                Properties properties = new MyProperties();

                properties.put("tableId", object.getTableId());
                properties.put("theState", object.getTheState());
                properties.put("busiState", object.getBusiState());
                properties.put("eCode", object.geteCode());
                //				properties.put("userStart", object.getUserStart());
                //				properties.put("userStartId", object.getUserStart().getTableId());
                //				properties.put("createTimeStamp", object.getCreateTimeStamp());
                //				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
                //				properties.put("userRecord", object.getUserRecord());
                //				properties.put("userRecordId", object.getUserRecord().getTableId());
                //				properties.put("recordTimeStamp", object.getRecordTimeStamp());
                properties.put("theName", object.getTheName());
                //				properties.put("theVerion", object.getTheVerion());
                String theType = object.getTheType();

                if (theType != null) {
                    Sm_BaseParameter parameter = baseParameterGetService.getParameter("5", theType);

                    //					String theTypeString = "";
//					try
//					{
//						int parseInt = Integer.parseInt(theType);
//						if (parseInt == 0)
//						{
//							theTypeString = "毛坯房";
//						}
//						else
//						{
//							theTypeString = "成品房";
//						}
//					}
//					catch (Exception e)
//					{
//						e.printStackTrace();
//					}
                    properties.put("theType", theType);
                    properties.put("theTypeString", parameter.getTheName());
                }

                //				properties.put("limitedAmountInfoJSON", object.getLimitedAmountInfoJSON());
                properties.put("beginExpirationDate", object.getBeginExpirationDate());
                properties.put("beginExpirationDateString",
                        myDatetime.dateToSimpleString(object.getBeginExpirationDate()));

                properties.put("endExpirationDate", object.getEndExpirationDate());
                properties.put("endExpirationDateString", myDatetime.dateToSimpleString(object.getEndExpirationDate()));
                properties.put("isUsing", object.getIsUsing());
                properties.put("approvalState", object.getApprovalState());

                list.add(properties);
            }
        }
        return list;
    }
}
