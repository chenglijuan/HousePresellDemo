package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：监管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpj_BankAccountSupervisedListService {
    @Autowired
    private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
    @Autowired
    private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpj_BankAccountSupervisedForm model) {
        Properties properties = new MyProperties();
        if(StringUtils.isEmpty(model.getOrderBy())){
            model.setOrderBy(null);
        }
        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        String keyword = model.getKeyword();
        Long buildingId = model.getBuildingId();
        Boolean flag;
        if (buildingId != null && buildingId > 0) {
            flag = true;
        } else {
            flag = false;
        }
//		System.out.println("flag is "+flag);
        Integer totalCount = 0;
        List<Tgpj_BankAccountSupervised> tgpj_BankAccountSupervisedList = new ArrayList<>();

//		List<Tgpj_BankAccountSupervised> filteList=new ArrayList<>();
//		Integer totalCount = tgpj_BankAccountSupervisedDao.findByPage_Size(tgpj_BankAccountSupervisedDao.
//				getQuery_Size(tgpj_BankAccountSupervisedDao.getSizeHQL(flag), model));
//		if(flag){
//			List<Tgpj_BankAccountSupervised> tempList = tgpj_BankAccountSupervisedDao.findByPage(tgpj_BankAccountSupervisedDao.
//					getQuery(tgpj_BankAccountSupervisedDao.getBasicHQL(), model));
//			filteList= tempList.stream().filter(tgpj_bankAccountSupervised -> {
//				Integer state = tgpj_bankAccountSupervised.getTheState();
//				if (state == S_TheState.Normal)
//				{
////					List<Empj_BuildingInfo> buildingInfoList = tgpj_bankAccountSupervised.getBuildingInfoList();
////					for (Empj_BuildingInfo empj_buildingInfo : buildingInfoList)
////					{
////						if (empj_buildingInfo.getTableId().longValue() == buildingId.longValue())
////						{
////							return true;
////						}
////					}
//
//				}
//				else
//				{
//					return false;
//				}
//				return false;
//			}).collect(Collectors.toList());
//			totalCount=filteList.size();
//		}else{
        List<Tgpj_BankAccountSupervised> filteList =new ArrayList();
        if (flag) {
            Empj_BuildingAccountSupervisedForm empj_buildingAccountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
            empj_buildingAccountSupervisedForm.setBuildingInfoId(buildingId);
            empj_buildingAccountSupervisedForm.setTheState(S_TheState.Normal);
            List<Empj_BuildingAccountSupervised> buildingAccountSupervisedList = empj_buildingAccountSupervisedDao.findByPage(empj_buildingAccountSupervisedDao.getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(), empj_buildingAccountSupervisedForm));
            for(Empj_BuildingAccountSupervised buildingAccountSupervised:buildingAccountSupervisedList){
                filteList.add(buildingAccountSupervised.getBankAccountSupervised());
            }
//            for (Tgpj_BankAccountSupervised buildingAccountSupervised: filteList){
//
//            }
            totalCount=filteList.size();
        } else {
            totalCount = tgpj_BankAccountSupervisedDao.findByPage_Size(tgpj_BankAccountSupervisedDao.
                    createCriteriaForList(model));
        }

//		}

//		System.out.println("totalCount is "+totalCount+" flag is "+flag);

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;
//		if (totalCount % countPerPage > 0) totalPage++;
        if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;

        if (totalCount > 0) {
            if (flag) {
                for (int i = 0; i < totalPage; i++) {
                    int getIndex = (pageNumber - 1) * countPerPage;
                    if (getIndex < filteList.size()) {
                        tgpj_BankAccountSupervisedList.add(filteList.get(getIndex));
                    }
                }
            } else {
                tgpj_BankAccountSupervisedList = tgpj_BankAccountSupervisedDao.findByPage(tgpj_BankAccountSupervisedDao.
                        createCriteriaForList(model), pageNumber, countPerPage);
            }
        } else {
            tgpj_BankAccountSupervisedList = new ArrayList<Tgpj_BankAccountSupervised>();
        }
//		System.out.println("tgpj_BankAccountSupervisedList size is "+tgpj_BankAccountSupervisedList.size());

        properties.put("tgpj_BankAccountSupervisedList", tgpj_BankAccountSupervisedList);
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
