package zhishusz.housepresell.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_AFForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_ProjProgInspection_AFService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller添加操作：项目进度巡查-主 Company：ZhiShuSZ
 */
@Controller
public class Empj_ProjProgInspection_AFController extends BaseController {
    @Autowired
    private Empj_ProjProgInspection_AFService service;

    @RequestMapping(value = "/Empj_ProjProgInspection_AF", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String execute(@RequestBody Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
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
    
    @RequestMapping(value = "/Empj_ProjProgInspection_AFList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
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

        return jsonStr;
    }
    
    @RequestMapping(value = "/Empj_ProjProgInspection_AFBatchDelete", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String batchDeleteExecute(@RequestBody Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
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

        return jsonStr;
    }
    
    @RequestMapping(value = "/Empj_ProjProgInspection_AFDetail", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String detailExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
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

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
    
    @RequestMapping(value = "/Empj_ProjProgInspectionSave", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String saveExecute(@RequestBody Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.saveExecute(model);
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
    
    @RequestMapping(value = "/Empj_ProjProgInspectionExeport", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String exeportExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.exeportExecute(model);
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
    
    @RequestMapping(value = "/Empj_ProjProgInspectionForecast", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String forecastExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.forecastExecute(model);
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
    
    
    @RequestMapping(value = "/Empj_ProjProgInspection_AFViewList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String viewListExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.viewListExecute(model);
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
    
    @RequestMapping(value = "/Empj_ProjProgInspection_AFViewDetail", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String viewDetailExecute(@ModelAttribute Empj_ProjProgInspection_AFForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.viewDetailExecute(model);
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
