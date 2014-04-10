package example;

import java.util.Iterator;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;
import example.model.Timer;

/**
 * 遍历出监听某个epl的所有updateListener
 * 
 * @author luonanqin
 *
 */
public class IteratorUpdateListenerTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String timer = Timer.class.getName();
		String epl1 = "select * from " + timer;

		EPStatement state = admin.createEPL(epl1);
		state.addListener(new IteratorUpdateListener());

		Iterator<UpdateListener> listeners = state.getUpdateListeners();
		while (listeners.hasNext()) {
			System.out.println(listeners.next().getClass().getName());
		}
	}

}
