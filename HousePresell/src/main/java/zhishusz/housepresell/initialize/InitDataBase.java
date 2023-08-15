package zhishusz.housepresell.initialize;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库初始化，总的执行方法
 * @author http://zhishusz
 *
 */
@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitDataBase extends BaseJunitTest
{
	@Autowired
	private PermissionInitService permissionInitService;
	@Autowired
	private Tg_HandleTimeLimitConfigInitService tg_handleTimeLimitConfigInitService;
	@Autowired
	private Tg_HolidayInitService tg_holidayInitService;
	
	/**
	 * 初始化数据到数据库中执行方法
	 */
	@Test
	public void execute() throws Exception 
	{
		/*//=========== 权限资源(UIResource)初始化，执行方法 Start ========//
		//Step1-->持久化，每一个URL链接，以及对应链接下的所有按钮
		permissionInitService.Step1_saveResourseFromJsonFile("UrlAndBtnRelationConfig.json");
		//Step2-->持久化，每一个权限资源对象（关联关系暂时不保存）
		permissionInitService.Step1_saveResourseFromJsonFile("PermissionConfig.json");
		//Step3-->持久化，每一个菜单之前的关联关系
		permissionInitService.Step3_saveRelation();
		//Step4-->持久化，每一个实体菜单权限所关联的URL链接对象
		permissionInitService.Step4_saveRealityMenuAndUrlRelation();
		//Step5-->持久化，工作时间检查配置
		tg_handleTimeLimitConfigInitService.Step5_HandleTimeLimitConfig();
		//Step6-->持久化，风控例行抽查比例配置
		tg_RiskRoutineCheckRatioConfigInitService.Step6_saveRiskRoutineCheckRatioConfig();*/
		//Step7-->持久化，假日
		tg_holidayInitService.Step7_Holiday2();
		//=========== 权限资源(UIResource)初始化，执行方法 End =========//
	}
}
