package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_CyberBankStatementListService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String billTimeStamp = model.getBillTimeStamp();
		//总笔数
		Integer transactionCount = 0;
		//总金额
		Double transactionAmount = 0.0;
		Long sumCount = 0l;
		Double sumAmount = 0.0;
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null == billTimeStamp || billTimeStamp.trim().isEmpty())
		{
			model.setBillTimeStamp(null);
		}
		
		model.setTheState(S_TheState.Normal);
		
		Integer totalCount = tgpf_CyberBankStatementDao.findByPage_Size(tgpf_CyberBankStatementDao.getQuery_Size(tgpf_CyberBankStatementDao.getBasicHQL2(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList;
		if(totalCount > 0)
		{
			tgpf_CyberBankStatementList = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), model), pageNumber, countPerPage);
			for (Tgpf_CyberBankStatement tgpf_CyberBankStatement : tgpf_CyberBankStatementList)
			{
				Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
				form.setTheState(S_TheState.Normal);
				form.setBusiState("0");
				form.setMainTable(tgpf_CyberBankStatement);
				
				//查询交易总笔数
				transactionCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
				if(transactionCount >  0){
					
					String queryTransactionAmountCondition = "  nvl(sum(income),0)  ";
					transactionAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form,queryTransactionAmountCondition));
				}
				
				tgpf_CyberBankStatement.setTransactionCount(transactionCount);
				tgpf_CyberBankStatement.setTransactionAmount(transactionAmount);
			}
			
			//统计所有总笔数和总金额
			List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementLists = tgpf_CyberBankStatementDao.findByPage(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), model));
			for (Tgpf_CyberBankStatement tgpf_CyberBankStatement : tgpf_CyberBankStatementLists)
			{
				Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
				form.setTheState(S_TheState.Normal);
				form.setBusiState("0");
				form.setMainTable(tgpf_CyberBankStatement);
				
				//查询交易总笔数
				transactionCount = tgpf_CyberBankStatementDtlDao.findByPage_Size(tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
				if(transactionCount >  0){
					
					String queryTransactionAmountCondition = "  nvl(sum(income),0)  ";
					transactionAmount = (Double) tgpf_CyberBankStatementDtlDao.findOneByQuery(tgpf_CyberBankStatementDtlDao.getSpecialQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form,queryTransactionAmountCondition));
				}
				
				sumCount += transactionCount;
				sumAmount += transactionAmount;
			}
			
		}
		else
		{
			tgpf_CyberBankStatementList = new ArrayList<Tgpf_CyberBankStatement>();
		}
		
		
		
		properties.put("sumCount", sumCount);
		properties.put("sumAmount", sumAmount);
		
		properties.put("tgpf_CyberBankStatementList", tgpf_CyberBankStatementList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
