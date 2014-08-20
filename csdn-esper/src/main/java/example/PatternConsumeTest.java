package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by Luonanqin on 8/11/14.
 */
class ConsumeEvent {

	private int id;
	private String name;
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
		return "ConsumeEvent{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
	}
}

class PatternConsumeListener1 implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println("a: " + newEvents[i].get("a"));
				System.out.println("b: " + newEvents[i].get("b"));
			}
		}
	}
}

class PatternConsumeListener2 implements UpdateListener {

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			for (int i = 0; i < newEvents.length; i++) {
				System.out.println("a: " + newEvents[i].get("a"));
				System.out.println("b: " + newEvents[i].get("b"));
				System.out.println("c: " + newEvents[i].get("c"));
			}
		}
	}
}

public class PatternConsumeTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String consume = ConsumeEvent.class.getName();

		String epl1 = "every (a=" + consume + "(id = 1)@consume and b=" + consume + "(name = 'luonq'))";
		System.out.println("EPL1: " + epl1 + "\n");

		EPStatement stat1 = admin.createPattern(epl1);
		stat1.addListener(new PatternConsumeListener1());

		ConsumeEvent ce1 = new ConsumeEvent();
		ce1.setId(1);
		ce1.setName("luonq");
		System.out.println("Send Event: " + ce1);
		runtime.sendEvent(ce1);

		System.out.println();

		ConsumeEvent ce2 = new ConsumeEvent();
		ce2.setId(2);
		ce2.setName("luonq");
		System.out.println("Send Event: " + ce2);
		runtime.sendEvent(ce2);

		stat1.destroy();
		System.out.println();

		String epl2 = "every (a=" + consume + "(id = 1)@consume(2) or b=" + consume + "(name = 'luonq')@consume(3) or c=" + consume + "(age > 2))";
		System.out.println("EPL2: " + epl2 + "\n");

		EPStatement stat2 = admin.createPattern(epl2);
		stat2.addListener(new PatternConsumeListener2());

		ConsumeEvent ce3 = new ConsumeEvent();
		ce3.setId(1);
		ce3.setName("luonq");
		ce3.setAge(3);
		System.out.println("Send Event: " + ce3);
		runtime.sendEvent(ce3);

		ConsumeEvent ce4 = new ConsumeEvent();
		ce4.setId(1);
		ce4.setName("luonqin");
		ce4.setAge(1);
		System.out.println("Send Event: " + ce4);
		runtime.sendEvent(ce4);

		ConsumeEvent ce5 = new ConsumeEvent();
		ce5.setId(3);
		ce5.setName("luonqin");
		ce5.setAge(5);
		System.out.println("Send Event: " + ce5);
		runtime.sendEvent(ce5);
	}
}
