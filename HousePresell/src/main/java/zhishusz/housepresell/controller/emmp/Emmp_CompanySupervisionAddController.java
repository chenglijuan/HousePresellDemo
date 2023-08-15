package zhishusz.housepresell.controller.emmp;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.emmp.Emmp_CompanySupervisionAddService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;

/*
 * Controller添加操作：监理机构 Company：ZhiShuSZ
 */
@Controller
public class Emmp_CompanySupervisionAddController extends BaseController {
    @Autowired
    private Emmp_CompanySupervisionAddService service;
    @Autowired
    private Emmp_CompanyInfoRebuild rebuild;

    @RequestMapping(value = "/Emmp_CompanySupervisionAdd", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@RequestBody Emmp_CompanyInfoForm model, HttpServletRequest request) {

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

        if (MyBackInfo.isSuccess(properties)) {
            properties.put("emmp_CompanyInfo", rebuild.execute((Emmp_CompanyInfo)(properties.get("emmp_CompanyInfo"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Emmp_CompanySupervisionAdd", model, properties, jsonStr);

        return jsonStr;
    }
}
