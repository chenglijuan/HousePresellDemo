package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * 中间库开发企业
 * 
 * @ClassName: Tb_b_company
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午6:38:40
 * @version V1.0
 *
 */
public class Tb_b_company
{
	@Getter
	@Setter
	private String ROWGUID; // 企业Guid
	@Getter
	@Setter
	private String COMPANYVERSION; // 企业版本
	@Getter
	@Setter
	private String COMPANYNAME; // 企业名称
	@Getter
	@Setter
	private String LEGALPERSON; // 法定代表人
	@Getter
	@Setter
	private String CONTACT; // 联系人
	@Getter
	@Setter
	private String CONTACTPHONE; // 联系电话
	@Getter
	@Setter
	private String COMPANYADDRESS;// 单位地址
	@Getter
	@Setter
	private String CREATETIME;// 生成时间
	@Getter
	@Setter
	private String UPDATETIME;// 更新时间
	@Getter
	@Setter
	private Long UPDATEUSER;// 修改人id
	@Getter
	@Setter
	private String STATUS;// 状态(字典622，1启用，2冻结，5删除)
	@Getter
	@Setter
	private String SOCIALCREDITFILE;// 统一社会信用证附件ID
	@Getter
	@Setter
	private String LEVELFILEGUID;// 资质证书附件ID
	@Getter
	@Setter
	private String IS_FORMAL;// 是否正式（0否，1是）
	@Getter
	@Setter
	private String COMPANYLEVEL;// 资质等级（字典502）
	@Getter
	@Setter
	private String SOCIALCREDITCODE;// 统一社会信用代码
	@Getter
	@Setter
	private String FILEGUID;// 其它附件
	@Getter
	@Setter
	private String REMARK;// 备注
	@Getter
	@Setter
	private String COMPANYTYPE;// 企业类型（1开发企业；2测绘企业）
	@Getter
	@Setter
	private String COMPANYID;// 企业id（同一企业标识）
	@Getter
	@Setter
	private Long CREATEUSER;// 创建人
	
	

}