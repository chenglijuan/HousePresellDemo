package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_NodePredictionForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_NodePrediction;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Service列表查询：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_BankAccountEscrowedListService
{
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_BankAccountEscrowedForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		Long bankId = model.getBankId();
		if(null != bankId && bankId > 0){
			//查询（托管银行）
			Emmp_BankInfo emmp_BankInfo = emmp_BankInfoDao.findById(bankId);
			model.setBank(emmp_BankInfo);
		}
		
		Long bankBranchId = model.getBankBranchId();
		if(null != bankBranchId && bankBranchId > 0)
		{
			//托管银行开户行
			Emmp_BankBranch emmp_BankBranch = emmp_BankBranchDao.findById(bankBranchId);
			model.setBankBranch(emmp_BankBranch);
		}
		
		Integer totalCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao.createCriteriaForList( model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		if(totalCount > 0)
		{
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.createCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			tgxy_BankAccountEscrowedList = new ArrayList<Tgxy_BankAccountEscrowed>();
		}
		
		properties.put("tgxy_BankAccountEscrowedList", tgxy_BankAccountEscrowedList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	 /**
     * 加载托管账户列表
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties loadingListExecute(Tgxy_BankAccountEscrowedForm model) {
        Properties properties = new MyProperties();

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "当前托管账户id为空");
        }
        
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		if(StringUtils.isNotBlank(keyword)){
			model.setKeyword("%"+keyword+"%");
		}
	
	    Integer totalCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao.getQuery_Size(tgxy_BankAccountEscrowedDao.loadingListExecute(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		if(totalCount > 0)
		{
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.loadingListExecute(), model), pageNumber, countPerPage);
		}
		else
		{
			tgxy_BankAccountEscrowedList = new ArrayList<Tgxy_BankAccountEscrowed>();
		}

		properties.put("tgxy_BankAccountEscrowedList", tgxy_BankAccountEscrowedList);
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
