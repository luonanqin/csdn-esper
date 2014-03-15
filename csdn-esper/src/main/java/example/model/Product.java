package example.model;

public class Product {

	private String type;
	private int price;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String toString() {
		return "type: " + type + ", price: " + price;
	}
}
