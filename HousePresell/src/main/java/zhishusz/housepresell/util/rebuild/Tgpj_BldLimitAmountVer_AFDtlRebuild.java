package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.extra.Tg_HouseStage;
import zhishusz.housepresell.database.po.state.S_HouseStage;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Rebuilder：受限额度设置
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpj_BldLimitAmountVer_AFDtlRebuild extends RebuilderBase<Tgpj_BldLimitAmountVer_AFDtl>
{
	@Override
	public Properties getSimpleInfo(Tgpj_BldLimitAmountVer_AFDtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpj_BldLimitAmountVer_AFDtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("bldLimitAmountVerMng", object.getBldLimitAmountVerMng());
		properties.put("bldLimitAmountVerMngId", object.getBldLimitAmountVerMng().getTableId());
		properties.put("stageName", object.getStageName());
		properties.put("limitedAmount", object.getLimitedAmount());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpj_BldLimitAmountVer_AFDtl> tgpj_BldLimitAmountVer_AFDtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpj_BldLimitAmountVer_AFDtlList != null)
		{
			for(Tgpj_BldLimitAmountVer_AFDtl object:tgpj_BldLimitAmountVer_AFDtlList)
			{
				String nodeTypeName = "";
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng = object.getBldLimitAmountVerMng();
//				properties.put("bldLimitAmountVerMng", );
				if(bldLimitAmountVerMng!=null){
					properties.put("bldLimitAmountVerMngId", bldLimitAmountVerMng.getTableId());

					if(bldLimitAmountVerMng.getTheType()!=null){
						String theType = bldLimitAmountVerMng.getTheType();
						if(theType.equals("0")){//毛坯房
							HashMap<Integer, Tg_HouseStage> buildingStage = S_HouseStage.BUILDING_STAGE;
							nodeTypeName = getNodeTypeName(object, nodeTypeName, buildingStage);
						}else{
							HashMap<Integer, Tg_HouseStage> completeStage = S_HouseStage.COMPLETE_STAGE;
							nodeTypeName = getNodeTypeName(object, nodeTypeName, completeStage);
						}

					}
				}
				properties.put("stageName", object.getStageName());
				properties.put("limitedAmount", object.getLimitedAmount());
				properties.put("remark", object.getRemark());
				properties.put("nodeTypeName",nodeTypeName);

				list.add(properties);
			}
		}
		return list;
	}

	private String getNodeTypeName(Tgpj_BldLimitAmountVer_AFDtl object, String nodeTypeName,
			HashMap<Integer, Tg_HouseStage> buildingStage)
	{
		for (Map.Entry<Integer, Tg_HouseStage> entry : buildingStage.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			Tg_HouseStage value = entry.getValue();
			if(value.getStageName().equals(object.getStageName())){
				nodeTypeName=entry.getKey()+"";
				break;
			}
		}
		return nodeTypeName;
	}
}
