package zhishusz.housepresell.util.defender;

/**
 * Created by Dechert on 2018-11-13.
 * Company: zhishusz
 */

public class MemoryDefender {
    private static DefenderHandler defenderHandler = new DefenderHandler();

    public static int add(String interfaceName, Long userId) {
        String temp = "";
        if (interfaceName.contains("/")) {
            temp = interfaceName.substring(1, interfaceName.length());
        } else {
            temp = interfaceName;
        }
        return defenderHandler.add(interfaceName, userId);
    }

    public static boolean isNeedExclude(String interfaceName, Long userId) {
        DefenderModel lastUserNode = defenderHandler.getLastUserNode(userId, interfaceName);
        if(lastUserNode!=null){
            System.out.println("nowIndex is " + defenderHandler.getNowIndex() + " lastUserNode is " + lastUserNode.toString());
        }else{
            System.out.println("nowIndex is " + defenderHandler.getNowIndex()+ " lastUserNode is null!");
        }
        boolean needExclude = defenderHandler.isNeedExclude(lastUserNode);
        return needExclude;
    }

    public static void handleNode(int index, String interfaceName, Long userId) {
        defenderHandler.finishHandleNode(index, interfaceName, userId);
    }


}
