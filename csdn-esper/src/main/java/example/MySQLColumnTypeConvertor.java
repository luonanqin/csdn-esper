package example;

import com.espertech.esper.client.hook.SQLColumnTypeContext;
import com.espertech.esper.client.hook.SQLColumnTypeConversion;
import com.espertech.esper.client.hook.SQLColumnValueContext;
import com.espertech.esper.client.hook.SQLInputParameterContext;

/**
 * Created by Luonanqin on 2/9/14.
 */
public class MySQLColumnTypeConvertor implements SQLColumnTypeConversion{

	public Class getColumnType(SQLColumnTypeContext context) {
		Class clazz = context.getColumnClassType();
		return clazz;
	}

	public Object getColumnValue(SQLColumnValueContext context) {
		Object obj = context.getColumnValue();
		return obj;
	}

	public Object getParameterValue(SQLInputParameterContext context) {
		Object obj = context.getParameterValue();
		return obj;
	}
}
