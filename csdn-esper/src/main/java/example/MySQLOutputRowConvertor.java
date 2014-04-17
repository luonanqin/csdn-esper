package example;

import com.espertech.esper.client.hook.SQLOutputRowConversion;
import com.espertech.esper.client.hook.SQLOutputRowTypeContext;
import com.espertech.esper.client.hook.SQLOutputRowValueContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Luonanqin on 2/10/14.
 */
public class MySQLOutputRowConvertor implements SQLOutputRowConversion {

	// 设置输出内容的数据类型
	public Class getOutputRowType(SQLOutputRowTypeContext context) {
		return String.class;
	}

	// 返回转换后的内容
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
