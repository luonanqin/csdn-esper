package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * unidirectional可以在join的时候使被其修饰的事件流无状态，只要来一个事件就可以出发join操作
 * 
 * @author luonanqin
 * 
 */
class Orange {
	private int price;

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public String toString() {
		return "Orange price=" + price;
	}
}

class Banana {
	private int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String toString() {
		return "Banana price=" + price;
	}
}

class JoinUnidirectionalListener implements UpdateListener {
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents != null) {
			System.out.println(newEvents[0].get("o") + ", " + newEvents[0].get("b"));
		}
	}
}

public class JoinUnidirectionalTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String epl1 = "select * from " + Orange.class.getName() + "	as o unidirectional, " + Banana.class.getName()
				+ ".std:lastevent() as b where o.price = b.price";
		/*
		 * unidirectional只能用于一个事件流
		 */
		// String epl1 = "select * from " + Orange.class.getName() + "	as o unidirectional, " + Banana.class.getName() +
		// " as b unidirectional where o.price = b.price";

		EPStatement stat = admin.createEPL(epl1);
		stat.addListener(new JoinUnidirectionalListener());

		EPRuntime runtime = epService.getEPRuntime();

		/*
		 * Orange事件是无状态的，所以先进入的Orange事件发现没有满足join条件就立刻被移除了。 Banana事件到达后，因为之前的Orange事件已经被移除，所以仍然没有输出
		 */
		Orange o1 = new Orange();
		o1.setPrice(1);
		System.out.println("Send Orange1");
		runtime.sendEvent(o1);

		Banana b1 = new Banana();
		b1.setPrice(1);
		System.out.println("Send Banana1");
		runtime.sendEvent(b1);

		/*
		 * Banana事件到达后，没有达到join条件，但是被暂存了起来 Orange事件是无状态的，所以进入的Orange事件发现有满足join条件的Banana，所以有输出。
		 */
		Banana b2 = new Banana();
		b2.setPrice(2);
		System.out.println("Send Banana2");
		runtime.sendEvent(b2);

		Orange o2 = new Orange();
		o2.setPrice(2);
		System.out.println("Send Orange2");
		runtime.sendEvent(o2);
	}
}
