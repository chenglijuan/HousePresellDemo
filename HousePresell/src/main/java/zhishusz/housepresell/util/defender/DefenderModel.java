package zhishusz.housepresell.util.defender;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-11-13.
 * Company: zhishusz
 */

public class DefenderModel {
    @Getter @Setter
    private Long userId;
    @Getter @Setter
    private String interfaceName;
    @Getter @Setter
    private boolean handled;
    @Getter @Setter
    private Long receiveTimeStamp;
    @Getter @Setter
    private Long handledTimeStamp;
    @Getter @Setter
    private int index;

    @Override
    public String toString() {
        return "DefenderModel{" +
                "userId=" + userId +
                ", interfaceName='" + interfaceName + '\'' +
                ", handled=" + handled +
                ", receiveTimeStamp=" + receiveTimeStamp +
                ", handledTimeStamp=" + handledTimeStamp +
                ", index=" + index +
                '}';
    }
}
