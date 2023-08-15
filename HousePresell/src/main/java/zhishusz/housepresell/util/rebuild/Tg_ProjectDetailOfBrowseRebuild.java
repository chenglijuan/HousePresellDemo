package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_projectDetailOfBrowse_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuild : 托管项目详情一览表
 * */
@Service
public class Tg_ProjectDetailOfBrowseRebuild extends RebuilderBase<Tg_projectDetailOfBrowse_View>
{

	@Override
	public Properties getSimpleInfo(Tg_projectDetailOfBrowse_View object)
	{
		if(object == null) return null;
		
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		properties.put("projectName", object.getProjectName());
		
		properties.put("cityRegion", object.getCityRegion());
		
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		
		properties.put("forEcastArea", object.getForEcastArea());
		
		properties.put("escrowArea", object.getEscrowArea());
		
		properties.put("recordAveragePrice", object.getRecordAveragePrice());
		
		properties.put("houseTotal", object.getHouseTotal());
		
		properties.put("produceOfProject", object.getProduceOfProject());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_projectDetailOfBrowse_View object)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
