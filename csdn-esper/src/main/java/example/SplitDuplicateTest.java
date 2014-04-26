package example;

import java.io.Serializable;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;

/**
 * Created by Luonanqin on 4/26/14.
 */
class SplitEvent implements Serializable {

	private int size;
	private int price;

	public SplitEvent(int price, int size) {
		this.price = price;
		this.size = size;
	}

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

	public String toString() {
		return "SplitEvent{" + "size=" + size + ", quantity=" + price + '}';
	}
}

public class SplitDuplicateTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		EPRuntime runtime = epService.getEPRuntime();

		String splitEvent = SplitEvent.class.getName();

		String epl1 = "create window LargeOrder.win:keepall() (price int) ";
		String epl2 = "create window MediumOrder.win:keepall() (price int)";
		String epl3 = "create window SmallOrder.win:keepall() (price int)";
		String epl4 = "create window LargeSize.win:keepall() (size int)";
		String epl5 = "create window MediumSize.win:keepall() (size int)";
		String epl6 = "create window SmallSize.win:keepall() (size int)";

		// output first
		String epl7 = "on " + splitEvent + " insert into LargeOrder select price where price > 8"
				+ "insert into MediumOrder select price where price between 3 and 10 " + "insert into SmallOrder select price where price <= 3";

		String epl8 = "on " + splitEvent + " insert into LargeSize select size where size > 5 "
				+ "insert into MediumSize select size where size between 2 and 8 " + "insert into SmallSize select size where size <= 2 output all";

		String selectLargeOrder = "select * from LargeOrder";
		String selectMediumOrder = "select * from MediumOrder";
		String selectSmallOrder = "select * from SmallOrder";

		String selectLargeSize = "select * from LargeSize";
		String selectMediumSize = "select * from MediumSize";
		String selectSmallSize = "select * from SmallSize";

		admin.createEPL(epl1);
		admin.createEPL(epl2);
		admin.createEPL(epl3);
		admin.createEPL(epl4);
		admin.createEPL(epl5);
		admin.createEPL(epl6);

		admin.createEPL(epl7);
		admin.createEPL(epl8);

		SplitEvent se1 = new SplitEvent(1, 2);
		SplitEvent se2 = new SplitEvent(9, 4);
		SplitEvent se3 = new SplitEvent(3, 1);
		SplitEvent se4 = new SplitEvent(5, 6);
		SplitEvent se5 = new SplitEvent(7, 9);

		runtime.sendEvent(se1);
		runtime.sendEvent(se2);
		runtime.sendEvent(se3);
		runtime.sendEvent(se4);
		runtime.sendEvent(se5);

		EPOnDemandQueryResult selectLOrder = runtime.executeQuery(selectLargeOrder);
		EPOnDemandQueryResult selectMOrder = runtime.executeQuery(selectMediumOrder);
		EPOnDemandQueryResult selectSOrder = runtime.executeQuery(selectSmallOrder);
		EPOnDemandQueryResult selectLSize = runtime.executeQuery(selectLargeSize);
		EPOnDemandQueryResult selectMSize = runtime.executeQuery(selectMediumSize);
		EPOnDemandQueryResult selectSSize = runtime.executeQuery(selectSmallSize);

		outputResult(selectLargeOrder, selectLOrder);
		outputResult(selectMediumOrder, selectMOrder);
		outputResult(selectSmallOrder, selectSOrder);
		outputResult(selectLargeSize, selectLSize);
		outputResult(selectMediumSize, selectMSize);
		outputResult(selectSmallSize, selectSSize);
	}

	private static void outputResult(String selectSentence, EPOnDemandQueryResult result) {
		System.out.println("\n" + selectSentence);
		EventBean[] events = result.getArray();
		for (int i = 0; i < events.length; i++) {
			EventBean event = events[i];
			System.out.println(event.getUnderlying());
		}
	}
}
