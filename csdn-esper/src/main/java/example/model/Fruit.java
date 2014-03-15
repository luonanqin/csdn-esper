package example.model;

public class Fruit {

	private String name;
	private long price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String toString() {
		return "name: " + name + ", price: " + price;
	}
}
