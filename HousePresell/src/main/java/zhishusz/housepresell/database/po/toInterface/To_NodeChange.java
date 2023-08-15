package zhishusz.housepresell.database.po.toInterface;

import lombok.Getter;
import lombok.Setter;

public class To_NodeChange {
    // 方法 add,edit,del
    @Getter
    @Setter
    private String action;

    // 业务类型 pj项目 bld楼栋 ts托管系统
    @Getter
    @Setter
    private String cate;

    // 托管系统楼栋ID
    @Getter
    @Setter
    private String ts_bld_id;

    // 托管系统该条记录ID
    @Getter
    @Setter
    private String ts_id;

    // 进度状态
    @Getter
    @Setter
    private String jdzt;

    // 交付备案通知书网址
    @Getter
    @Setter
    private String jfbatzs_url;

    // 主管部门网站公示网址
    @Getter
    @Setter
    private String gswz_url;

    // 第一监理单位名称
    @Getter
    @Setter
    private String jlbg_name1;

    // 第一监理报告网址
    @Getter
    @Setter
    private String jlbg_url1;

    // 第二监理单位名称
    @Getter
    @Setter
    private String jlbg_name2;

    // 第二监理报告网址
    @Getter
    @Setter
    private String jlbg_url2;

    // 进度变更时间
    @Getter
    @Setter
    private String jdtime;
    
    // 公安编号对照表
    @Getter
    @Setter
    private String gabhdzb;
    
    //监理报告网址
    @Getter
    @Setter
    private String jlbg_url;
    
    //主管部门网站公示打开后的截图
    @Getter
    @Setter
    private String gswz_picurl;

}
