package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Tb_b_company;
import zhishusz.housepresell.util.JDBCConnectionUtil;

/**
 * 开发企业
 * 
 * @ClassName: Tb_b_companyDao
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午8:02:25
 * @version V1.0
 *
 */
@Repository
public class Tb_b_companyDao
{
	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	/**
	 * 根据开发企业名称和统一社会信用代码查询开发企业列表
	 * @param companyname
	 * @param socialcreditcode
	 * @return
	 */
	public List<Tb_b_company> getCompanyList(String companyname, String socialcreditcode)
	{

		List<Tb_b_company> list = new ArrayList<Tb_b_company>();

//		String sql = "select * from tb_b_company tc where tc.companyname=? and tc.socialcreditcode=? and tc.is_formal=? ";
		/*
		 * xsz by time 2018-11-23 13:57:00
		 * #bug1313 开发企业导入界面改成模糊查询，开发企业名称和社会信用代码都可以模糊查询
		 */
		String sql = "select * from tb_b_company tc where tc.companyname like ? and tc.socialcreditcode like ? and tc.is_formal=? ";
		
		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, companyname);
			ps.setString(2, socialcreditcode);
			ps.setString(3, "1");

			ResultSet rs = ps.executeQuery();
			Tb_b_company vo;
			while (rs.next())
			{
				vo = new Tb_b_company();

				vo.setCOMPANYADDRESS(rs.getString("COMPANYADDRESS"));
				vo.setCOMPANYID(rs.getString("COMPANYID"));
				vo.setCOMPANYLEVEL(rs.getString("COMPANYLEVEL"));
				vo.setCOMPANYNAME(rs.getString("COMPANYNAME"));
				vo.setCOMPANYVERSION(rs.getString("COMPANYVERSION"));
				vo.setCONTACT(rs.getString("CONTACT"));
				vo.setCONTACTPHONE(rs.getString("CONTACTPHONE"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setCREATEUSER(rs.getLong("CREATEUSER"));
				vo.setFILEGUID(rs.getString("FILEGUID"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setLEGALPERSON(rs.getString("LEGALPERSON"));
				vo.setLEVELFILEGUID(rs.getString("LEVELFILEGUID"));
				vo.setREMARK(rs.getString("REMARK"));
				vo.setSOCIALCREDITCODE(rs.getString("SOCIALCREDITCODE"));
				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setSOCIALCREDITFILE(rs.getString("SOCIALCREDITFILE"));
				vo.setSTATUS(rs.getString("STATUS"));
				vo.setUPDATETIME(rs.getString("UPDATETIME"));
				vo.setUPDATEUSER(rs.getLong("UPDATEUSER"));

				list.add(vo);

			}

		}
		catch (SQLException e)
		{
			dao.closeConn(conn);
		}
		finally
		{
			dao.closeConn(conn);
		}

		return list;

	}
	
	/**
	 * 根据开发企业rowguid查询开发企业详情
	 * @param rowguid
	 * @return
	 */
	public Tb_b_company getCompanyDetail(String rowguid)
	{

		Tb_b_company vo = new Tb_b_company();
		
		String sql = "select * from tb_b_company tc where tc.rowguid=?";

		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, rowguid);

			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				vo.setCOMPANYADDRESS(rs.getString("COMPANYADDRESS"));
				vo.setCOMPANYID(rs.getString("COMPANYID"));
				vo.setCOMPANYLEVEL(rs.getString("COMPANYLEVEL"));
				vo.setCOMPANYNAME(rs.getString("COMPANYNAME"));
				vo.setCOMPANYVERSION(rs.getString("COMPANYVERSION"));
				vo.setCONTACT(rs.getString("CONTACT"));
				vo.setCONTACTPHONE(rs.getString("CONTACTPHONE"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setCREATEUSER(rs.getLong("CREATEUSER"));
				vo.setFILEGUID(rs.getString("FILEGUID"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setLEGALPERSON(rs.getString("LEGALPERSON"));
				vo.setLEVELFILEGUID(rs.getString("LEVELFILEGUID"));
				vo.setREMARK(rs.getString("REMARK"));
				vo.setSOCIALCREDITCODE(rs.getString("SOCIALCREDITCODE"));
				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setSOCIALCREDITFILE(rs.getString("SOCIALCREDITFILE"));
				vo.setSTATUS(rs.getString("STATUS"));
				vo.setUPDATETIME(rs.getString("UPDATETIME"));
				vo.setUPDATEUSER(rs.getLong("UPDATEUSER"));

			}

		}
		catch (SQLException e)
		{
			dao.closeConn(conn);
		}
		finally
		{
			dao.closeConn(conn);
		}

		return vo;

	}

}
