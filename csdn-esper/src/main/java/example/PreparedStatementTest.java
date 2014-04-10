package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPPreparedStatement;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import example.model.User;

/**
 * 预定义EPL语句，即preparedEPL，然后传递参数给占位符，最后转为EPL
 * 
 * @author luonq(luonq@primeton.com)
 *
 */
public class PreparedStatementTest {

	public static void main(String[] args) {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String preEpl = "select ? from " + User.class.getName() + ".win:length(?)";
		EPPreparedStatement pstat = admin.prepareEPL(preEpl);
		System.out.println("preparedEPL: " + preEpl);
		pstat.setObject(1, "irstream id");
		pstat.setObject(2, 5);

		EPStatement stat = admin.create(pstat);
		System.out.println("EPL: " + stat.getText());
	}
}
