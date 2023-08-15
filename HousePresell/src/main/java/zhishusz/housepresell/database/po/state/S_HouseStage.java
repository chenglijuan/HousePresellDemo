package zhishusz.housepresell.database.po.state;

import zhishusz.housepresell.database.po.extra.Tg_HouseStage;

import java.util.HashMap;

/**
 * Created by Dechert on 2018-09-18.
 * Company: zhishusz
 */

public class S_HouseStage
{

	public static HashMap<Integer,Tg_HouseStage> BUILDING_STAGE=new HashMap<>();
	static {
		BUILDING_STAGE.put(1,new Tg_HouseStage("正负零前",100d));
		BUILDING_STAGE.put(2,new Tg_HouseStage("正负零",80d));
		BUILDING_STAGE.put(3,new Tg_HouseStage("主体结构达到1/2",60d));
		BUILDING_STAGE.put(4,new Tg_HouseStage("主体结构封顶",40d));
		BUILDING_STAGE.put(5,new Tg_HouseStage("外立面装饰工程完成",20d));
		BUILDING_STAGE.put(6,new Tg_HouseStage("完成交付使用备案",0d));
	}

	public static HashMap<Integer,Tg_HouseStage> COMPLETE_STAGE=new HashMap<>();
	static {
		COMPLETE_STAGE.put(1,new Tg_HouseStage("正负零前",100d));
		COMPLETE_STAGE.put(2,new Tg_HouseStage("正负零",80d));
		COMPLETE_STAGE.put(3,new Tg_HouseStage("主体结构达到1/2",60d));
		COMPLETE_STAGE.put(4,new Tg_HouseStage("主体结构封顶",40d));
		COMPLETE_STAGE.put(5,new Tg_HouseStage("外立面装饰工程完成",35d));
		COMPLETE_STAGE.put(6,new Tg_HouseStage("室内装修工程完成",5d));
		COMPLETE_STAGE.put(7,new Tg_HouseStage("完成交付使用备案",0d));
	}

}
