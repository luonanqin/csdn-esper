package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPOnDemandQueryResult;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventBean;

import java.io.Serializable;

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
		return "SplitEvent{" + "size=" + size + ", price=" + price + '}';
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
		String insert1 = "insert into LargeOrder select price where price > 8";
		String insert2 = "insert into MediumOrder select price where price between 3 and 10";
		String insert3 = "insert into SmallOrder select price where price <= 3";
		String epl7 = "on " + splitEvent + " " + insert1 + " " + insert2 + " " + insert3;

		// output all
		String insert4 = "insert into LargeSize select size where size > 5";
		String insert5 = "insert into MediumSize select size where size between 2 and 8";
		String insert6 = "insert into SmallSize select size where size <= 2";
		String epl8 = "on " + splitEvent + " " + insert4 + " " + insert5 + " " + insert6 + " output all";

		System.out.println("Output first(default): ");
		System.out.println(insert1);
		System.out.println(insert2);
		System.out.println(insert3);

		System.out.println();

		System.out.println("Output all: ");
		System.out.println(insert4);
		System.out.println(insert5);
		System.out.println(insert6);

		String selectLargeOrder = "select * from LargeOrder";
		String selectMediumOrder = "select * from MediumOrder";
		String selectSmallOrder = "select * from SmallOrder";

		String selectLargeSize = "select * from LargeSize";
		String selectMediumSize = "select * from MediumSize";
		String selectSmallSize = "select * from SmallSize";

		System.out.println();

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

		System.out.println(se4);
		runtime.sendEvent(se1);
		System.out.println(se1);
		runtime.sendEvent(se2);
		System.out.println(se2);
		runtime.sendEvent(se3);
		System.out.println(se3);
		runtime.sendEvent(se4);
		runtime.sendEvent(se5);
		System.out.println(se5);

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
