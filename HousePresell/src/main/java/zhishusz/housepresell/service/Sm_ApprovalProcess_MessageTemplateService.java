package zhishusz.housepresell.service;

import zhishusz.housepresell.database.dao.Sm_BaseDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/*
 * Service审批流程：消息模板范围
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_MessageTemplateService
{
	@Autowired
	private Sm_BaseDao sm_BaseDao;
	@Autowired
	private Gson gson;

	public Map<String, Object> execute(Sm_ApprovalProcess_Workflow approvalProcess_Workflow)
	{
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = approvalProcess_Workflow.getApprovalProcess_AF();

		Class oldPoClass = null;
		Class newFormClass = null;
		try
		{
			String oldPoClassName = sm_ApprovalProcess_AF.getSourceType(); //po类名
			oldPoClass = Class.forName(oldPoClassName);
			String poName = oldPoClassName.substring(oldPoClassName.lastIndexOf(".")+1);
			newFormClass = Class.forName("zhishusz.housepresell.controller.form."+poName+"Form");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//获取数据库中的数据
		Long poId = sm_ApprovalProcess_AF.getSourceId();
		Object oldPo = sm_BaseDao.findById(oldPoClass, poId);
		Object  newPo = null;
		if(sm_ApprovalProcess_AF.getOrgObjJsonFilePath()!=null && sm_ApprovalProcess_AF.getOrgObjJsonFilePath().length() > 0)
		{
			String expectObjJson = sm_ApprovalProcess_AF.getExpectObjJson().trim();
			newPo =  gson.fromJson(expectObjJson, newFormClass);
		}

		//-------------------------------------定义模板规范start--------------------------------------------------------------//

		Map<String,Object> templateObject = new HashMap<>();
		templateObject.put("oldPo",oldPo);
		templateObject.put("newPo",newPo);
		templateObject.put("af",sm_ApprovalProcess_AF); //申请单
		templateObject.put("workflow",approvalProcess_Workflow); //当前处理节点

		//-------------------------------------定义模板规范end----------------------------------------------------------------//

		return templateObject;
	}
}
