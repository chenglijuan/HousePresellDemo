package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-11-08.
 * Company: zhishusz
 */

public class MsgInfo {
    @Getter @Setter
    private boolean success;
    @Getter @Setter
    private String info;
    @Getter @Setter
    private Object extra;

}
