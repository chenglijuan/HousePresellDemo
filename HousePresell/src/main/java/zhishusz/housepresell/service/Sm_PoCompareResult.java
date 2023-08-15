package zhishusz.housepresell.service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import zhishusz.objectdiffer.differ.ProjectObjectDiffer;
import zhishusz.objectdiffer.model.ListCompareModel;
import zhishusz.objectdiffer.model.ObjectCompareModel;


/*
 * Service添加操作：编辑前后数据对比结果
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_PoCompareResult
{
    public Boolean execute(Object oldObj, Object newObj) 
    {
        try 
        {
            ProjectObjectDiffer objectDiffer = new ProjectObjectDiffer();
            objectDiffer.addFilterString("userUpdate");
            objectDiffer.addFilterString("userStart");
            objectDiffer.addFilterString("userRecord");
            objectDiffer.addFilterString("lastUpdateTimeStamp");
            objectDiffer.addFilterString("version");
            objectDiffer.addFilterString("approvalState");
            ArrayList<ObjectCompareModel> differ = objectDiffer.projectDiffer(oldObj,newObj);
            boolean twoObjSame = objectDiffer.isTwoObjSame(differ);
            if(!twoObjSame){
                return false;
            }
//            if(objectDiffer.calDiffentFieldAmount(differ) != 0)
//            {
//                return false;//不一样
//            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return true;
    }
    
    public Boolean execute(List list1, List list2) 
    {
    	Boolean flag = true;
    	
        try 
        {
        	OneList one = new OneList(list1);
        	OneList two = new OneList(list2);
            ProjectObjectDiffer objectDiffer = new ProjectObjectDiffer();
            objectDiffer.setTableUniqueString("tableId");
            objectDiffer.addFilterString("userUpdate");
            objectDiffer.addFilterString("userStart");
            objectDiffer.addFilterString("userRecord");
            objectDiffer.addFilterString("lastUpdateTimeStamp");
            objectDiffer.addFilterString("version");
            objectDiffer.addFilterString("approvalState");
            ArrayList<ObjectCompareModel> differ = objectDiffer.differ(one,two);
            
            for(ObjectCompareModel compare: differ){
            	if(compare instanceof ListCompareModel)
            	{
            		 ListCompareModel listCompareModel= (ListCompareModel) compare;
            		 Integer a = listCompareModel.getObjAUnique().size();
            		 Integer b = listCompareModel.getObjBUnique().size();
            		 if(a != 0 || b != 0)flag = false;//不一样
            	}
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return flag;
    }
    
    class OneList{
    	private List list;
    	
    	
		public OneList() {
			super();
		}
		
		public OneList(List list) {
			super();
			this.list = list;
		}

		public List getList() {
			return list;
		}

		public void setList(List list) {
			this.list = list;
		}
    }
}
