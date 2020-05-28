package mutithreading.lab1;

public class Consumer implements Runnable {
	private DoubleList list;
	Thread t;
	
	Consumer(String name, DoubleList list){
		this.list = list;
		t = new Thread(this, name);
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 10; i++) {
			/*System.out.println("Thread " + t.getName() + 
					" is trying to get element " + i + " from the list");*/
			
			list.get(i);
			
			/*System.out.println("Thread " + t.getName() + 
					" successfully got element " + i + " from the list\n");*/
		}
	}

}
