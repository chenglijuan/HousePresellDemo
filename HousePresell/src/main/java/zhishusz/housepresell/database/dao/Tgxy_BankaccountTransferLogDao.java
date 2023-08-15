package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;
import zhishusz.housepresell.database.po.Tgxy_BankaccountTransferLog;

@Repository
public class Tgxy_BankaccountTransferLogDao extends BaseDao<Tgxy_BankaccountTransferLog> {

    public String getBasicHQL() {
        return "from Tgxy_BankaccountTransferLog where 1=1"
                + " <#if fromId??> and fromId=:fromId</#if>"
                + " <#if toId??> and toId=:toId</#if>";
    }

}
