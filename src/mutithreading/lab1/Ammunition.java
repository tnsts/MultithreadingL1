package mutithreading.lab1;

public class Ammunition {
	private int price;
	private int weight;
	
	public Ammunition(){
		this(0, 0);
	}
	
	public Ammunition(int some) {
		this(some, some);
	}
	
	public Ammunition(int price, int weight){
		this.price = price;
		this.weight = weight;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void showFeatures() {
		System.out.println("Price: " + price);
		System.out.println("Weight: " + weight);
	}
}
