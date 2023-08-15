package zhishusz.housepresell.external.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zhishusz.housepresell.external.service.UploadBalanceOfAccountService;
import zhishusz.housepresell.util.JsonUtil;
;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * @Author: chenglijuan
 * @Data: 2022/5/20  10:48
 * @Decription:
 * @Modified:
 */
@Controller
@Transactional
public class UploadBalanceOfAccountController {


    @Autowired
    private UploadBalanceOfAccountService uploadBalanceOfAccountService;

    @RequestMapping(value = "/uploadBalanceOfAccount", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String execute(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {
        /** 上传网银数据**/

        Properties properties = null;
//        System.out.println("obj="+obj);
        properties = uploadBalanceOfAccountService.uploadBalanceOfAccountExecute(request, obj);

        String jsonStr = new JsonUtil().propertiesToJson(properties);

        return jsonStr;

    }
}
