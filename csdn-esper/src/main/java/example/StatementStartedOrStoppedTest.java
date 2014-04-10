package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.User;

/**
 * 查看EPL的运行状态
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class StatementStartedOrStoppedTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String epl1 = "select id from " + User.class.getName();
		String epl2 = epl1 + ".win:time(10 sec)";

		admin.createEPL(epl1);
		admin.createEPL(epl2);

		String[] stateNames = admin.getStatementNames();
		for (String name : stateNames) {
			EPStatement state = admin.getStatement(name);
			System.out.println("EPL: " + state.getText() + " State: " + state.getState());
		}
	}

}
