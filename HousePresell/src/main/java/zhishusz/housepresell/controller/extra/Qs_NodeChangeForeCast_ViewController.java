package zhishusz.housepresell.controller.extra;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.extra.Qs_RecordAmount_ViewForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.extra.Qs_NodeChangeForeCast_ViewService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller列表查询：节点变更预测表 Company：ZhiShuSZ
 */
@Controller
public class Qs_NodeChangeForeCast_ViewController extends BaseController {
    @Autowired
    private Qs_NodeChangeForeCast_ViewService service;

    @RequestMapping(value = "/Qs_NodeChangeForeCast_ViewList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@ModelAttribute Qs_RecordAmount_ViewForm model, HttpServletRequest request) {
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

        return jsonStr;
    }

    @RequestMapping(value = "/Qs_NodeChangeForeCast_ViewExportExcel", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String exportExecute(@ModelAttribute Qs_RecordAmount_ViewForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.exportExecute(model);
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

        return jsonStr;
    }
}
