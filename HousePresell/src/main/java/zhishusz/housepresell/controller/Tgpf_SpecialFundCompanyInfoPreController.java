package zhishusz.housepresell.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgpf_SpecialFundCompanyPreService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Emmp_CompanyInfoRebuild;

/*
 * Controller列表查询：预加载当前登录用户机构
 * 属于开发企业，则直接加载开发企业信息
 * 不属于开发企业，加载所有开发企业列表
 * Company：ZhiShuSZ
 */
@Controller
public class Tgpf_SpecialFundCompanyInfoPreController extends BaseController
{
	@Autowired
	private Tgpf_SpecialFundCompanyPreService service;
	@Autowired
	private Emmp_CompanyInfoRebuild rebuild;

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@RequestMapping(value = "/Tgpf_SpecialFundCompanyInfoPre", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST
	})
	@ResponseBody
	public String execute(@ModelAttribute Emmp_CompanyInfoForm model, HttpServletRequest request)
	{
		model.init(request);

		Properties properties = null;
		switch (model.getInterfaceVersion())
		{
		case 19000101:
		{
			properties = service.execute(model);
			break;
		}
		default:
		{
			properties = new MyProperties();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_ErrorInterfaceVersion);
			break;
		}
		}

		if (MyBackInfo.isSuccess(properties))
		{
			if (null != properties.get("emmp_CompanyInfoList"))
			{

				properties.put("emmp_CompanyInfoList",
						rebuild.execute((List) (properties.get("emmp_CompanyInfoList"))));
			}

			if (null != properties.get("companyInfo"))
			{
				properties.put("emmp_CompanyInfo", rebuild.execute((Emmp_CompanyInfo) (properties.get("companyInfo"))));
			}
		}

		String jsonStr = new JsonUtil().propertiesToJson(properties);

		super.writeOperateHistory("Tgpf_SpecialFundCompanyInfoPre", model, properties, jsonStr);

		return jsonStr;
	}
}
