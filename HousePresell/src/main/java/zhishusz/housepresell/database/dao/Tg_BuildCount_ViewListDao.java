package zhishusz.housepresell.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.database.po.Tg_BuildCount_View;

/*
 * Dao数据库操作：托管楼幢入账统计表报表
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_BuildCount_ViewListDao extends BaseDao<Tg_BuildCount_View>
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public String getBasicHQL()
    {
    	return "from Tg_BuildCount_View where 1=1"
    	+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword or bankName like :keyword )</#if>"
		+ " <#if companyName??> and companyName=:companyName</#if>"
		+ " <#if projectName??> and projectName=:projectName</#if>"
    	+ " <#if ECodeFroMconstruction??> and eCodeFroMconStruction=:ECodeFroMconstruction</#if>"
    	+ " <#if billTimeStamp??> and billTimeStamp >=:billTimeStamp</#if>"
    	+ " <#if endBillTimeStamp??> and billTimeStamp <=:endBillTimeStamp</#if>"
    	+ " order by companyName asc, projectName asc, eCodeFroMconStruction asc, bankName asc";
    }
	
	/**
	 * 托管楼幢收支明细统计表
	 * 
	 * @param comapanyId   开发企业Id
	 * @param projectId    项目Id
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBuildCount_View(String comapanyId, String projectId,String buildingId, String keyWord,
			String beginTime, String endTime, Integer pageNumber, Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_BuildCount_View(?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, comapanyId);
			sp.setString(2, projectId);
			sp.setString(3, buildingId);
			sp.setString(4, keyWord);
			sp.setString(5, beginTime);
			sp.setString(6, endTime);
			sp.setInt(7, pageNumber);
			sp.setInt(8, countPerPage);

			// 游标类型
			sp.registerOutParameter(9, OracleTypes.CURSOR);
			sp.registerOutParameter(10, OracleTypes.INTEGER);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			
			rs = (ResultSet) sp.getObject(9);

			List<Tg_BuildCount_View> tg_BuildCount_ViewList = new ArrayList<Tg_BuildCount_View>();
			Tg_BuildCount_View tg_BuildCount_View = null;

			while (rs.next()) {
				tg_BuildCount_View=new Tg_BuildCount_View();				
				
				//主键
				tg_BuildCount_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));	
				//记账日期 
				tg_BuildCount_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				//开发企业 
				tg_BuildCount_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				//项目名称  
				tg_BuildCount_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				//楼幢  
				//tg_BuildCount_View.seteCodeFroMconstruction(ObjectUtils.toString(rs.getObject(5)));
				//银行名称
				tg_BuildCount_View.setBankName(ObjectUtils.toString(rs.getObject(6)));
				//托管收入
				tg_BuildCount_View.setIncome(Double.valueOf(ObjectUtils.toString(rs.getObject(7))));
				//托管支出
				tg_BuildCount_View.setPayout(Double.valueOf(ObjectUtils.toString(rs.getObject(8))));
				//余额
				tg_BuildCount_View.setBalance(Double.valueOf(ObjectUtils.toString(rs.getObject(9))));			
				
				//商贷（托管收入）
				tg_BuildCount_View.setCommercialLoan(Double.valueOf(ObjectUtils.toString(rs.getObject(10))));
				//公积金（托管收入）
				tg_BuildCount_View.setAccumulationFund(Double.valueOf(ObjectUtils.toString(rs.getObject(11))));			
				
							
				tg_BuildCount_ViewList.add(tg_BuildCount_View);
			}
		
			map.put("tg_BuildCount_ViewList", tg_BuildCount_ViewList);
			

			int totalPage = sp.getInt(10);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(11);

			map.put("totalCount", totalCount);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
			/*if(sessionFactory!=null) {
				sessionFactory.getCurrentSession().flush();
			}*/
		}
		return map;
	}
}
