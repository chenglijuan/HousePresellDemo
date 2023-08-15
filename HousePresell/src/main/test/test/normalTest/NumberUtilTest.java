package test.normalTest;

import java.math.BigDecimal;

import zhishusz.housepresell.util.MyDouble;

import cn.hutool.core.util.NumberUtil;

public class NumberUtilTest 
{

	public static void main(String[] args) 
	{
//		Double theRatio = 0.123456789123456;
//		BigDecimal bigDecimal = NumberUtil.round(theRatio, 11);
		
		Double theRatio = MyDouble.getInstance().div(4, 100.0, 2);
		
		System.out.println(theRatio);
	}

}
