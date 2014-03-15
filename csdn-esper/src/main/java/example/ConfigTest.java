package example;

import java.io.File;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EventType;

import example.model.ESB;
import example.model.Product;

/**
 * ConfigurationOperations对象可以获取配置文件和运行时的EventType等其他配置
 * 
 * @author luonanqin
 *
 */
public class ConfigTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration config = new Configuration();
		config.configure(new File("etc/esper.examples.cfg.xml"));
		EPServiceProvider epService = EPServiceProviderManager.getProvider("default", config);

		EPAdministrator admin = epService.getEPAdministrator();

		String esb = ESB.class.getName();
		String context1 = "create context esbtest partition by id from " + esb;
		String epl1 = "context esbtest select avg(price) as aPrice from " + esb;

		admin.createEPL(context1, "context1");
		admin.createEPL(epl1, "epl1");

		ConfigurationOperations conf = admin.getConfiguration();
		conf.addEventType("product", Product.class);

		EventType[] types = conf.getEventTypes();
		for (EventType type : types) {
			System.out.println("name: " + type.getName() + ", underlyingName: " + type.getUnderlyingType());
		}
	}

}
