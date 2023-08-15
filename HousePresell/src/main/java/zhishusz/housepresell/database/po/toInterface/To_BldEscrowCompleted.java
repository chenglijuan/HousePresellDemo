package zhishusz.housepresell.database.po.toInterface;

import lombok.Getter;
import lombok.Setter;

public class To_BldEscrowCompleted {
    // 方法 add
    @Getter
    @Setter
    private String action;

    // 业务类型ldjd
    @Getter
    @Setter
    private String cate;
    
    //进度状态
    @Getter
    @Setter
    private String jdzt;

    // 托管系统楼栋ID
    @Getter
    @Setter
    private String ts_bld_id;

    // 托管系统该条记录ID
    @Getter
    @Setter
    private String ts_id;
    
    // 公安编号对照表
    @Getter
    @Setter
    private String gabhdzb;
    
    // 主管部门网站公示网址
    @Getter
    @Setter
    private String gswz_url;
    
    // 主管部门网站公示打开后的截图
    @Getter
    @Setter
    private String gswz_picurl;

    // 进度变更时间
    @Getter
    @Setter
    private String jdtime;
    
}
