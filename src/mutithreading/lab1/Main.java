package mutithreading.lab1;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DoubleList list = new DoubleList();
		
		Consumer con = new Consumer("Consumer", list);
		Producer prod = new Producer("Producer", list);
		
		con.t.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Caught Interrupted Exeption while sleep in main thred");
		}
		
		prod.t.start();
		
	}

}
