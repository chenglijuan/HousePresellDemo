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
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.emmp.Emmp_CompanyInspectionUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller更新操作：无人机巡查机构 Company：ZhiShuSZ
 */
@Controller
public class Emmp_CompanyInspectionUpdateController extends BaseController {
    @Autowired
    private Emmp_CompanyInspectionUpdateService service;

    @RequestMapping(value = "/Emmp_CompanyInspectionUpdate", produces = "text/html;charset=UTF-8",
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

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Emmp_CompanyInspectionUpdate", model, properties, jsonStr);

        return jsonStr;
    }
}
