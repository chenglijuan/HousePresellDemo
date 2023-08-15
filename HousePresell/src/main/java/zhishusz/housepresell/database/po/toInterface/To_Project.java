package zhishusz.housepresell.database.po.toInterface;

import lombok.Getter;
import lombok.Setter;

public class To_Project
{
	@Getter @Setter 
	private String action;//方法 add,edit,del
	
	@Getter @Setter 
	private String cate;//业务类型 pj项目  bld楼栋  ts托管系统
	
	@Getter @Setter 
	private String pj_title;//楼盘名
	
	@Getter @Setter 
	private String ts_pj_id;// 托管系统项目Id
	
	@Getter @Setter 
	private String pj_area_name;//区域名称
	
	@Getter @Setter 
	private String ts_area_id;//托管系统区域iD
	
	@Getter @Setter 
	private String pj_kfs;//开发商
	
	@Getter @Setter
	private String pj_dz;//地址
	
	@Getter @Setter
	private String pj_pic;//鸟瞰图 URL
	
	@Getter @Setter
	private String pj_content;//项目介绍
	
	@Getter @Setter
	private String ts_jl_name;//监理公司
	
	@Getter @Setter
	private String ts_jl_id;//监理公司ID
	
	@Getter @Setter
	private String pj_longitude;//经度
	
	@Getter @Setter
	private String pj_latitude;//纬度
	
	@Getter @Setter
	private String pj_str1;//备用文本字段1
	
	@Getter @Setter
	private String pj_str2;//备用文本字段2
	
	@Getter @Setter
	private String pj_str3;//备用文本字段3
	
	@Getter @Setter
	private String pj_str4;//备用文本字段4
	
	@Getter @Setter
	private String pj_num1;//备用数字字段1
	
	@Getter @Setter
	private String pj_num2;//备用数字字段2
	
	@Getter @Setter
	private String pj_num3;//备用数字字段3
	
	@Getter @Setter
	private String pj_num4;//备用数字字段4
	
	@Getter @Setter
	private String pj_ysz;//预售证
}
