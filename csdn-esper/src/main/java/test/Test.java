package test;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import example.model.Product;

/**
 * 
 * @author luonanqin
 *
 */
public class Test {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String product = Product.class.getName();
		//		String epl1 = "select * from " + product + ".win:time_batch(10 sec)";
		String epl1 = "select sum(price) from " + product + ".win:length_batch(2)";

		EPStatement state = admin.createEPL(epl1);
		state.addListener(new TestListener());

		EPRuntime runtime = epService.getEPRuntime();

		Product esb = new Product();
		esb.setPrice(1);
		esb.setType("esb");
		runtime.sendEvent(esb);

		Product eos = new Product();
		eos.setPrice(2);
		eos.setType("eos");
		runtime.sendEvent(eos);


		Product esb1 = new Product();
		esb1.setPrice(2);
		esb1.setType("esb");
		runtime.sendEvent(esb1);

		Product eos1 = new Product();
		eos1.setPrice(5);
		eos1.setType("eos");
		runtime.sendEvent(eos1);

		System.out.println();

		Product esb2 = new Product();
		esb2.setPrice(3);
		esb2.setType("esb");
		runtime.sendEvent(esb2);

		Product eos3 = new Product();
		eos3.setPrice(6);
		eos3.setType("eos");
		runtime.sendEvent(eos3);

		System.out.println();
	}
}
