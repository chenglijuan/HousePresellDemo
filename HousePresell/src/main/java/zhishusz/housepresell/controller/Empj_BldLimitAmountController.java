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

import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_BldLimitAmountService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Empj_BldLimitAmountRebuild;

/*
 * Controller添加操作：申请表-进度节点变更 Company：ZhiShuSZ
 */
@Controller
public class Empj_BldLimitAmountController extends BaseController {
    @Autowired
    private Empj_BldLimitAmountService service;
    @Autowired
    private Empj_BldLimitAmountRebuild rebuild;
    
    /**
     * 保存
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountSave", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String saveExecute(@RequestBody Empj_BldLimitAmountForm model, HttpServletRequest request) {
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

    /**
     * 修改
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountEdit", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String editExecute(@RequestBody Empj_BldLimitAmountForm model, HttpServletRequest request) {
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

        return jsonStr;
    }

    /**
     * 删除
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountBatchDelete", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.deleteExecute(model);
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

    /**
     * 列表
     * 
     * @param model
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/Empj_BldLimitAmountList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
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

        if (MyBackInfo.isSuccess(properties)) {
            properties.put("empj_BldLimitAmountList", rebuild
                .empj_BldLimitAmountList((List<Empj_BldLimitAmount>)(properties.get("empj_BldLimitAmountList"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }

    /**
     * 详情
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountDetail", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String detailExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
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

        if (MyBackInfo.isSuccess(properties)) {
            properties.put("empj_BldLimitAmount",
                rebuild.getDetail((Empj_BldLimitAmount)(properties.get("empj_BldLimitAmount"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties, 8);

        return jsonStr;
    }

    /**
     * 审批详情
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountApprovalDetail", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String approvalDetailExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.approvalDetailExecute(model);
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
            properties.put("empj_BldLimitAmount",
                rebuild.getDetail((Empj_BldLimitAmount)(properties.get("empj_BldLimitAmount"))));
        }

        String jsonStr = new JsonUtil().propertiesToJson(properties, 8);

        return jsonStr;
    }

    /**
     * 提交
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountSubmit", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String submitExecute(@RequestBody Empj_BldLimitAmountForm model, HttpServletRequest request) {
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

        return jsonStr;
    }

    /**
     * 维护楼幢申请审批
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmount_DtlUpdate", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dtlUpdateExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.updateDtlExecute(model);
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

    /**
     * 报表查询
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountQueryCompanyList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryCompanyListExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.queryCompanyListExecute(model);
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

    /**
     * 报表查询
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountQueryReportList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String queryReportListExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.queryReportListExecute(model);
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

    /**
     * 报表导出
     * 
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/Empj_BldLimitAmountQueryReportExportList", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reportExportListExecute(@ModelAttribute Empj_BldLimitAmountForm model, HttpServletRequest request) {
        model.init(request);

        Properties properties = null;
        switch (model.getInterfaceVersion()) {
            case 19000101: {
                properties = service.reportExportListExecute(model);
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
