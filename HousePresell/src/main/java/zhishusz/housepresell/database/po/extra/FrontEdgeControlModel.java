package zhishusz.housepresell.database.po.extra;

import lombok.Data;

/**
 * Created by Dechert on 2018/11/30.
 * Company: zhishusz
 * Usage:
 */
@Data
public class FrontEdgeControlModel {
    /**
     * 需要禁用，不能让它填写，true为禁用，false为不禁用
     */
    private Boolean needDisable;
    /**
     * 需要必填，true为必填，false为非必填
     */
    private Boolean needInput;
    /**
     * 限制最大字数
     */
    private Integer maxWord;
    /**
     * 限制最少字数
     */
    private Integer minWord;


}
