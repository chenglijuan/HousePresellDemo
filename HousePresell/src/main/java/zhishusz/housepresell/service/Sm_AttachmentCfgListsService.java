package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：附件配置
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_AttachmentCfgListsService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		// 模糊查询
		String Keyword = model.getKeyword();
		/*
		 * String outKeyword=model.getKeyword();
		 * String keyword ="";
		 * List<Sm_BaseParameter> sm_BaseParameterList=null;
		 * if(null!=model.getKeyword() && model.getKeyword().length()>0){
		 * Sm_BaseParameterForm sm_BaseParameterForm=new Sm_BaseParameterForm();
		 * sm_BaseParameterForm.setKeyword("%" + model.getKeyword()+ "%");
		 * sm_BaseParameterForm.setParametertype("1");
		 * sm_BaseParameterList=
		 * sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getTheValueHQL(),
		 * sm_BaseParameterForm).getResultList();
		 * if(sm_BaseParameterList.size()>0){
		 * if( sm_BaseParameterList.size()==1){
		 * keyword=sm_BaseParameterList.get(0).getTheValue();
		 * }else{
		 * if(sm_BaseParameterList.get(0).getTheValue().length()<=2){
		 * keyword=sm_BaseParameterList.get(0).getTheValue();
		 * }else{
		 * keyword=sm_BaseParameterList.get(0).getTheValue().substring(0,
		 * sm_BaseParameterList.get(0).getTheValue().length()-1);
		 * }
		 * 
		 * }
		 * }
		 * }
		 */
		if (Keyword == null || Keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + Keyword + "%");
		}
		// 查询没有删除的数据
		model.setTheState(S_TheState.Normal);
		String sql1 = "";
		if (StringUtils.isBlank(model.getKeyword()))
		{
			sql1 = "SELECT busiType FROM SM_ATTACHMENTCFG  WHERE THESTATE = '0' GROUP BY busiType";
		}
		else
		{
			sql1 = "SELECT busiType FROM SM_ATTACHMENTCFG  WHERE THESTATE = '0' AND( basetheName like ?) GROUP BY busiType";
		}
		
		List list1 = null;
		
		if (StringUtils.isBlank(model.getKeyword()))
		{
			list1 = sessionFactory.getCurrentSession().createNativeQuery(sql1).list();
		}else{
			list1 = sessionFactory.getCurrentSession().createNativeQuery(sql1).setParameter(1, model.getKeyword()).list();
		}

		Integer totalCount = list1.size();
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Sm_AttachmentCfg> newAttachmentCfg = new ArrayList<Sm_AttachmentCfg>();
		List<Sm_AttachmentCfg> sm_AttachmentCfgList;
		if (totalCount > 0)
		{
			// 不进行模糊查询
			String sql = "";
			sql = " distinct(busiType) ";
			List list = sm_AttachmentCfgDao.findByPage(
					sm_AttachmentCfgDao.getSpecialQuery(sm_AttachmentCfgDao.getBasicHQLBylike(), model, sql),
					pageNumber, countPerPage);
			for (int i = 0; i < list.size(); i++)
			{
				String str = list.get(i).toString();
				model.setBusiType(str);
				sm_AttachmentCfgList = sm_AttachmentCfgDao
						.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), model));
				if (sm_AttachmentCfgList != null)
				{
					Sm_AttachmentCfg NewAttachmentCfg = new Sm_AttachmentCfg();
					String theName = "";
					for (Sm_AttachmentCfg resm_AttachmentCfg : sm_AttachmentCfgList)
					{
						NewAttachmentCfg.setBusiType(resm_AttachmentCfg.getBusiType());
						NewAttachmentCfg.setUserStart(resm_AttachmentCfg.getUserStart());
						NewAttachmentCfg.setCreateTimeStamp(resm_AttachmentCfg.getCreateTimeStamp());
						if (resm_AttachmentCfg.getUserStart() != null)
						{
							NewAttachmentCfg.setUserStart(resm_AttachmentCfg.getUserStart());
							NewAttachmentCfg.getUserStart()
									.setRealName(resm_AttachmentCfg.getUserStart().getRealName());
						}
						Sm_BaseParameterForm remodel = new Sm_BaseParameterForm();
						// 根据业务类型BusiType找到Sm_BaseParameter表中的tableid
						remodel.setTheValue(resm_AttachmentCfg.getBusiType());
						List<Sm_BaseParameter> sm_BaseParameter = sm_BaseParameterDao
								.getQuery(sm_BaseParameterDao.getBasicHQL(), remodel).list();
						if (sm_BaseParameter.size() == 1)
						{
							for (Sm_BaseParameter Sm_BaseParameter : sm_BaseParameter)
							{
								NewAttachmentCfg.setTableId(Sm_BaseParameter.getTableId());
								NewAttachmentCfg.setBusiType(
										resm_AttachmentCfg.getBusiType() + "+" + Sm_BaseParameter.getTheName());
							}
						}
						theName += resm_AttachmentCfg.getTheName() + ",";
					}
					if (theName.length() > 0)
					{
						// 去除最后一个逗号
						theName = theName.substring(0, theName.length() - 1);
					}
					NewAttachmentCfg.setTheName(theName);
					newAttachmentCfg.add(NewAttachmentCfg);
				}
			}
		}
		else
		{
			newAttachmentCfg = new ArrayList<Sm_AttachmentCfg>();
		}

		properties.put("newAttachmentCfg", newAttachmentCfg);
		properties.put(S_NormalFlag.keyword, Keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
