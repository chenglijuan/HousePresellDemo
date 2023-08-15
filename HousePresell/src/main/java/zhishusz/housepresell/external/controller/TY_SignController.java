package zhishusz.housepresell.external.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.util.tianyin.TianyinUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
 * Controller电子签章
 * Company：ZhiShuSZ
 */
@Controller
public class TY_SignController
{

	public static String signUrl = "";

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@RequestMapping(value = "/TY_SignController", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_TripleAgreementForm model, HttpServletRequest request)
	{
		String fileKey= "$7366da3e-4c8d-488d-a95b-7d34e36809c6$1232830605";
		String docName= "Oracle透明网关常见问题处理.pdf";
		String accountId= "c594ca18-3809-4b01-8408-771e32fcc8a0";
		String initiatorAccountId = "279e974f-577d-47fa-86cd-6672c617043a";
		String result = "";
		try {
			TianyinUtil.signFlowsCreate1(fileKey,docName,accountId,initiatorAccountId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	@RequestMapping(value = "/TY_CallBackController", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.POST
	})
	@ResponseBody
	public void callBackExecute( HttpServletRequest request, @RequestBody JSONObject obj )
	{
//		System.out.println("obj="+obj);
		JSONArray finishDocUrlBeansResult = obj.getJSONArray("finishDocUrlBeans");
		Integer status = obj.getInteger("status");
		String action = obj.getString("action");
		//整个签署流程结束此次签署结束
		if("SIGN_FLOW_FINISH".equals(action)  && status.intValue() == 2){
			if(finishDocUrlBeansResult != null && finishDocUrlBeansResult.size() > 0){
				JSONObject bean = finishDocUrlBeansResult.getJSONObject(0);
				String finishFileKey = bean.getString("finishFileKey");
				if(StringUtils.isNotBlank(finishFileKey)){
					try {
					String preViewResult = TianyinUtil.getPreviewUrl(finishFileKey);
						System.out.println("preViewResult="+preViewResult);
						if(StringUtils.isNotBlank(preViewResult)){
							JSONObject previewData = JSONObject.parseObject(preViewResult).getJSONObject("data");
							System.out.println("previewData="+previewData);
							if(previewData != null){
								String url = previewData.getString("url");
								url = url.replaceAll("8084","8888");
								System.out.println("url="+url);
								signUrl = url;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			System.out.println("文件签署失败");
		}
	}
}
