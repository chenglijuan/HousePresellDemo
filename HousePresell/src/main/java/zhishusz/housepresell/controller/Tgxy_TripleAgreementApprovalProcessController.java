package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementApprovalProcessInterfaceService;
import zhishusz.housepresell.service.Tgxy_TripleAgreementApprovalProcessService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller提交：提交审批 三方协议 Company：ZhiShuSZ
 */
@Controller
public class Tgxy_TripleAgreementApprovalProcessController extends BaseController {
    @Autowired
    private Tgxy_TripleAgreementApprovalProcessService service;
    
    @Autowired
    private Tgxy_TripleAgreementApprovalProcessInterfaceService service1;

    @RequestMapping(value = "/Tgxy_TripleAgreementApprovalProcess", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@ModelAttribute Tgxy_TripleAgreementForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.execute(model);
                break;
            }
            default: {
                properties = new MyProperties();
                properties.put(S_NormalFlag.result, S_NormalFlag.fail);
                properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
                break;
            }
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Tgxy_TripleAgreementApprovalProcess", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Tgxy_TripleAgreementApprovalTest", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute1(@ModelAttribute Tgxy_TripleAgreementForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service1.testService();
                break;
            }
            default: {
                properties = new MyProperties();
                properties.put(S_NormalFlag.result, S_NormalFlag.fail);
                properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
                break;
            }
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Tgxy_TripleAgreementApprovalProcess", model, properties, jsonStr);

        return jsonStr;
    }
}
