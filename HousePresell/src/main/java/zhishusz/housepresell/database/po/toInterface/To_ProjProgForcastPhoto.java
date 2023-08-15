package zhishusz.housepresell.database.po.toInterface;

import lombok.Getter;
import lombok.Setter;

public class To_ProjProgForcastPhoto {
    // 方法 add,edit,del
    @Getter
    @Setter
    private String action;
    
    @Getter
    @Setter
    private String actionType;

    // 业务类型jindu
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
    
    // 托管系统该条项目ID
    @Getter
    @Setter
    private String ts_pj_id;
    
    // 航拍日期
    @Getter
    @Setter
    private String jdtime;
    
    // 节点日期+楼层+上传类型
    @Getter
    @Setter
    private String news_title;
    
    //项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型
    @Getter
    @Setter
    private String news_title1;
    
    //批量上传缩略图(4张,分割，首张为总平图)
    @Getter
    @Setter
    private String smallpic;
    
    //批量上传高清大图(4张,分割，首张为总平图)
    @Getter
    @Setter
    private String image2;
    
    //当前楼层（整数类型）
    @Getter
    @Setter
    private String dqlc;

}
