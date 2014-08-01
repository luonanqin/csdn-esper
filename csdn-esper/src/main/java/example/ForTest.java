package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 7/31/14.
 */
class ForEvent {

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String toString() {
		return "ForEvent{" + "name='" + name + '\'' + ", age=" + age + '}';
	}
}

class ForListener implements UpdateListener {

	// 用于记录调用次数
	private int num = 1;

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		System.out.println("invocation: " + num++);
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println(newEvents[i].getUnderlying());
			}
		}
	}
}

public class ForTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String forEvent = ForEvent.class.getName();
		String select = "select * from " + forEvent + ".win:length_batch(3)";
		String for1 = "select * from " + forEvent + ".win:length_batch(3) for grouped_delivery (age)";
		String for2 = "select * from " + forEvent + ".win:length_batch(3) for discrete_delivery";
		// not valid! because select clause isn't contain age
		// select name from ForEvent.win:lenght_batch(3) for grouped_delivery (age)

		ForEvent fe1 = new ForEvent();
		fe1.setName("luo");
		fe1.setAge(1);

		ForEvent fe2 = new ForEvent();
		fe2.setName("nan");
		fe2.setAge(2);

		ForEvent fe3 = new ForEvent();
		fe3.setName("qin");
		fe3.setAge(1);

		EPStatement stat1 = admin.createEPL(select);
		stat1.addListener(new ForListener());
		System.out.println("select EPL1: " + select);

		System.out.println();
		runtime.sendEvent(fe1);
		runtime.sendEvent(fe2);
		runtime.sendEvent(fe3);
		stat1.destroy();
		System.out.println();

		EPStatement stat2 = admin.createEPL(for1);
		stat2.addListener(new ForListener());
		System.out.println("for EPL2: " + for1);

		System.out.println();
		runtime.sendEvent(fe1);
		runtime.sendEvent(fe2);
		runtime.sendEvent(fe3);
		stat2.destroy();
		System.out.println();

		EPStatement stat3 = admin.createEPL(for2);
		stat3.addListener(new ForListener());
		System.out.println("for EPL3: " + for2);

		System.out.println();
		runtime.sendEvent(fe1);
		runtime.sendEvent(fe2);
		runtime.sendEvent(fe3);
	}
}
