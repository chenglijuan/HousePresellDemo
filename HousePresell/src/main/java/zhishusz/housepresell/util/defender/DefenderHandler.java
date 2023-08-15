package zhishusz.housepresell.util.defender;

import zhishusz.housepresell.controller.form.BaseForm;

import java.util.ArrayList;

/**
 * Created by Dechert on 2018-11-13.
 * Company: zhishusz
 */

public class DefenderHandler {
    private int intervalTime;
    private int maxListSize;
    private ArrayList<DefenderModel> defenderList;
    private int nowIndex = 0;
    private boolean showLog;

    public DefenderHandler() {
        defenderList = new ArrayList<DefenderModel>(maxListSize);
//        System.out.println("maxListSize is " + maxListSize);
        initAdd();
    }

    private void initAdd() {
        for (int i = 0; i < maxListSize; i++) {
            defenderList.add(new DefenderModel());
        }
    }

    public synchronized int add(String interfaceName, Long userId) {
        if (userId == null) {
            return -1;
        }
        DefenderModel defenderModel = new DefenderModel();
        defenderModel.setUserId(userId);
        defenderModel.setInterfaceName(interfaceName);
        defenderModel.setReceiveTimeStamp(System.currentTimeMillis());
        if (nowIndex == maxListSize) {
            nowIndex = 0;
        }
        defenderModel.setIndex(nowIndex);
        defenderList.set(nowIndex, defenderModel);
        int thisIndex = nowIndex;
        nowIndex++;
        return thisIndex;
    }

    public synchronized ArrayList<DefenderModel> getNodeList(Long userId, String interfaceName) {
        ArrayList<DefenderModel> resultList = new ArrayList<>();
        if (userId == null) {
            return null;
        }
        for (DefenderModel model : defenderList) {
            if (model.getUserId() == null) continue;
            if (model.getUserId().equals(userId) && model.getInterfaceName().equals(interfaceName)) {
                resultList.add(model);
            }
        }
        return resultList;
    }

    public synchronized void finishHandleNode(int index, String interfaceName, Long userId) {
        DefenderModel defenderModel = defenderList.get(index);
        if (userId == null) {
            return;
        }
        if (defenderModel.getUserId().equals(userId) && defenderModel.getInterfaceName().equals(interfaceName)) {
            defenderModel.setHandled(true);
            defenderModel.setHandledTimeStamp(System.currentTimeMillis());
        }
    }

    public synchronized void finishHandleNode(int index, String interfaceName, BaseForm model) {
        finishHandleNode(index, interfaceName, model.getUserId());
    }

    public synchronized DefenderModel getLastUserNode(Long userId, String interfaceName) {
        long maxTimeStamp = 0L;
        DefenderModel defenderModel = null;
        if (defenderList.size() == 0) {
            initAdd();
        }
        for (int i = 0; i < maxListSize; i++) {
            DefenderModel item = defenderList.get(i);
            if (item.getUserId() == null || item.getInterfaceName() == null) continue;
            if (item.getUserId().equals(userId) && item.getInterfaceName().equals(interfaceName)) {
//                System.out.println("node is "+item.toString());
                Long receiveTimeStamp = item.getReceiveTimeStamp();
                if (receiveTimeStamp > maxTimeStamp) {
                    defenderModel = item;
                    maxTimeStamp = receiveTimeStamp;
                }
            }
        }
        return defenderModel;
    }

    public synchronized boolean isNeedExclude(DefenderModel defenderModel) {
        if (defenderModel == null) {
            return false;
        }
        boolean handled = defenderModel.isHandled();
        if (!handled) {
            return true;
        }
        long now = System.currentTimeMillis();
        long interval = now - defenderModel.getReceiveTimeStamp();
        if (showLog) {
            System.out.println("interval time is " + interval);
        }
        if (interval <= intervalTime) {
            return true;
        }
        return false;
    }

    public synchronized DefenderMessage isNeedExcludeAndAdd(String interfaceName, Long userId) {
        DefenderModel lastUserNode = getLastUserNode(userId, interfaceName);
        if (showLog && lastUserNode != null) {
            System.out.println("lastest node is " + lastUserNode.toString());
        } else {
            System.out.println("lastest node is null");
        }
        boolean needExclude = isNeedExclude(lastUserNode);
        DefenderMessage defenderMessage = new DefenderMessage();
        defenderMessage.setNeedExclude(needExclude);
        if (!needExclude) {
            int theIndex = add(interfaceName, userId);
            if(showLog){
                System.out.println("add node finish now index is "+nowIndex);
            }
            defenderMessage.setTheIndex(theIndex);
        } else {
            defenderMessage.setTheIndex(lastUserNode.getIndex());
        }
        return defenderMessage;
    }

    public synchronized DefenderMessage isNeedExcludeAndAdd(String interfaceName, BaseForm model) {
        return isNeedExcludeAndAdd(interfaceName, model.getUserId());
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getMaxListSize() {
        return maxListSize;
    }

    public void setMaxListSize(int maxListSize) {
        this.maxListSize = maxListSize;
    }

    public int getNowIndex() {
        return nowIndex;
    }

    public void setNowIndex(int nowIndex) {
        this.nowIndex = nowIndex;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public class DefenderMessage {
        private boolean isNeedExclude;
        private int theIndex;

        public boolean isNeedExclude() {
            return isNeedExclude;
        }

        public void setNeedExclude(boolean needExclude) {
            isNeedExclude = needExclude;
        }

        public int getTheIndex() {
            return theIndex;
        }

        public void setTheIndex(int theIndex) {
            this.theIndex = theIndex;
        }
    }
}
