package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * Created by Luonanqin on 3/17/14.
 */

class OrderEvent {

	private int price;
	private OrderEvent o;

	public OrderEvent getO() {
		return o;
	}

	public void setO(OrderEvent o) {
		this.o = o;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}

class SubOrderEvent extends OrderEvent {

}

interface InterfaceEvent {
	public int getPrice();

	public String getName();
}

class InterfaceEventImpl implements InterfaceEvent {
	private int price;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {

		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}

public class CreateNamedWindowTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();

		String orderEvent = OrderEvent.class.getName();
		String subOrderEvent = SubOrderEvent.class.getName();
		String epl1 = "create window OrderWindow.win:time(30 sec) select o, price from " + orderEvent;
		String epl2 = "insert into OrderWindow select o, price from " + subOrderEvent + " as o";

		String interfaceEvent = InterfaceEvent.class.getName();
		String interfaceEventImpl = InterfaceEventImpl.class.getName();
		String epl3 = "create window InterfaceWindow.win: time(2 sec) as select * from " + interfaceEvent;
		String epl4 = "insert into InterfaceWindow select * from " + interfaceEventImpl;

		admin.createEPL(epl1);
		System.out.println("Create EPL: " + epl1);
		admin.createEPL(epl2);
		System.out.println("Create EPL: " + epl2);

		admin.createEPL(epl3);
		System.out.println("Create EPL: " + epl3);
		admin.createEPL(epl4);
		System.out.println("Create EPL: " + epl4);

	}
}
