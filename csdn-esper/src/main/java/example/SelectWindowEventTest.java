package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.User;

/**
 * 查看当前view中存在哪些事件
 * 为状态恢复做准备，即将某一时刻的状态记录下来，这里记录的是view中的残留事件
 * 
 * 注：如果view为length_batch或者XXX_batch等，则prevwindow函数只有在每一次batch满足时才会触发
 * 所以针对这类view的状态复制，只有在最新一次的batch满足时才停止事件的输入，然后通过prevwinodw获取到当前最新的事件缓存
 * 若没有满足，则不停止事件输入
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class SelectWindowEventTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String viewEpl = "select prevwindow(user) from " + User.class.getName() + ".win:length(2) as user";

		EPStatement state1 = admin.createEPL(viewEpl);
		state1.addListener(new ViewListener());

		EPRuntime runtime = epService.getEPRuntime();

		User user1 = new User();
		user1.setId(1);
		System.out.println("sendEvent: " + user1);
		runtime.sendEvent(user1);

		User user2 = new User();
		user2.setId(3);
		System.out.println("sendEvent: " + user2);
		runtime.sendEvent(user2);

		User user3 = new User();
		user3.setId(5);
		System.out.println("sendEvent: " + user3);
		runtime.sendEvent(user3);
	}
}
