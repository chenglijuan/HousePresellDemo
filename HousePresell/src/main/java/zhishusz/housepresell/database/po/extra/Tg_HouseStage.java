package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-09-18.
 * Company: zhishusz
 */

public class Tg_HouseStage
{

	@Getter @Setter
	private String stageName;
	@Getter @Setter
	private Double percent;

	public Tg_HouseStage(String stageName, Double percent)
	{
		this.stageName = stageName;
		this.percent = percent;
	}
}
