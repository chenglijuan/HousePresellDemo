package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Tb_b_project;
import zhishusz.housepresell.util.JDBCConnectionUtil;

/**
 * 中间库-项目取数
 * 
 * @ClassName: Tb_b_projectDao
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午10:32:01
 * @version V1.0
 *
 */
@Repository
public class Tb_b_projectDao
{
	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	/**
	 * 根据开发企业id加载项目列表
	 * @param companyid
	 * @return
	 */
	public List<Tb_b_project> getProjectListByCompanyId(String companyid)
	{

		List<Tb_b_project> list = new ArrayList<Tb_b_project>();
		
		String sql = "select * from tb_b_project tp where tp.companyid=? and tp.is_formal=?";
		
		Connection conn = dao.getConn();
		
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, companyid);
			ps.setString(2, "1");
			
			ResultSet rs = ps.executeQuery();
			Tb_b_project vo;
			while (rs.next())
			{
				vo = new Tb_b_project();
				
				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setPROJECTVERSION(rs.getString("PROJECTVERSION"));
				vo.setPROJECTID(rs.getString("PROJECTID"));
				vo.setPROJECTNAME(rs.getString("PROJECTNAME"));
				vo.setPROJECTADDRESS(rs.getString("PROJECTADDRESS"));
				vo.setPROJECTCONTACT(rs.getString("PROJECTCONTACT"));
				vo.setCONTACTPHONE(rs.getString("CONTACTPHONE"));
				vo.setREGIONCODE(rs.getString("REGIONCODE"));
				vo.setSTREETCODE(rs.getString("STREETCODE"));
				vo.setBRIEFINFO(rs.getString("BRIEFINFO"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setIS_DELETE(rs.getString("IS_DELETE"));
				vo.setSTATUS(rs.getString("STATUS"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setCREATEUSER(rs.getString("CREATEUSER"));
				vo.setTDXXPROJECTADDRESS(rs.getString("TDXXPROJECTADDRESS"));
				vo.setTDHTBH(rs.getString("TDHTBH"));
				vo.setTDHTZDMJ(rs.getString("TDHTZDMJ"));
				vo.setTDYT(rs.getString("TDYT"));
				vo.setKGRQ(rs.getString("KGRQ"));
				vo.setCOMPANYID(rs.getString("COMPANYID"));

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
	 * 根据guid查询项目详情
	 * @param rowguid
	 * @return
	 */
	public Tb_b_project getProjectDetail(String rowguid){
		
		Tb_b_project vo = new Tb_b_project();
		
		String sql = "select * from tb_b_project tp where tp.rowguid=?";
		
		Connection conn = dao.getConn();
		
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, rowguid);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				
				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setPROJECTVERSION(rs.getString("PROJECTVERSION"));
				vo.setPROJECTID(rs.getString("PROJECTID"));
				vo.setPROJECTNAME(rs.getString("PROJECTNAME"));
				vo.setPROJECTADDRESS(rs.getString("PROJECTADDRESS"));
				vo.setPROJECTCONTACT(rs.getString("PROJECTCONTACT"));
				vo.setCONTACTPHONE(rs.getString("CONTACTPHONE"));
				vo.setREGIONCODE(rs.getString("REGIONCODE"));
				vo.setSTREETCODE(rs.getString("STREETCODE"));
				vo.setBRIEFINFO(rs.getString("BRIEFINFO"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setIS_DELETE(rs.getString("IS_DELETE"));
				vo.setSTATUS(rs.getString("STATUS"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setCREATEUSER(rs.getString("CREATEUSER"));
				vo.setTDXXPROJECTADDRESS(rs.getString("TDXXPROJECTADDRESS"));
				vo.setTDHTBH(rs.getString("TDHTBH"));
				vo.setTDHTZDMJ(rs.getString("TDHTZDMJ"));
				vo.setTDYT(rs.getString("TDYT"));
				vo.setKGRQ(rs.getString("KGRQ"));
				vo.setCOMPANYID(rs.getString("COMPANYID"));

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
