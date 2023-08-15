package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_PaymentBondForm;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_PaymentBondService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Controller操作：支付保函信息 Company：ZhiShuSZ
 */
@Controller
public class Empj_PaymentBondController extends BaseController {
    @Autowired
    private Empj_PaymentBondService service;
    @Autowired
    private Sm_AttachmentCfgRebuild rebuild3;

    @RequestMapping(value = "/Empj_PaymentBondAddlalala", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
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

        super.writeOperateHistory("Empj_PaymentBondAdd", model, properties, jsonStr);

        return jsonStr;
    }
    
    @RequestMapping(value = "/Empj_PaymentBondDetail", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String detailExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.detailExecute(model);
                break;
            }
            default: {
                properties = new MyProperties();
                properties.put(S_NormalFlag.result, S_NormalFlag.fail);
                properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
                break;
            }
        }
        
        if(MyBackInfo.isSuccess(properties))
        {
            properties.put("smAttachmentCfgList", rebuild3.getDetailForAdmin2((List)(properties.get("smAttachmentCfgList"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        // super.writeOperateHistory("Empj_PaymentBondList", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.listExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondList", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondBatchDelete", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String batchDeleteExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.batchDeleteExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondBatchDelete", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondAdd", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addExecute(@RequestBody Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.addExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondAdd", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondEdit", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String editExecute(@RequestBody Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.editExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondEdit", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondSubmit", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String submitExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.submitExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondSubmit", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondPrint", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String printExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.printExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondPrint", model, properties, jsonStr);

        return jsonStr;
    }

    @RequestMapping(value = "/Empj_PaymentBondLoadBuildByProjectId", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String loadBuildByProjectIdExecute(@ModelAttribute Empj_PaymentBondForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.loadBuildByProjectIdExecute(model);
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

        // super.writeOperateHistory("Empj_PaymentBondPrint", model, properties, jsonStr);

        return jsonStr;
    }

}
