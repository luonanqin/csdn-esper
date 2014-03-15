package example;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.espertech.esper.client.hook.SQLOutputRowConversion;
import com.espertech.esper.client.hook.SQLOutputRowTypeContext;
import com.espertech.esper.client.hook.SQLOutputRowValueContext;

/**
 * Created by Luonanqin on 2/10/14.
 */
public class MySQLOutputRowConvertor implements SQLOutputRowConversion {

	public Class getOutputRowType(SQLOutputRowTypeContext context) {
		return String.class;
	}

	public Object getOutputRow(SQLOutputRowValueContext context) {
		ResultSet result = context.getResultSet();
		Object obj1 = null;
		Object obj2 = null;
		try {
			obj1 = result.getObject("id");
			obj2 = result.getObject("name");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return obj1 + " and " + obj2;
	}
}
