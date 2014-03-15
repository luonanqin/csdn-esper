package example;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * subquer的exists用法
 * 
 * 注：subquery不可返回多列结果
 * 
 * @author luonanqin
 * 
 */
class Apple1 {
	private int price;
	private int size;

	public void setPrice(int price) {
		this.price = price;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPrice() {
		return price;
	}

	public int getSize() {
		return size;
	}
}

class Fruit1 {
}

public class SubqueryExistsTest {

	public static void main(String[] args) throws InterruptedException {
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

		EPAdministrator admin = epService.getEPAdministrator();

		String epl1 = "select * from " + Fruit1.class.getName() + " where exists (select price from " + Apple1.class.getName()
				+ ".std:lastevent() group by price)";
		/*
		 * 同样可以适用于Filter
		 */
		// String epl1 = "select * from " + Fruit.class.getName() + "(exists (select sum(size) from " + Apple.class.getName() + ".std:lastevent()))";

		/*
		 * 注意：不能返回多列结果，只能返回单列
		 */
		// String epl1 = "select * from " + Fruit.class.getName() + " where exists (select price, size from " + Apple.class.getName() + ".std:lastevent())";

		admin.createEPL(epl1);
		System.out.println("Create epl successfully!");
	}
}
