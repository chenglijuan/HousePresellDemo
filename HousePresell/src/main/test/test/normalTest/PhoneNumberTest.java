package test.normalTest;

import zhishusz.housepresell.util.MyString;

public class PhoneNumberTest {

	public static void main(String[] args) 
	{
		System.out.println(MyString.getInstance().checkPhoneNumber("0512-58560012"));
		System.out.println(MyString.getInstance().checkPhoneNumber("15162327456"));
	}
}
