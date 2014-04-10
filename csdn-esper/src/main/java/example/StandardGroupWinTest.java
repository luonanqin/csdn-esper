package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.Product;

/**
 * std:groupwin根据type进行分组，并且每一组的事件长度达到2时输出结果，group by是用于分组展示结果。
 * 如果没有group by，则sum计算的是所有事件的price，而不是每一个view单独计算price，groupwin只是为不同的
 * type提供一个length_batch
 * 
 * @author luonanqin
 *
 */
public class StandardGroupWinTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String product = Product.class.getName();
		String epl2 = "select sum(price), type from " + product + ".std:groupwin(type).win:length_batch(2) group by type";

		EPStatement state = admin.createEPL(epl2);
		state.addListener(new StandardGroupWinListener());

		EPRuntime runtime = epService.getEPRuntime();

		Product esb = new Product();
		esb.setPrice(1);
		esb.setType("esb");
		System.out.println("sendEvent: " + esb);
		runtime.sendEvent(esb);

		Product eos = new Product();
		eos.setPrice(2);
		eos.setType("eos");
		System.out.println("sendEvent: " + eos);
		runtime.sendEvent(eos);

		Product esb1 = new Product();
		esb1.setPrice(2);
		esb1.setType("esb");
		System.out.println("sendEvent: " + esb1);
		runtime.sendEvent(esb1);

		Product eos1 = new Product();
		eos1.setPrice(5);
		eos1.setType("eos");
		System.out.println("sendEvent: " + eos1);
		runtime.sendEvent(eos1);
	}
}
