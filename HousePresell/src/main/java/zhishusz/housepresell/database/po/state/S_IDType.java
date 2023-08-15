package zhishusz.housepresell.database.po.state;

//证件类型
public class S_IDType
{
	public static final String ResidentIdentityCard = "1";//身份证
	public static final String Passport = "2";//护照
	public static final String HongKongMacaoTaiwan_IdentityCard = "3";//军官证
	public static final String TravelVisa = "4";//港澳台居民通行证
	public static final String ResidenceBooklet = "5";//户口簿
	public static final String Others = "6";//其他证件
	
	public static final String[] idTypeArr = {"身份证", "护照", "军官证", "港澳台居民通行证", "户口簿", "其他证件"};
}
