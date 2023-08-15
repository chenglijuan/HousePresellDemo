package zhishusz.housepresell.external.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import zhishusz.housepresell.external.service.GjjService;
import zhishusz.housepresell.util.JsonUtil;

/*
 * Controller公积金接口对接
 * Company：ZhiShuSZ
 */
@Controller
public class Gjj_Controller {

    @Autowired
    private GjjService gjjService;


    //根据传入的预售证编号返回该商品房预售资金托管协议
    @RequestMapping(value = "/Gjj_getInfoByeCodeFromPresellSystem", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public String getInfoByeCodeFromPresellSystem(HttpServletRequest request, @RequestBody JSONObject obj) {
        Properties properties = null;

        properties = gjjService.getInfoByeCodeFromPresellSystemExecute(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }



    //中心在房源准入发布后将发布的房源信息推送给正泰；推送内容：开发公司名称、项目名称、施工编号、楼幢ID（托管）、楼栋ID（公积金）
    @RequestMapping(value = "/Gjj_getApproveInfo", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public String getApproveInfo(HttpServletRequest request, @RequestBody JSONObject obj) {
        Properties properties = null;
        System.out.println("2");
        properties = gjjService.getApproveInfo(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }

    //每月初第一个工作日，中心将上月进入可放款审批流程的楼栋信息推送给正泰（正泰标注状态，被标记的楼幢不需再做三这个接口推送）
    @RequestMapping(value = "/Gjj_getLoanApproveInfo", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public String Gjj_getLoanApproveInfo(HttpServletRequest request,@RequestBody JSONObject obj) {
        Properties properties = null;
        System.out.println("4");
        properties = gjjService.getLoanApproveInfo(request,obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }
//    中心贷款终审后将贷款申请信息推送给正泰（每天）
    @RequestMapping(value = "/Gjj_getFinalReviewOfLoanByDay", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public String Gjj_getFinalReviewOfLoanByDay(HttpServletRequest request, @RequestBody JSONObject obj) {
        Properties properties = null;
        System.out.println("5");
        properties = gjjService.getFinalReviewOfLoanByDay(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;
    }



}
