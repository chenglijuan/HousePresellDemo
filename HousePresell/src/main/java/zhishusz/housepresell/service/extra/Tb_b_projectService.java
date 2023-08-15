package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_projectForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_companyDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_projectDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Tb_b_company;
import zhishusz.housepresell.database.po.extra.Tb_b_project;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库-项目取数
 * 
 * @ClassName: Tb_b_projectService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午11:12:10
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tb_b_projectService
{
	@Autowired
	private Tb_b_projectDao dao;
	@Autowired
	private Tb_b_companyDao comDao;
	@Autowired
	private Empj_ProjectInfoDao projectInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tb_b_projectForm model)
	{
		Properties properties = new MyProperties();

		// 获取请求类型
		String type = model.getType();

		switch (type)
		{
		case "listByCompany":
			// 根据开发企业rowguid查询项目列表
			String companyid = model.getCOMPANYID();

			if (null == companyid || companyid.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择正确的企业信息");
			}

			List<Tb_b_project> list = new ArrayList<Tb_b_project>();

			list = dao.getProjectListByCompanyId(companyid);

			properties.put("projectList", list);

			break;

		case "detail":

			// 根据项目rowguid查询项目详情
			String rowguid = model.getROWGUID();

			if (null == rowguid || rowguid.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择项目");
			}

			Tb_b_project vo = new Tb_b_project();

			vo = dao.getProjectDetail(rowguid);

			properties.put("projectDetail", vo);

			break;

		case "list":

			/*
			 * 根据当前登录人所属开发企业加载项目列表
			 * 开发企业与中间库关联字段extraId
			 */
			Sm_User user = model.getUser();
			if (null == user)
			{
				return MyBackInfo.fail(properties, "请先登录");
			}

			// 获取开发企业
			Emmp_CompanyInfo company = user.getCompany();
			if (null == company)
			{
				return MyBackInfo.fail(properties, "未查询到当前登录用的所属开发企业");
			}

			String externalId = company.getExternalId();
			if (null == externalId || externalId.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "当前登录用的所属开发企业未同步预售系统");
			}

			// 根据中间库开发企业rowguid查询项目列表
			List<Tb_b_project> listProject = dao.getProjectListByCompanyId(externalId);
			if (null == listProject || listProject.size() < 1)
			{
				return MyBackInfo.fail(properties, "该企业下没有有效的预售项目信息！");
			}

			properties.put("projectList", listProject);

			break;

		case "listByLocal":

			/*
			 * 根据当前登录人所属开发企业加载托管系统中项目列表
			 * 
			 */
			user = model.getUser();
			if (null == user)
			{
				return MyBackInfo.fail(properties, "请先登录");
			}

			// 获取开发企业
			company = user.getCompany();
			if (null == company)
			{
				return MyBackInfo.fail(properties, "未查询到当前登录用的所属开发企业");
			}

			Empj_ProjectInfoForm projectModel = new Empj_ProjectInfoForm();

			projectModel.setDevelopCompanyId(company.getTableId());
			projectModel.setTheState(S_TheState.Normal);
			projectModel.setBusiState(S_BusiState.HaveRecord);
			projectModel.setApprovalState(S_ApprovalState.Completed);
			

			List<Empj_ProjectInfo> listPro = new ArrayList<Empj_ProjectInfo>();

			listPro = projectInfoDao.findByPage(projectInfoDao.getQuery(projectInfoDao.getBasicHQL(), projectModel));

			if (null != listPro && listPro.size() > 0)
			{
				properties.put("projectListByLocal", listPro);

			}
			else
			{
				return MyBackInfo.fail(properties, "未查到有效的项目信息");
			}

			break;

		default:

			return MyBackInfo.fail(properties, "请选择正确的请求类型");

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}
