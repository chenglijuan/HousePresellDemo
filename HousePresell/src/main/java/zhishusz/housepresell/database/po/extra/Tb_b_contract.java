package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 中间库合同VO
 * @ClassName:  Tb_b_ontract   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月2日 下午7:47:18   
 * @version V1.0 
 *
 */
@Entity
@Table(name="tb_b_contract")
public class Tb_b_contract implements Serializable
{	
	private static final long serialVersionUID = -7588615885644224924L;
	
	@Id @Getter @Setter
	private String rowguid;          //主键  NVARCHAR2(50) not null,
	@Getter @Setter
	private String companyid;        // NVARCHAR2(50),企业id
	@Getter @Setter
	private String projectid;        // NVARCHAR2(50),项目id
	@Getter @Setter
	private String buildingid;       // NVARCHAR2(50),幢id
	@Getter @Setter
	private String roomguid;         // VARCHAR2(50),户rowguid
	@Getter @Setter
	private String roomid;           // NVARCHAR2(50),户id
	@Getter @Setter
	private String createuserid;     // NVARCHAR2(50),创建人id
	@Getter @Setter
	private String updateuserid;     //  NVARCHAR2(50),修改人id
	@Getter @Setter
	private String updatetime;       //  DATE,修改时间
	@Getter @Setter
	private String version;          //  NUMBER,版本
	@Getter @Setter
	private String qdtime;           //  DATE,签订开始时间
	@Getter @Setter
	private String qdwctime;         //  DATE,签订完成时间
	@Getter @Setter
	private String batime;           //  DATE,备案申请时间
	@Getter @Setter
	private String bawctime;         //  DATE,备案完成时间
	@Getter @Setter
	private String zxtime;           //  DATE,注销申请时间
	@Getter @Setter
	private String zxwctime;         //  DATE,注销完成时间
	@Getter @Setter
	private String parentid;         //  NUMBER(11),模板配置id
	@Getter @Setter
	private String source;           //  NVARCHAR2(50),自定义模板rowguid
	@Getter @Setter
	private String name;             //  NVARCHAR2(50),模板名称
	@Getter @Setter
	private String is_delete;        //  NUMBER,是否删除（1是；2否）
	@Getter @Setter
	private String state;            //  NUMBER,状态（字典：445）
	@Getter @Setter
	private String editing;          // NUMBER,编辑状态（1正常……其余为正在办理的业务）
	@Getter @Setter
	private String remark;           // NVARCHAR2(200),备注
	@Getter @Setter
	private String htbh;             // NVARCHAR2(50),合同编号（流水号）
	@Getter @Setter
	private String beianno;          // NVARCHAR2(50),备案号（字符串）
	@Getter @Setter
	private String fileguid;         // NVARCHAR2(100),附件id（无用）
	@Getter @Setter
	private String cmr;              // NVARCHAR2(100),出卖人（公司名称）
	@Getter @Setter
	private String msr;              // NVARCHAR2(100),买受人
	@Getter @Setter
	private String lxsj;             // NVARCHAR2(200),联系手机
	@Getter @Setter
	private String roomno;           // NVARCHAR2(100),户号
	@Getter @Setter
	private String roomlocation;     // NVARCHAR2(100),户坐落
	@Getter @Setter
	private String contractprice;    // NUMBER(18,2),总价
	@Getter @Setter
	private String mj;               // NUMBER(18,2),面积
	@Getter @Setter
	private String fkfs;             // NVARCHAR2(50),付款方式
	@Getter @Setter
	private String sfkbfb;           // NUMBER(18,2),首付款百分比
	@Getter @Setter
	private String msrsfz;           // NVARCHAR2(500),买受人身份证
	@Getter @Setter
	private String xsfs;             // NVARCHAR2(50),销售方式
	@Getter @Setter
	private String certificatenumber;// NVARCHAR2(255),预销售证编号
	@Getter @Setter
	private String licenserowguid;   // NVARCHAR2(50),预销售证rowguid
	@Getter @Setter
	private String buildingno;       // NVARCHAR2(50),幢号
	@Getter @Setter
	private String intercept;        // NUMBER(1) default 1,是否拦截（0为否，1为是）
	@Getter @Setter
	private String regioncode;       // NVARCHAR2(50),所在行政区域（文字）
	@Getter @Setter
	private String roomusage;        // NVARCHAR2(255),房屋用途
	@Getter @Setter
	private String msrlx;            // NUMBER(1),买受人类型（1自然人、2机构）
	@Getter @Setter
	private String trusteestatus;    // NUMBER(1),托管状态（0托管1非托管2托管取消）
	@Getter @Setter
	private String sfk;              // NUMBER(18,2)首付款
	@Getter @Setter
	private String jfrq;			 // 交付日期
}