package mutithreading.lab1;

public class Producer implements Runnable {
	private DoubleList list;
	Thread t;
	
	Producer(String name, DoubleList list){
		this.list = list;
		t = new Thread(this, name);
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 10; i++) {
			list.add(new Ammunition(2 * i, i));
			
			/*System.out.println("Thread " + t.getName() +
					" successfully put element " + i +" to the list\n");*/
		}
	}

}
