package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * join的各种用法
 * 
 * @author luonanqin
 */
class NOKIA {
	private int price;
	private int size;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}

class Iphone {
	private int price;
	private int size;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}

class JoinListener implements UpdateListener {

	private String id;

	public JoinListener(String id) {
		this.id = id;
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		System.out.print(id+" ");
		if (newEvents != null)
			System.out.println(newEvents.length);
	}
}

public class JoinTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		/**
		 * 左外连接：当满足条件的NOKIA事件到达后才能输出
		 */
		String epl1 = "select * from " + NOKIA.class.getName() + ".std:lastevent() as o left outer join " + Iphone.class.getName()
				+ ".std:lastevent() as b on o.price = b.price";

		/**
		 * 右外链接：当满足条件的Iphone事件到达后才能输出
		 */
		String epl2 = "select * from " + NOKIA.class.getName() + ".std:lastevent() as o right outer join " + Iphone.class.getName()
				+ ".std:lastevent() as b on o.price = b.price";

		/**
		 * 内连接：当满足条件的NOKIA和Iphone事件都出现时才能输出
		 */
		String epl3 = "select * from " + NOKIA.class.getName() + ".std:lastevent() as o inner join " + Iphone.class.getName()
				+ ".std:lastevent() as b on o.price = b.price";

		/**
		 * 外连接：无论是否满足条件，进入的事件都可以立刻输出
		 */
		String epl4 = "select * from " + NOKIA.class.getName() + ".std:lastevent() as o full outer join " + Iphone.class.getName()
				+ ".std:lastevent() as b on o.price = b.price";

		EPStatement stat1 = admin.createEPL(epl1);
		stat1.addListener(new JoinListener("Left Outer Join"));
		EPStatement stat2 = admin.createEPL(epl2);
		stat2.addListener(new JoinListener("Right Outer Join"));
		EPStatement stat3 = admin.createEPL(epl3);
		stat3.addListener(new JoinListener("Inner Join"));
		EPStatement stat4 = admin.createEPL(epl4);
		stat4.addListener(new JoinListener("Full Outer Join"));
		System.out.println("Create epl successfully!");

		EPRuntime runtime = epService.getEPRuntime();

		NOKIA nokia2 = new NOKIA();
		nokia2.setPrice(2);
		System.out.println("\nSend NOKIA2");
		runtime.sendEvent(nokia2);

		NOKIA nokia1 = new NOKIA();
		nokia1.setPrice(1);
		System.out.println("\nSend NOKIA1");
		runtime.sendEvent(nokia1);

		NOKIA nokia3 = new NOKIA();
		nokia3.setPrice(3);
		System.out.println("\nSend NOKIA3");
		runtime.sendEvent(nokia3);

		NOKIA nokia4 = new NOKIA();
		nokia4.setPrice(4);
		System.out.println("\nSend NOKIA4");
		runtime.sendEvent(nokia4);

		Iphone iphone4 = new Iphone();
		iphone4.setPrice(4);
		System.out.println("\nSend Iphone4");
		runtime.sendEvent(iphone4);

		Iphone iphone2 = new Iphone();
		iphone2.setPrice(2);
		System.out.println("\nSend Iphone2");
		runtime.sendEvent(iphone2);

		Iphone iphone3 = new Iphone();
		iphone3.setPrice(3);
		System.out.println("\nSend Iphone3");
		runtime.sendEvent(iphone3);

		Iphone iphone1 = new Iphone();
		iphone1.setPrice(1);
		System.out.println("\nSend Iphone1");
		runtime.sendEvent(iphone1);
	}
}
