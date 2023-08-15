package test.normalTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyInteger;

public class RandomNumberTest {

	public static void main(String[] args) 
	{
		MyDouble myDouble = MyDouble.getInstance();
		List<String> result = new ArrayList<String>();
		List<String> strList = new ArrayList<String>();
		strList.add("a");
		strList.add("b");
		strList.add("c");
		strList.add("d");
		strList.add("e");
		strList.add("f");
		strList.add("g");
		
		Double proportion = myDouble.div(30.0, 100.0, 1);
		Integer length = (int) (proportion*(strList.size()))+1;
		int Max=strList.size()-1,Min=0;
		for(int i=0;i<length;i++)
		{
			getRandomNum(Min, Max, result, strList);
		}
		
		System.out.println(result);
		
		Integer randomcount = (int) ((0.1*10)+1);
		
		System.out.println(randomcount);
	}
	
	public static <T> Integer getRandomNum(Integer min, Integer max, List<T> result, List<T> strList)
	{
		Integer randNum = (int)Math.round(Math.random()*(max-min)+min);
		
		if(result.contains(strList.get(randNum)))
		{
			getRandomNum(min, max, result, strList);
			return null;
		}
		else
		{
			result.add(strList.get(randNum));
			return randNum;
		}
	}
}
