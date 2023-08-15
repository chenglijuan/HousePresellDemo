package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.extra.Tb_b_building;
import zhishusz.housepresell.database.po.extra.Tb_b_room;
import zhishusz.housepresell.util.JDBCConnectionUtil;

/**
 * 中间库-户室取数
 * 
 * @ClassName: Tb_b_roomDao
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月26日 上午10:08:23
 * @version V1.0
 *
 */
@Repository
public class Tb_b_roomDao
{
	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	/**
	 * 根据楼幢guid加载户室列表
	 * 
	 * @param buildingid
	 * @return
	 */
	public List<Tb_b_room> getRoomListByBuildingId(String buildingid)
	{

		List<Tb_b_room> list = new ArrayList<Tb_b_room>();

		String sql = "select * from tb_b_room tr where tr.buildingid=?";

		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, buildingid);

			ResultSet rs = ps.executeQuery();
			Tb_b_room vo;
			while (rs.next())
			{
				vo = new Tb_b_room();

//				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setROWGUID(rs.getString("ROOMID"));
				vo.setROOMID(rs.getString("ROOMID"));
				vo.setROOMVERSION(rs.getString("ROOMVERSION"));
				vo.setBUILDINGID(rs.getString("BUILDINGID"));
				vo.setBUILDINGVERSION(rs.getString("BUILDINGVERSION"));
				vo.setROOMNO(rs.getString("ROOMNO"));
				vo.setROOMLOCATION(rs.getString("ROOMLOCATION"));
				vo.setUNITCODE(rs.getString("UNITCODE"));
				vo.setFLOORNUMBER(rs.getString("FLOORNUMBER"));
				vo.setFLOORNAME(rs.getString("FLOORNAME"));
				vo.setFLOORSPAN(rs.getString("FLOORSPAN"));
				vo.setROOMUSAGE(rs.getString("ROOMUSAGE"));
				vo.setPUBLICFACILITYTYPE(rs.getString("PUBLICFACILITYTYPE"));
				vo.setAREA_BUILDING(rs.getString("AREA_BUILDING"));
				vo.setAREA_SHARE(rs.getString("AREA_SHARE"));
				vo.setAREA_IN(rs.getString("AREA_IN"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setDIETIME(rs.getString("DIETIME"));
				vo.setSOURCETYPE(rs.getString("SOURCETYPE"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setIS_DELETE(rs.getString("IS_DELETE"));
				vo.setSTATE(rs.getString("STATE"));
				vo.setLIMIT_STATE(rs.getString("LIMIT_STATE"));
				vo.setSEQUESTRATION_STATE(rs.getString("SEQUESTRATION_STATE"));
				vo.setCLOSE_STATE(rs.getString("CLOSE_STATE"));
				vo.setEDITING(rs.getString("EDITING"));
				vo.setIS_LOCK(rs.getString("IS_LOCK"));
				vo.setSURVEYTYPE(rs.getString("SURVEYTYPE"));
				vo.setSURVEYROOMID(rs.getString("SURVEYROOMID"));

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
	 * 根据guid查询户室详情
	 * 
	 * @param rowguid
	 * @return
	 */
	public Tb_b_room getRoomDetail(String rowguid)
	{

		Tb_b_room vo = new Tb_b_room();

		String sql = "select * from tb_b_room tr where tr.roomid=?";

		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, rowguid);

			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
//				vo.setROWGUID(rs.getString("ROWGUID"));
				vo.setROWGUID(rs.getString("ROOMID"));
				vo.setROOMID(rs.getString("ROOMID"));
				vo.setROOMVERSION(rs.getString("ROOMVERSION"));
				vo.setBUILDINGID(rs.getString("BUILDINGID"));
				vo.setBUILDINGVERSION(rs.getString("BUILDINGVERSION"));
				vo.setROOMNO(rs.getString("ROOMNO"));
				vo.setROOMLOCATION(rs.getString("ROOMLOCATION"));
				vo.setUNITCODE(rs.getString("UNITCODE"));
				vo.setFLOORNUMBER(rs.getString("FLOORNUMBER"));
				vo.setFLOORNAME(rs.getString("FLOORNAME"));
				vo.setFLOORSPAN(rs.getString("FLOORSPAN"));
				vo.setROOMUSAGE(rs.getString("ROOMUSAGE"));
				vo.setPUBLICFACILITYTYPE(rs.getString("PUBLICFACILITYTYPE"));
				vo.setAREA_BUILDING(rs.getString("AREA_BUILDING"));
				vo.setAREA_SHARE(rs.getString("AREA_SHARE"));
				vo.setAREA_IN(rs.getString("AREA_IN"));
				vo.setCREATETIME(rs.getString("CREATETIME"));
				vo.setDIETIME(rs.getString("DIETIME"));
				vo.setSOURCETYPE(rs.getString("SOURCETYPE"));
				vo.setIS_FORMAL(rs.getString("IS_FORMAL"));
				vo.setIS_DELETE(rs.getString("IS_DELETE"));
				vo.setSTATE(rs.getString("STATE"));
				vo.setLIMIT_STATE(rs.getString("LIMIT_STATE"));
				vo.setSEQUESTRATION_STATE(rs.getString("SEQUESTRATION_STATE"));
				vo.setCLOSE_STATE(rs.getString("CLOSE_STATE"));
				vo.setEDITING(rs.getString("EDITING"));
				vo.setIS_LOCK(rs.getString("IS_LOCK"));
				vo.setSURVEYTYPE(rs.getString("SURVEYTYPE"));
				vo.setSURVEYROOMID(rs.getString("SURVEYROOMID"));

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
	 * 根据楼幢guid加载有效的户室列表
	 * @param buildingId
	 * @return
	 * 
	 */
	public List<Empj_HouseInfo> getHouseList(String buildingId)
	{
		List<Empj_HouseInfo> list = new ArrayList<Empj_HouseInfo>();
		
		String sql = "select * from tb_b_room tr where tr.is_formal=1 and tr.is_delete=0 and tr.buildingid=? ";
		
		Connection conn = dao.getConn();

		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, buildingId);

			ResultSet rs = ps.executeQuery();

			Empj_HouseInfo po;
			while (rs.next())
			{
				
				po = new Empj_HouseInfo();
				
//				po.setExternalId(rs.getString("ROWGUID"));
				po.setExternalId(rs.getString("ROOMID"));
				po.seteCodeFromPresellSystem(rs.getString("ROOMID"));
				po.setRoomId(rs.getString("ROOMNO"));
				po.setPosition(rs.getString("ROOMLOCATION"));
				po.setFloor(rs.getDouble("FLOORNUMBER"));
				po.setOverFloors(rs.getInt("FLOORSPAN"));
				po.setActualArea(rs.getDouble("AREA_BUILDING"));
				po.setForecastArea(rs.getDouble("AREA_BUILDING"));
				po.setShareConsArea(rs.getDouble("AREA_SHARE"));
				po.setInnerconsArea(rs.getDouble("AREA_IN"));
				po.seteCodeOfMapping(rs.getString("SURVEYROOMID"));
				po.setRemark(rs.getString("UNITCODE"));

				list.add(po);

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
