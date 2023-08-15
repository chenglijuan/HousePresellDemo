package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Tb_b_building;
import zhishusz.housepresell.database.po.extra.Tb_b_buildingVO;
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
public class Tb_b_buildingDao
{
	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	/**
	 * 根据项目guid查询楼幢列表
	 * 
	 * @param projectid
	 * @return
	 */
	public List<Tb_b_buildingVO> getBuildingListByProjectId(String projectid)
	{

		List<Tb_b_buildingVO> list = new ArrayList<Tb_b_buildingVO>();

		// String sql = "select * from tb_b_building tb where tb.projectid=?";
		// String sql = "select rowguid externalId, pk_basinfo
		// eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid
		// eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno
		// eCodeFromPublicSecurity,unitnum unitNumber,area
		// buildingArea,constructaddress position,upfloor
		// upfloorNumber,downfloor downfloorNumber,enddate endDate,unitinfo
		// unitInfo from v_pm_building where pk_basinfo=? ";

		/*
		 * xsz by time 2018-11-10 18:25:35
		 * 关联字段用 buildingid
		 */
		String sql = "select certificatenumber ,buildingid externalId, pk_basinfo eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno eCodeFromPublicSecurity,unitnum unitNumber,area buildingArea,constructaddress position,upfloor upfloorNumber,downfloor downfloorNumber,enddate endDate,unitinfo unitInfo from v_pm_building where pk_basinfo=? ";
		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, projectid);

			ResultSet rs = ps.executeQuery();
			Tb_b_buildingVO vo;
			while (rs.next())
			{
				vo = new Tb_b_buildingVO();

				vo.setBuildingArea(rs.getString("BuildingArea"));
				// vo.setDecorationType(rs.getString("DecorationType"));
				vo.setDownfloorNumber(rs.getString("DownfloorNumber"));
				vo.setECodeFromConstruction(rs.getString("ECodeFromConstruction"));
				vo.setECodeFromPresellSystem(rs.getString("ECodeFromPresellSystem"));
				vo.setECodeFromPublicSecurity(rs.getString("ECodeFromPublicSecurity"));
				vo.setECodeOfPjFromPS(rs.getString("ECodeOfPjFromPS"));
				vo.setEndDate(rs.getString("EndDate"));
				vo.setExternalId(rs.getString("ExternalId"));
				vo.setPosition(rs.getString("Position"));
				vo.setTheNameFromPresellSystem(rs.getString("theNameFromPresellSystem"));
				vo.setUnitNumber(rs.getString("UnitNumber"));
				vo.setUpfloorNumber(rs.getString("UpfloorNumber"));
				vo.setUnitInfo(rs.getString("unitInfo"));
				vo.setCertificatenumber(rs.getString("certificatenumber"));

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
	 * 根据项目guid查询楼幢列表
	 * 
	 * @param projectid
	 * @return
	 */
	public List<Tb_b_buildingVO> getBuildingListByProjectId(String projectid, String buildingid)
	{

		List<Tb_b_buildingVO> list = new ArrayList<Tb_b_buildingVO>();

		// String sql = "select tb.*,tp.projectname from tb_b_building
		// tb,tb_b_project tp where tb.projectid=? and tb.is_formal= ? and
		// tb.state= ? and tp.projectid=tb.projectid";

		// String sql = "select rowguid externalId, pk_basinfo
		// eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid
		// eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno
		// eCodeFromPublicSecurity,unitnum unitNumber,area
		// buildingArea,constructaddress position,upfloor
		// upfloorNumber,downfloor downfloorNumber,enddate endDate,unitinfo
		// unitInfo from v_pm_building where pk_basinfo=? and constructno=? ";

		if (null != buildingid)
		{
			buildingid = "%"+buildingid+"%";
		}
		/*
		 * xsz by time 2018-11-10 18:25:35
		 * 关联字段用 buildingid
		 */
//		String sql = "select buildingid externalId, pk_basinfo eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno eCodeFromPublicSecurity,unitnum unitNumber,area buildingArea,constructaddress position,upfloor upfloorNumber,downfloor downfloorNumber,enddate endDate,unitinfo unitInfo from v_pm_building where pk_basinfo=? and constructno=? ";
		String sql = "select certificatenumber ,buildingid externalId, pk_basinfo eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno eCodeFromPublicSecurity,unitnum unitNumber,area buildingArea,constructaddress position,upfloor upfloorNumber,downfloor downfloorNumber,enddate endDate,unitinfo unitInfo from v_pm_building where pk_basinfo=? and constructno like ? ";
		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, projectid);
			ps.setString(2, buildingid);

			ResultSet rs = ps.executeQuery();
			Tb_b_buildingVO vo;
			while (rs.next())
			{
				vo = new Tb_b_buildingVO();

				vo.setBuildingArea(rs.getString("BuildingArea"));
				// vo.setDecorationType(rs.getString("DecorationType"));
				vo.setDownfloorNumber(rs.getString("DownfloorNumber"));
				vo.setECodeFromConstruction(rs.getString("ECodeFromConstruction"));
				vo.setECodeFromPresellSystem(rs.getString("ECodeFromPresellSystem"));
				vo.setECodeFromPublicSecurity(rs.getString("ECodeFromPublicSecurity"));
				vo.setECodeOfPjFromPS(rs.getString("ECodeOfPjFromPS"));
				vo.setEndDate(rs.getString("EndDate"));
				vo.setExternalId(rs.getString("ExternalId"));
				vo.setPosition(rs.getString("Position"));
				vo.setTheNameFromPresellSystem(rs.getString("theNameFromPresellSystem"));
				vo.setUnitNumber(rs.getString("UnitNumber"));
				vo.setUpfloorNumber(rs.getString("UpfloorNumber"));
				vo.setUnitInfo(rs.getString("unitInfo"));
				vo.setCertificatenumber(rs.getString("certificatenumber"));

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
	 * 根据guid查询楼幢详情
	 * 
	 * @param rowguid
	 * @return
	 */
	public Tb_b_building getBuildingDetail(String rowguid)
	{

		Tb_b_building vo = new Tb_b_building();

		String sql = "select * from tb_b_building tb where tb.rowguid=? ";

		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, rowguid);

			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setBUILDINGID(rs.getString("BUILDINGID"));
				vo.setBUILDINGVERSION(rs.getString("BUILDINGVERSION"));
				vo.setPROJECTID(rs.getString("PROJECTID"));
				vo.setPROJECTVERSION(rs.getString("PROJECTVERSION"));
				vo.setBUILDINGNO(rs.getString("BUILDINGNO"));
				vo.setAREA_TOTAL(rs.getString("AREA_TOTAL"));
				vo.setAREA_IN(rs.getString("AREA_IN"));
				vo.setAREA_SHARE(rs.getString("AREA_SHARE"));
				vo.setFLOORS_UP(rs.getString("FLOORS_UP"));
				vo.setFLOORS_DOWN(rs.getString("FLOORS_DOWN"));
				vo.setUNITCOUNT(rs.getString("UNITCOUNT"));
				vo.setSURVEYTYPE(rs.getString("SURVEYTYPE"));
				vo.setSOURCETYPE(rs.getString("SOURCETYPE"));
				vo.setUNITINFO(rs.getString("UNITINFO"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setSTATE(rs.getString("STATE"));
				vo.setIS_DELETE(rs.getString("IS_DELETE"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setEDITING(rs.getString("EDITING"));

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

	/**
	 * 预售系统项目主键 projectid
	 * 预售系统楼幢编号 buildingid
	 * 
	 * @param projectid
	 * @return
	 */
	public List<Tb_b_buildingVO> getBuildingListBySign(String projectid, String buildingid)
	{

		List<Tb_b_buildingVO> list = new ArrayList<Tb_b_buildingVO>();

		// String sql = "select rowguid externalId, pk_basinfo
		// eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid
		// eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno
		// eCodeFromPublicSecurity,unitnum unitNumber,area
		// buildingArea,constructaddress position,upfloor
		// upfloorNumber,downfloor downfloorNumber,enddate endDate from
		// v_pm_building where pk_basinfo=? ";

		/*
		 * xsz by time 2018-11-10 18:25:35
		 * 关联字段用 buildingid
		 */
		String sql = "select buildingid externalId, pk_basinfo eCodeOfPjFromPS,proname theNameFromPresellSystem,buildingid eCodeFromPresellSystem,constructno eCodeFromConstruction,buildingno eCodeFromPublicSecurity,unitnum unitNumber,area buildingArea,constructaddress position,upfloor upfloorNumber,downfloor downfloorNumber,enddate endDate from v_pm_building where pk_basinfo=? ";
		if (null != buildingid && !buildingid.trim().isEmpty())
		{
			sql += "  and buildingid=? ";
		}

		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, projectid);
			ps.setString(2, buildingid);

			ResultSet rs = ps.executeQuery();
			Tb_b_buildingVO vo;
			while (rs.next())
			{
				vo = new Tb_b_buildingVO();

				vo.setExternalId(rs.getString("externalId"));
				vo.setECodeOfPjFromPS(rs.getString("eCodeOfPjFromPS"));
				vo.setTheNameFromPresellSystem(rs.getString("theNameFromPresellSystem"));
				vo.setECodeFromPresellSystem(rs.getString("eCodeFromPresellSystem"));
				vo.setECodeFromConstruction(rs.getString("eCodeFromConstruction"));
				vo.setECodeFromPublicSecurity(rs.getString("eCodeFromPublicSecurity"));
				vo.setUnitNumber(rs.getString("unitNumber"));
				vo.setBuildingArea(rs.getString("buildingArea"));
				vo.setPosition(rs.getString("position"));
				vo.setUpfloorNumber(rs.getString("upfloorNumber"));
				vo.setDownfloorNumber(rs.getString("downfloorNumber"));
				vo.setEndDate("endDate");

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

}
