package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Timer;

/**
 * 测试时间窗口的时间到达时输出view中的事件，即oldEvents
 * 只有使用irstream才可以达到此效果，istream显然不会输出oldEvents，但是rstream也不可行
 * 
 * @author luonanqin
 *
 */
public class TimeOldEventsTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String timer = Timer.class.getName();
		String epl1 = "select irstream * from " + timer + ".win:time(3 sec)";

		EPStatement state = admin.createEPL(epl1);
		System.out.println(state.getText());
		state.addListener(new TimeOldEventsListener());

		EPRuntime runtime = epService.getEPRuntime();

		Timer t1 = new Timer();
		t1.setId(1);
		t1.setTime(15);
		System.out.println("send Event: t1");
		runtime.sendEvent(t1);

		Timer t2 = new Timer();
		t2.setId(2);
		t2.setTime(35);
		System.out.println("send Event: t2");
		runtime.sendEvent(t2);

		System.out.println("event input after 3 sec");
		Thread.sleep(1000);
		System.out.println("===== 1 sec");
		Thread.sleep(1000);
		System.out.println("===== 2 sec");
		Thread.sleep(1000);
		System.out.println("===== 3 sec");

		Timer t3 = new Timer();
		t3.setId(3);
		t3.setTime(30);
		System.out.println("send Event: t3");
		runtime.sendEvent(t3);

		Timer t4 = new Timer();
		t4.setId(4);
		t4.setTime(20);
		System.out.println("send Event: t4");
		runtime.sendEvent(t4);

		Timer t5 = new Timer();
		t5.setId(5);
		t5.setTime(20);
		System.out.println("send Event: t5");
		runtime.sendEvent(t5);

		Timer t6 = new Timer();
		t6.setId(6);
		t6.setTime(25);
		System.out.println("send Event: t6");
		runtime.sendEvent(t6);

		System.out.println("event input after 3 sec");
		Thread.sleep(1000);
		System.out.println("===== 1 sec");
		Thread.sleep(1000);
		System.out.println("===== 2 sec");
		Thread.sleep(1000);
		System.out.println("===== 3 sec");
	}
}
