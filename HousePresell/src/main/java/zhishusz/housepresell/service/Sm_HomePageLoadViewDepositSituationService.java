package zhishusz.housepresell.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.po.Tg_DepositProjectAnalysis_HomeView;
import zhishusz.housepresell.database.po.state.S_NormalFlag;

/**
 * 首页展示加载-合作机构首页
 * 
 * @ClassName: Sm_HomePageLoadViewDepositSituationService
 * @Description:TODO
 * @author: yangyu
 * @date: 2019年1月10日
 * @version V1.0
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageLoadViewDepositSituationService {

	@Autowired
	private SessionFactory sessionFactory;

	public Properties execute(Tb_b_contractFrom model) {
		Properties properties = new Properties();

		// model.setCityRegionId(78L);
		String companyId = String.valueOf(model.getUser().getCompany().getTableId());
		
		Long regionId = model.getCityRegionId();

		if (null == regionId || regionId == 1L)
		{
			regionId = null;
		}

		/**
		 * 存储过程提取数据
		 */
		List<Tg_DepositProjectAnalysis_HomeView> tg_DepositProjectAnalysis_HomeViewList = new ArrayList<Tg_DepositProjectAnalysis_HomeView>();
//		System.out.println("過程開始"+System.currentTimeMillis());
		try {
			Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
			String sql = "{call GET_ProjectAnalysis_HomeView(?,?,?)}";
			CallableStatement sp = connection.prepareCall(sql);

			// 设置参数
			sp.setString(1, companyId);
			
			if (null == regionId)
			{
				sp.setString(2, "");
			}
			else
			{
				sp.setString(2, Long.toString(regionId));
			}

			// 游标类型
			sp.registerOutParameter(3, OracleTypes.CURSOR);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			ResultSet rs = null;
			rs = (ResultSet) sp.getObject(3);

			Tg_DepositProjectAnalysis_HomeView tg_DepositProjectAnalysis_HomeView = null;

			while (rs.next()) {
				tg_DepositProjectAnalysis_HomeView = new Tg_DepositProjectAnalysis_HomeView();

				// 年月
				tg_DepositProjectAnalysis_HomeView.setQueryTime(ObjectUtils.toString(rs.getObject(1)));
				// 托管面积（㎡）
				tg_DepositProjectAnalysis_HomeView.setEscrowArea(ObjectUtils.toString(rs.getObject(2)).isEmpty() ? null
						: Double.valueOf(ObjectUtils.toString(rs.getObject(2))));
				// 预售面积（㎡）
				tg_DepositProjectAnalysis_HomeView
						.setPreEscrowArea(ObjectUtils.toString(rs.getObject(3)).isEmpty() ? null
								: Double.valueOf(ObjectUtils.toString(rs.getObject(3))));
				// 托管面积同比（%）
				tg_DepositProjectAnalysis_HomeView
						.setEscrowAreaBeforeYear(ObjectUtils.toString(rs.getObject(4)).isEmpty() ? null
								: Double.valueOf(ObjectUtils.toString(rs.getObject(4))));
				// 托管面积环比（%）
				tg_DepositProjectAnalysis_HomeView
						.setEscrowAreaBeforeMonth(ObjectUtils.toString(rs.getObject(5)).isEmpty() ? null
								: Double.valueOf(ObjectUtils.toString(rs.getObject(5))));
				// 托管面积同比（%）
				tg_DepositProjectAnalysis_HomeView
						.setPreEscrowAreaBeforeYear(ObjectUtils.toString(rs.getObject(6)).isEmpty() ? null
								: Double.valueOf(ObjectUtils.toString(rs.getObject(6))));
				// 托管面积环比（%）
				tg_DepositProjectAnalysis_HomeView
						.setPreEscrowAreaBeforeMonth(ObjectUtils.toString(rs.getObject(7)).isEmpty() ? null
								: Double.valueOf(ObjectUtils.toString(rs.getObject(7))));

				tg_DepositProjectAnalysis_HomeViewList.add(tg_DepositProjectAnalysis_HomeView);
			}

			if (null != rs) {
				rs.close();
			}
			sp.close();
			connection.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		}
//		System.out.println("過程結束"+System.currentTimeMillis());
		
		properties.put("depositProjectAnalysisMapList", tg_DepositProjectAnalysis_HomeViewList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
