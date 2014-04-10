package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Fruit;

/**
 * 
 * @author luonanqin
 * 
 */
public class PatternTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String fruit = Fruit.class.getName();
		String pattern1 = "";
		String epl1 = "every p1=" + fruit + " -> p2=" + fruit + "(p1.price>p2.price)";
		EPStatement state = admin.createPattern(epl1, "epl1");

		state.addListener(new PattenListener());

		EPRuntime runtime = epService.getEPRuntime();

		Fruit apple = new Fruit();
		apple.setName("apple");
		apple.setPrice(50);
		runtime.sendEvent(apple);

		Fruit orange = new Fruit();
		orange.setName("orange");
		orange.setPrice(55);
		runtime.sendEvent(orange);

		Fruit banana = new Fruit();
		banana.setName("banana");
		banana.setPrice(48);
		runtime.sendEvent(banana);

		Fruit pink = new Fruit();
		pink.setName("pink");
		pink.setPrice(47);
		runtime.sendEvent(pink);

		Fruit pear = new Fruit();
		pear.setName("pear");
		pear.setPrice(43);
		runtime.sendEvent(pear);

		Fruit peach = new Fruit();
		peach.setName("peach");
		peach.setPrice(46);
		runtime.sendEvent(peach);

		Fruit strawberry = new Fruit();
		strawberry.setName("strawberry");
		strawberry.setPrice(43);
		runtime.sendEvent(strawberry);

	}
}
