package test.normalTest;

import java.util.Properties;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exception.RoolBackException;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;

public class ExceptionTest {
	public static void main(String[] args) 
	{
		MyString myString = MyString.getInstance();
		Properties properties = new MyProperties();
		
		try 
		{
		    // 你的代码
			throw new RoolBackException(myString.parse(properties.get(S_NormalFlag.info)));
		} 
		catch (Exception e) {
//		    // 这个异常打印
			e.printStackTrace();
		}
		
	}
}
