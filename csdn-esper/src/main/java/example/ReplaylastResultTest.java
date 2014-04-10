package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import example.model.User;

/**
 * 重现上一个/上一批事件的处理结果
 * 这里重现的是view中两个User的id和，在新event进入后，可得到进入前view中User产生的事件处理结果
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class ReplaylastResultTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String epl1 = "select avg(id) as aid from " + User.class.getName() + ".win:length(2)";

		EPStatement state = admin.createEPL(epl1);
		state.addListenerWithReplay(new ResultListener());

		EPRuntime runtime = epService.getEPRuntime();

		User user1 = new User();
		user1.setId(1);
		System.out.println("sendEvent: " + user1);
		runtime.sendEvent(user1);

		safeCurrentEventResult(state);

		User user2 = new User();
		user2.setId(3);
		System.out.println("sendEvent: " + user2);
		runtime.sendEvent(user2);

		safeCurrentEventResult(state);

		User user3 = new User();
		user3.setId(5);
		System.out.println("sendEvent: " + user3);
		runtime.sendEvent(user3);

		safeCurrentEventResult(state);

		User user4 = new User();
		user4.setId(5);
		System.out.println("sendEvent: " + user4);
		runtime.sendEvent(user4);

		safeCurrentEventResult(state);
	}

	public static void safeCurrentEventResult(EPStatement state) {
		SafeIterator<EventBean> events = state.safeIterator();
		while (events.hasNext()) {
			EventBean event = events.next();
			System.out.println("replayResult: " + event.get("aid"));
		}
		events.close();
	}
}
