package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_RemainRight_Bak;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_RemainRight_BakRebuild extends RebuilderBase<Tgpf_RemainRight_Bak>
{
	@Override
	public Properties getSimpleInfo(Tgpf_RemainRight_Bak object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_RemainRight_Bak object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("eCode", object.geteCode());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_RemainRight_Bak> tgpf_RemainRight_BakList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_RemainRight_BakList != null)
		{
			for(Tgpf_RemainRight_Bak object:tgpf_RemainRight_BakList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("eCode", object.geteCode());
				
				list.add(properties);
			}
		}
		return list;
	}
}
