package zhishusz.housepresell.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.service.Empj_PaymentGuaranteeApplyUpdateService;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyProperties;

/*
 * Controller批量删除：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Controller
public class Empj_PaymentGuaranteeApplyUpdateController extends BaseController
{
	@Autowired
	private Empj_PaymentGuaranteeApplyUpdateService service;
	
	@RequestMapping(value="/Empj_PaymentGuaranteeApplyUpdate",produces="text/html;charset=UTF-8",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String execute(@ModelAttribute Empj_PaymentGuaranteeForm model, HttpServletRequest request)
	{
		model.init(request);
		
		Properties properties = null;
		switch(model.getInterfaceVersion())
		{
			case 19000101:
			{
//				Empj_PaymentGuaranteeForm empj_PaymentGuarantee = new Empj_PaymentGuaranteeForm();
//				empj_PaymentGuarantee.setGuaranteeNo("20180909");	
//
//				empj_PaymentGuarantee.setTableId(65632l);
//				empj_PaymentGuarantee.setApplyDate("2018-10-12");//申请日期
//				empj_PaymentGuarantee.setGuaranteeType("2");//支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
//				empj_PaymentGuarantee.setAlreadyActualAmount(52220.0);// 项目建设工程已实际支付金额 
//				empj_PaymentGuarantee.setActualAmount(111.0); //项目建设工程待支付承保金额 
//				empj_PaymentGuarantee.setGuaranteedAmount(50.0); //已落实支付保证金额
//				empj_PaymentGuarantee.setRemark("dasfasdf"); //备注
//				empj_PaymentGuarantee.setGuaranteeCompanyId(10747l);
//				empj_PaymentGuarantee.setCompanyId(10751l);
//				empj_PaymentGuarantee.setProjectId(11041l);
//				
//				model.setEmpj_PaymentGuarantee(empj_PaymentGuarantee);
//				List<Empj_PaymentGuaranteeChildForm> empj_PaymentGuaranteeChildList = new ArrayList<Empj_PaymentGuaranteeChildForm>();
//				Empj_PaymentGuaranteeChildForm Empj_PaymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
//				
//				Empj_PaymentGuaranteeChildForm.setTableId(65633l);
//				Empj_PaymentGuaranteeChildForm.setEmpj_BuildingInfoId(65395l);
//				Empj_PaymentGuaranteeChildForm.setBuildProjectPaid(15000.0);//楼幢项目建设已实际支付金额 	    		
//				Empj_PaymentGuaranteeChildForm.setBuildProjectPay(25000.0);//楼幢项目建设待支付承保金额（元）	  
//				empj_PaymentGuaranteeChildList.add(Empj_PaymentGuaranteeChildForm);
//				
//				model.setEmpj_PaymentGuaranteeChildList(empj_PaymentGuaranteeChildList);
				
				
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
		
		String jsonStr = new JsonUtil().propertiesToJson(properties);
		
		super.writeOperateHistory("Empj_PaymentGuaranteeApplyUpdate", model, properties, jsonStr);
		
		return jsonStr;
	}
}
