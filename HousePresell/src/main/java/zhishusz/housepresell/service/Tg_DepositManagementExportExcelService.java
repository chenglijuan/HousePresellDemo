package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tg_DepositManagementForm;
import zhishusz.housepresell.database.dao.Tg_DepositManagementDao;
import zhishusz.housepresell.database.po.Tg_DepositManagement;
import zhishusz.housepresell.database.po.state.S_DepositState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tg_DepositManagementInProgressTemplate;
import zhishusz.housepresell.util.excel.model.Tg_DepositManagementTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：存单管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_DepositManagementExportExcelService
{
	@Autowired
	private Tg_DepositManagementDao tg_DepositManagementDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_DepositManagementForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		List<Tg_DepositManagement> tg_DepositManagementList;
		if (idArr == null || idArr.length < 1)
		{

			String keyword = model.getKeyword();

			if (keyword != null)
			{
				model.setKeyword("%"+keyword+"%");
			}

			if (model.getOrderBy() != null && model.getOrderBy().length() > 0)
			{
				String[] orderByAr = model.getOrderBy().split(" ");
				if ("startDateStr".equals(orderByAr[0]))
				{
					model.setOrderBy("startDate");
				}
				else if ("stopDateStr".equals(orderByAr[0]))
				{
					model.setOrderBy("stopDate");
				}
				else
				{
					model.setOrderBy(orderByAr[0]);
				}
				model.setOrderByType(orderByAr[1]);
			}
			else
			{
				if (S_DepositState.InProgress.equals(model.getDepositState()))
				{
					model.setOrderBy("startDate");
				}
				else
				{
					model.setOrderBy("stopDate desc, startDate");
				}
				model.setOrderByType("desc");
			}

			if (model.getDepositState() == null || model.getDepositState().length() == 0)
			{
				model.setExceptDepositState("03");
			}

			if (model.getStartDateStr() != null && model.getStartDateStr().length() > 0)
			{
				model.setStartDate(MyDatetime.getInstance().stringToLong(model.getStartDateStr()));
			}

			if (model.getStopDateStr() != null && model.getStopDateStr().length() > 0)
			{
				model.setStopDate(MyDatetime.getInstance().stringToLong(model.getStopDateStr()));
			}

			if ("".equals(model.getDepositState()))
			{
				model.setDepositState(null);
			}

			if ("".equals(model.getDepositProperty()))
			{
				model.setDepositProperty(null);
			}

			if ("".equals(model.getBankOfDepositId()))
			{
				model.setBankOfDepositId(null);
			}

			tg_DepositManagementList = tg_DepositManagementDao.findByPage(tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getBasicHQL(), model));
		}
		else
		{
			tg_DepositManagementList = tg_DepositManagementDao.findByPage(tg_DepositManagementDao.getQuery(tg_DepositManagementDao.getExcelListHQL(), model));
		}

		ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();

		String depositState = model.getDepositState();
		String depositStateStr = "";

		Properties propertiesExport = new Properties();

		if (model.getDepositState() == null || model.getDepositState().length() == 0)
		{
			depositStateStr = "存单存入列表信息";

			propertiesExport = exportToExcelUtil.execute(tg_DepositManagementList, Tg_DepositManagementTemplate.class, depositStateStr);

			if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
			{
				return properties;
			}
		}
		else if (depositState.equals("02"))
		{
			depositStateStr = "存单提取列表信息";

			propertiesExport = exportToExcelUtil.execute(tg_DepositManagementList, Tg_DepositManagementTemplate.class, depositStateStr);

			if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
			{
				return properties;
			}
		}
		else if (depositState.equals("03"))
		{
			depositStateStr = "存单正在办理列表信息";

			propertiesExport = exportToExcelUtil.execute(tg_DepositManagementList, Tg_DepositManagementInProgressTemplate.class, depositStateStr);

			if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
			{
				return properties;
			}
		}

		properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
		properties.put("tg_DepositManagementList", tg_DepositManagementList);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
