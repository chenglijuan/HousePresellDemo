package zhishusz.housepresell.controller;

import java.util.Properties;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_BuildingInfoBldEscrowListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BuildingInfoRebuild;
import zhishusz.housepresell.util.MyBackInfo;

/*
 * Controller列表查询：托管终止楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_BuildingInfoBldEscrowListController extends BaseController
{
    @Autowired
    private Empj_BuildingInfoBldEscrowListService service;
    @Autowired
    private Empj_BuildingInfoRebuild rebuild;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value="/Empj_BuildingInfoBldEscrowList",produces="text/html;charset=UTF-8",method=
            {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String execute(@ModelAttribute Empj_BldEscrowCompletedForm model, HttpServletRequest request)
    {
        model.init(request);

        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.execute(model);
                break;
            }
            default :
            {
                properties = new MyProperties();
                properties.put(S_NormalFlag.result, S_NormalFlag.fail);
                properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
                break;
            }
        }

        if(MyBackInfo.isSuccess(properties))
        {
            properties.put("empj_BuildingInfoList", rebuild.execute((List)(properties.get("empj_BuildingInfoList"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        super.writeOperateHistory("Empj_BuildingInfoList", model, properties, jsonStr);

        return jsonStr;
    }
}
