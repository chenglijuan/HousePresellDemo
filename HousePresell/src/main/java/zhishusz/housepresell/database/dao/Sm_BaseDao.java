package zhishusz.housepresell.database.dao;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：审批流程：审核通过
 * Company：ZhiShuSZ
 */
@Repository
public class Sm_BaseDao
{
	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}

	public <T> void update(T object)
	{
		getCurrentSession().update(object);
		getCurrentSession().flush();
		return;
	}
	
	public <T> Serializable save(T object)
	{
		Serializable entity = getCurrentSession().save(object);
		getCurrentSession().flush();
		return entity;
	}
	
	public <T> void saveOrUpdate(T object)
	{
		getCurrentSession().saveOrUpdate(object);
		getCurrentSession().flush();
	}

	public <T> T findById(Class<T> entityClass, Long id)
	{
		if (id != null && id > 0)
		{
			return (T) getCurrentSession().get(entityClass, id);

		}
		else
		{
			return null;
		}
	}
}
