package zhishusz.housepresell.controller;

import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Tgxy_BankAccountEscrowedListService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Tgxy_BankAccountEscrowedRebuild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/*
 * Controller列表查询：托管账户
 * Company：ZhiShuSZ
 * */
@Controller
public class Tgxy_BankAccountEscrowedViewListController extends BaseController
{
	@Autowired
	private Tgxy_BankAccountEscrowedListService service;
	@Autowired
	private Tgxy_BankAccountEscrowedRebuild rebuild;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@RequestMapping(value="/Tgxy_BankAccountEscrowedViewPreList",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
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
			properties.put("tgxy_BankAccountEscrowedList", rebuild.executeForViewSelectList((List)(properties.get("tgxy_BankAccountEscrowedList"))));
		}
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Tgxy_BankAccountEscrowedViewPreList", model, properties, jsonStr);
		
		return jsonStr;
	}
	
	/**
	 * 加载托管账户列表
     * @param model
     * @param request
     * @return
     */
	@SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value="/loadingBankAccount",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String loadingBankAccount(@ModelAttribute Tgxy_BankAccountEscrowedForm model, HttpServletRequest request)
    {
        model.init(request);
        
        Properties properties = null;
        switch(model.getInterfaceVersion())
        {
            case 19000101:
            {
                properties = service.loadingListExecute(model);
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
			properties.put("tgxy_BankAccountEscrowedList", rebuild.loadingListExecute((List)(properties.get("tgxy_BankAccountEscrowedList"))));
		}
        
        String jsonStr = new JsonUtil().propertiesToJson(properties);
        
        return jsonStr;
    }
}
