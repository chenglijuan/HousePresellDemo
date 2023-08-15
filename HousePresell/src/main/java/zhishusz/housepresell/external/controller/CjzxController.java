package zhishusz.housepresell.external.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zhishusz.housepresell.external.service.CjzxService;
import zhishusz.housepresell.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


/*
 * Controller 促进中心同步三方协议接口
 * Company：ZhiShuSZ
 */
@Controller
@Transactional
public class CjzxController {


    @Autowired
    private CjzxService cjzxService;



    /**
     * 通过预售楼栋id获取协议编号和版本号
     * @param request
     * @param response
     * @param obj
     */
    @RequestMapping(value = "/getXybhByBuildingId", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public String getXybhByBuildingId(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

        Properties properties = null;
        properties = cjzxService.getXybhByBuildingId(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;

    }


    /**
     * 接收合同信息以及三方协议附件
     * @param request
     * @param response
     * @param obj
     */
    @RequestMapping(value = "/getXyContractInfo", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String getXyContractInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {

        Properties properties = null;
        properties = cjzxService.getXyContractInfo(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;



    }

    /**
     * 接收撤销合同信息  批量删除
     * @param request
     * @param response
     * @param obj
     */
    @RequestMapping(value = "/getCxContractInfo", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String getCxContractInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONArray obj) {
        Properties properties = null;
        String jsonStr = "";
        try{
            System.out.println("obj="+obj);
            properties = cjzxService.getCxContractInfo(request, obj);

            jsonStr = new JsonUtil().propertiesToJson(properties);

        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }

}
