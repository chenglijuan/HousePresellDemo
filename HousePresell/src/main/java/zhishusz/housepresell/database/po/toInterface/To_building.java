package zhishusz.housepresell.database.po.toInterface;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class To_building
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
	private String bld_hname;//施工编号
	
	@Getter @Setter 
	private String bld_hmane1;//公安编号
	
	@Getter @Setter 
	private String ts_bld_id;//托管系统楼栋ID
	
	@Getter @Setter 
	private String bld_mj;//托管面积
	
	@Getter @Setter 
	private String bld_lc;//地上层数
	
	@Getter @Setter 
	private String bld_type;//房屋类型 0代表毛坯，1代表成品房 
	
	@Getter @Setter 
	private String bld_tgtime;//托管签约时间
	
	@Getter @Setter
	private String bld_str1;//备用文本字段1
	
	@Getter @Setter
	private String bld_str2;//备用文本字段2
	
	@Getter @Setter
	private String bld_str3;//备用文本字段3
	
	@Getter @Setter
	private String bld_str4;//备用文本字段4
	
	@Getter @Setter
	private String bld_num1;//备用数字字段1
	
	@Getter @Setter
	private String bld_num2;//备用数字字段2
	
	@Getter @Setter
	private String bld_num3;//备用数字字段3
	
	@Getter @Setter
	private String bld_num4;//备用数字字段4
	
	@Getter @Setter
	private String bld_endtime;//托管结束时间
	
	@Getter @Setter
	private String bld_jfbatime;//交付备案时间
}
