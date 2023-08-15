package zhishusz.housepresell.controller.emmp;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.BaseController;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.emmp.Emmp_CompanySupervisionListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;

/*
 * Controller列表查询：监理机构 Company：ZhiShuSZ
 */
@Controller
public class Emmp_CompanySupervisionListController extends BaseController {
    @Autowired
    private Emmp_CompanySupervisionListService service;
    @Autowired
    private Emmp_CompanyInfoRebuild rebuild;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/Emmp_CompanySupervisionList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request) {
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
            properties.put("emmp_CompanyInfoList", rebuild.execute((List)(properties.get("emmp_CompanyInfoList"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Emmp_CompanySupervisionList", model, properties, jsonStr);

        return jsonStr;
    }
}
