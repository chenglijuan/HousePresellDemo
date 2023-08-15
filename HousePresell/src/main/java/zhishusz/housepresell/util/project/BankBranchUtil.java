package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by Dechert on 2018-11-07.
 * Company: zhishusz
 */
@Service
public class BankBranchUtil {
    @Autowired
    private Emmp_BankBranchDao bankBranchDao;
    @Autowired
    private Tgxy_BankAccountEscrowedDao bankAccountEscrowedDao;
    @Autowired
    private Tgpj_BankAccountSupervisedDao bankAccountSupervisedDao;

    public String canDelete = "canDelete";
    public String deleteInfo = "deleteInfo";

    public HashMap<String,Object> canDeleteThisBankBranck(Long tableId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        if (tableId == null) {
//            System.out.println("BankBranchUtil: tableId is null!");
            stringObjectHashMap.put(deleteInfo, "BankBranchUtil: tableId is null!");
            stringObjectHashMap.put(canDelete, false);
            return stringObjectHashMap;
        }
        Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
        bankAccountEscrowedForm.setTheState(S_TheState.Normal);
        bankAccountEscrowedForm.setBankBranchId(tableId);
        Integer pageSizeOfBankAccount = bankAccountEscrowedDao.findByPage_Size(bankAccountEscrowedDao.getQuery_Size(bankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));
        if(pageSizeOfBankAccount>0){
            stringObjectHashMap.put(deleteInfo, "托管账户中使用了该开户行");
            stringObjectHashMap.put(canDelete, false);
            return stringObjectHashMap;
        }
//        Tgpj_BankAccountSupervisedForm bankAccountSupervisedForm = new Tgpj_BankAccountSupervisedForm();
//        bankAccountSupervisedForm.setTheState(S_TheState.Normal);
//        bankAccountSupervisedForm.setBankBranchId(tableId);
//        pageSizeOfBankAccount = bankAccountSupervisedDao.findByPage_Size(bankAccountSupervisedDao.getQuery_Size(bankAccountSupervisedDao.getBasicHQL(), bankAccountSupervisedForm));
//        if(pageSizeOfBankAccount>0){
//            stringObjectHashMap.put(deleteInfo, "监管账户中使用了该开户行");
//            stringObjectHashMap.put(canDelete, false);
//            return stringObjectHashMap;
//        }
        stringObjectHashMap.put(canDelete, true);
        stringObjectHashMap.put(deleteInfo, "该开户行可以删除");
        return stringObjectHashMap;
    }

}
