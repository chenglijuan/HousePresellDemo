package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class RebuilderBase<E>
{
	public List<Properties> execute(List<E> oldList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(oldList != null)
		{
			for(E element:oldList)
			{
				list.add(getSimpleInfo(element));
			}
		}
		return list;
	}
	
	public Properties execute(E object)
	{
		return getDetail(object);
	}
	public abstract Properties getSimpleInfo(E object);
	public abstract Properties getDetail(E object);
	public List<Properties> executeForSelectList(List<E> oldList){
		List<Properties> list = new ArrayList<Properties>();
		if(oldList != null)
		{
			for(E element:oldList)
			{
				list.add(getSimpleInfo(element));
			}
		}
		return list;
	}
}
