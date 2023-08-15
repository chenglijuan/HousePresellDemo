package zhishusz.housepresell.dingding.webservice.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.dingding.entity.PanYunDingDingSaveSignUrlsEntity;
import zhishusz.housepresell.dingding.webservice.PanYunDingdingSignWebService;

/**
 * @Description 攀云钉钉保存现场签到时间webService接口实现类
 * @Author jxx
 * @Date 2020/9/27 10:50
 * @Version
 **/
@WebService(endpointInterface = "zhishusz.housepresell.dingding.webservice.PanYunDingdingSignWebService")
@Transactional
public class PanYunDingdingSignWebServiceImpl implements PanYunDingdingSignWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanYunDingdingSignWebServiceImpl.class);

    @Autowired
    private Empj_BldLimitAmountDao empj_BldLimitAmountDao;

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

    @Override
    public String saveSign(String result) {
        System.out.println("PanYunDingDingWebService saveSignUrls: " + result);
        System.out.println("-----------------------PanYunDingDingWebService saveSignUrls Start-----------------------");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isBlank(result)) {
            params.put("code", 100);
            params.put("message", "result is Blank");
            System.out
                .println("-------result is Blank-PanYunDingDingWebService saveSignUrls End-----------------------");
            return JSONObject.toJSONString(params);
        }
        PanYunDingDingSaveSignUrlsEntity saveSignUrlsEntity =
            JSONObject.parseObject(result, PanYunDingDingSaveSignUrlsEntity.class);
        System.out.println("taskId" + saveSignUrlsEntity.getTaskId());
        if (null == saveSignUrlsEntity || StringUtils.isBlank(saveSignUrlsEntity.getTaskId())) {
            params.put("code", 101);
            params.put("message", "Params taskId is Blank");
            System.out
                .println("-------Params taskId is Blank-PanYunDingDingWebService saveSignUrls End----------------");
            return JSONObject.toJSONString(params);
        }

        Empj_BldLimitAmountForm model = new Empj_BldLimitAmountForm();
        model.setTheState(0);
        model.seteCode(saveSignUrlsEntity.getTaskId());
        Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao
            .findOneByQuery_T(empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(), model));
        if (null == bldLimitAmount) {
            params.put("code", 400);
            params.put("message", "TaskId is not exist");
            System.out
                .println("-------TaskId is not exist-PanYunDingDingWebService saveSignUrls End-----------------------");
            return JSONObject.toJSONString(params);
        }

        String supervisorCode = saveSignUrlsEntity.getSupervisorCode();
        if (StringUtils.isBlank(supervisorCode)) {
            params.put("code", 104);
            params.put("message", "Params supervisorCode is Blank");
            System.out
                .println("-------Params supervisorCode is Blank-PanYunDingDingWebService saveSignUrls End------------");
            return JSONObject.toJSONString(params);
        }

        String companyOneCode = bldLimitAmount.getCompanyOne().geteCode();
        String companyTwoCode = bldLimitAmount.getCompanyTwo().geteCode();
        if (supervisorCode.equals(companyOneCode)) {
            bldLimitAmount.setSignTimeOne(saveSignUrlsEntity.getSignTime());
            empj_BldLimitAmountDao.update(bldLimitAmount);
        } else if (supervisorCode.equals(companyTwoCode)) {
            bldLimitAmount.setSignTimeTwo(saveSignUrlsEntity.getSignTime());
            empj_BldLimitAmountDao.update(bldLimitAmount);
        } else {
            params.put("code", 105);
            params.put("message", "Not Find SupervisorCompany");
            System.out.println(
                "-------Not Find SupervisorCompany Blank-PanYunDingDingWebService saveSignUrls End------------");
            return JSONObject.toJSONString(params);
        }


        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection("saveSign-webservice");
        tgpf_SocketMsg.setMsgContentArchives(result);
//        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);


        params.put("code", 200);
        params.put("message", "Success");

        System.out.println("-----------------------PanYunDingDingWebService saveSignUrls End-----------------------");
        return JSONObject.toJSONString(params);
    }
}
