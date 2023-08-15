package zhishusz.housepresell.util;

public class Checker
{
	private static Checker instance;
	private Checker()
	{
		
	}
	
	public static Checker getInstance()
	{
		if(instance == null) instance = new Checker();
		
		return instance;
	}
	
	public Integer checkCountPerPage(Integer countPerPage)
	{
		if (countPerPage == null || countPerPage < 1)
		{
			return 10;
		}
		else
		{
			return countPerPage;
		}
	}

	public Integer checkCountPerPage(Object countPerPage)
	{
		return checkCountPerPage(MyInteger.getInstance().parse(countPerPage));
	}

	public Integer checkPageNumber(Integer pageNumber)
	{
		if (pageNumber == null || pageNumber < 0)
		{
			return 0;
		}
		else
		{
			return pageNumber;
		}
	}

	public Integer checkPageNumber(Object pageNumber)
	{
		return checkPageNumber(MyInteger.getInstance().parse(pageNumber));
	}
	
	public String checkKeyword(String keyword)
	{
		keyword = keyword == null ? null : keyword.trim();
		if(keyword != null && keyword.length() > 0)
		{
			keyword = "%" + keyword + "%";
		}
		else
		{
			keyword = null;
		}
		return keyword;
	}
	
	//校验字符串是否为空
	public boolean checkNullOrEmpty(String value)
	{
		return (null == value ||value.trim().isEmpty());
	}
}
