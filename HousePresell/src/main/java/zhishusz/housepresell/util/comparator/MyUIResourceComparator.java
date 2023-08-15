package zhishusz.housepresell.util.comparator;

import java.util.Comparator;

import zhishusz.housepresell.database.po.Sm_Permission_UIResource;

//菜单排序
@SuppressWarnings("rawtypes")
public class MyUIResourceComparator implements Comparator
{
	//先按theIndex排序（小的排前面）
	//若theIndex一样，按照tableId顺序，小的排前面
	@Override
	public int compare(Object o1, Object o2)
	{
		if(!(o1 instanceof Sm_Permission_UIResource))
		{
			return 0;
		}
		if(!(o2 instanceof Sm_Permission_UIResource))
		{
			return 0;
		}
		
		Sm_Permission_UIResource uiResource1 = (Sm_Permission_UIResource) o1;
		Sm_Permission_UIResource uiResource2 = (Sm_Permission_UIResource) o2;
		Double theIndex1 = uiResource1.getTheIndex() == null ? 0.0 : uiResource1.getTheIndex();
		Double theIndex2 = uiResource2.getTheIndex() == null ? 0.0 : uiResource2.getTheIndex();
		
		if(theIndex2 < theIndex1)
        {
        	return 1;//排前面
        }
        else if(theIndex2 - theIndex1 < 0.0000001)
        {
        	Long tableId2 = uiResource2.getTableId();
        	if(tableId2 == null) tableId2 = 0L;
        	Long tableId1 = uiResource1.getTableId();
        	if(tableId1 == null) tableId1 = 0L; 
        	
        	if(tableId2 < tableId1)
        	{
        		return 1;
        	}
        	else if(tableId2 > tableId1)
        	{
        		return -1;
        	}
        	
    		return 0;//不变
        }
        else
        {
        	return -1;//排后面
        }
	}
}
