package example;

/**
 * Created by Luonanqin on 2/16/14.
 */

class JavaObject {
	private String name;
	private int size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String toString() {
		return "JavaObject{" + "name='" + name + '\'' + ", size=" + size + '}';
	}
}

public class InvocationMethodJava {

	public static JavaObject[] getJavaObject(int times) {
		JavaObject[] javaObjects = new JavaObject[2];
		JavaObject javaObject1 = new JavaObject();
		javaObject1.setName("javaObject1");
		javaObject1.setSize(1 * times);
		JavaObject javaObject2 = new JavaObject();
		javaObject2.setName("javaObject2");
		javaObject2.setSize(2 * times);

		javaObjects[0] = javaObject1;
		javaObjects[1] = javaObject2;

		return javaObjects;
	}
}
