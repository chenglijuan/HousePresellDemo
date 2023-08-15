package zhishusz.housepresell.util;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * 实现Oracle数据库中文字段按照拼音排序问题
 * 
 */
public class OracleOrder extends Order
{
	private static final long serialVersionUID = 3195511288628940855L;

	private String propertyName;
 
    private boolean ascending;
 
    protected OracleOrder(String propertyName, boolean ascending)
    {
        super(propertyName, ascending);
        this.propertyName = propertyName;
        this.ascending = ascending;
    }
    
	public static OracleOrder asc(String propertyName) {
		return new OracleOrder(propertyName, true);
	}

	public static OracleOrder desc(String propertyName) {
		return new OracleOrder(propertyName, false);
	}
    
    /**
     * 只考虑按一个字段排序的情况
     */
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException
    {
        String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
        return " nlssort(" + columns[0] + ",'NLS_SORT=SCHINESE_PINYIN_M') " + (ascending ? "" : "desc");
    }
 
    public static OracleOrder getOrder(String propertyName, boolean ascending)
    {
        return new OracleOrder(propertyName, ascending);
    }
}

