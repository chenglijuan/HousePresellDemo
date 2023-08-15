package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_DepositProjectAnalysis_View;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：托管项目情况分析表
 * Company：ZhiShuSZ
 * */
@Service
public class Tg_DepositProjectAnalysis_ViewRebuild extends RebuilderBase<Tg_DepositProjectAnalysis_View>
{

	@Override
	public Properties getSimpleInfo(Tg_DepositProjectAnalysis_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("cityRegion", object.getCityRegion());
		properties.put("busiKind", object.getBusiKind());
		properties.put("escrowArea", object.getEscrowArea());
		properties.put("escrowAreaRatio", object.getEscrowAreaRatio());
		properties.put("preEscrowArea", object.getPreEscrowArea());
		properties.put("preEscrowAreaRatio", object.getPreEscrowAreaRatio());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tg_DepositProjectAnalysis_View object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

}
