package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;
import zhishusz.housepresell.util.gjj.GJJ_LoanInforMation;
import zhishusz.housepresell.util.gjj.Gjj_BulidingRelation;

@Repository
public class Gjj_BulidingRelationDao extends BaseDao<Gjj_BulidingRelation> {

    public String getBasicHQL() {
        return "from Gjj_BulidingRelation where 1=1"
                + " <#if empjBuildingId??> and empjBuildingId=:empjBuildingId</#if>"
                + " <#if gjjBuildingId??> and gjjBuildingId=:gjjBuildingId</#if>";
    }

}
