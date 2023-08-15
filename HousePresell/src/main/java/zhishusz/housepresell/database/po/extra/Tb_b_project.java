package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * 中间库-项目取数VO
 * 
 * @ClassName: Tb_b_project
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午10:03:16
 * @version V1.0
 *
 */
public class Tb_b_project
{

	@Getter
	@Setter
	private String ROWGUID;// RowGuid
	@Getter
	@Setter
	private String PROJECTVERSION;// 项目版本
	@Getter
	@Setter
	private String PROJECTID;// 项目Guid
	@Getter
	@Setter
	private String PROJECTNAME;// 项目名称
	@Getter
	@Setter
	private String PROJECTNAME_FORMER;// 项目曾用名
	@Getter
	@Setter
	private String PROJECTADDRESS;// 项目地址
	@Getter
	@Setter
	private String PROJECTCONTACT;// 项目联系人
	@Getter
	@Setter
	private String CONTACTPHONE;// 联系电话
	@Getter
	@Setter
	private String REGIONCODE;// 【选项】所在行政区域（文字）
	@Getter
	@Setter
	private String STREETCODE;// 【选项】所在街道（文字）
	@Getter
	@Setter
	private String PLATECODE;// 【选项】板块
	@Getter
	@Setter
	private String BRIEFINFO;// 项目备注
	@Getter
	@Setter
	private String LOCATION_BAIDU;// 项目地图坐标范围(百度)
	@Getter
	@Setter
	private String LOCATION_GIS;// 项目地图坐标范围(GIS)
	@Getter
	@Setter
	private String CREATETIME;// 生成时间
	@Getter
	@Setter
	private String IS_DELETE;// 是否删除
	@Getter
	@Setter
	private String STATUS;// 0 未上报，1未审核，2审核通过 无用、待删
	@Getter
	@Setter
	private String IS_FORMAL;// 是否正式
	@Getter
	@Setter
	private String DIETIME;// 消亡时间
	@Getter
	@Setter
	private String OTHER_FILEGUID;// 其它附件
	@Getter
	@Setter
	private String CREATEUSER;// 操作人id
	@Getter
	@Setter
	private String DMPF;// 地名批复
	@Getter
	@Setter
	private String TDXXPROJECTADDRESS;// 土地信息项目位置
	@Getter
	@Setter
	private String TDHTBH;// 土地合同编号
	@Getter
	@Setter
	private String TDHTZDMJ;// 土地合同占地面积
	@Getter
	@Setter
	private String TDYT;// 土地用途
	@Getter
	@Setter
	private String CRZJE;// 出让总金额
	@Getter
	@Setter
	private String JSYDGHXKZBH;// 建设用地规划许可证编号
	@Getter
	@Setter
	private String JSYDGHXKZPROJECTNAME;// 建设用地规划许可证项目名称
	@Getter
	@Setter
	private String YDWZ;// 用地位置
	@Getter
	@Setter
	private String YDXZ;// 用地性质
	@Getter
	@Setter
	private String YDMJ;// 用地面积
	@Getter
	@Setter
	private String JSGCGHXKZBH;// 建设工程规划许可证编号
	@Getter
	@Setter
	private String JSGCGHXKZPROJECTNAME;// 建设工程规划许可证项目名称
	@Getter
	@Setter
	private String JSWZ;// 建设位置
	@Getter
	@Setter
	private String DSJZMJ;// 地上建筑面积
	@Getter
	@Setter
	private String DXJZMJ;// 地下建筑面积
	@Getter
	@Setter
	private String ZJSGM;// 总建设规模
	@Getter
	@Setter
	private String ZZJZMJ;// 住宅建筑面积
	@Getter
	@Setter
	private String JZGCSGXKZBH;// 建筑工程施工许可证编号
	@Getter
	@Setter
	private String JSGM;// 建设规模
	@Getter
	@Setter
	private String KGRQ;// 开工日期
	@Getter
	@Setter
	private String BABH;// 备案编号
	@Getter
	@Setter
	private String ZZBAMJ;// 住宅备案面积
	@Getter
	@Setter
	private String ZZBAJJ;// 住宅备案均价
	@Getter
	@Setter
	private String ZSBH;// 国有土地使用证编号
	@Getter
	@Setter
	private String ZL;// 坐落
	@Getter
	@Setter
	private String TDSYQR;// 土地使用权人
	@Getter
	@Setter
	private String SYQMJ;// 使用权面积
	@Getter
	@Setter
	private String DL;// 地类
	@Getter
	@Setter
	private String DYQK;// 抵押情况
	@Getter
	@Setter
	private String CFQK;// 查封情况
	@Getter
	@Setter
	private String BDCDJZBH;// 不动产登记证编号
	@Getter
	@Setter
	private String QLR;// 权利人
	@Getter
	@Setter
	private String BDCDJZZL;// 坐落
	@Getter
	@Setter
	private String XMHZPWBH;// 项目核准批文编号
	@Getter
	@Setter
	private String DKMC;// 地块名称
	@Getter
	@Setter
	private String XMZTZE;// 项目总投资额
	@Getter
	@Setter
	private String XMHZPWYDMJ;// 用地面积
	@Getter
	@Setter
	private String DSZJZM;// 地上总建筑面积
	@Getter
	@Setter
	private String ZZMJ;// 住宅面积
	@Getter
	@Setter
	private String COMPANYID;// 开发企业id
	@Getter
	@Setter
	private String TDSYQZZRQ;// 土地使用权终止日期
	@Getter
	@Setter
	private String ZZZTS;// 住宅总套数

}