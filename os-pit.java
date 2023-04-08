import java.util.Random;
import java.util.concurrent.Semaphore;

class CustomQueue {
    private int item;

    private Semaphore consumerSemaphore = new Semaphore(0);
    
    // 1 to make sure producer runs first
    private Semaphore producerSemaphore = new Semaphore(1); 

    public void get(){
        try{
            consumerSemaphore.acquire();
        } catch(InterruptedException e) {
            System.out.println("Exception: " + e);
        }

        System.out.println("Consumed item: " + item);

        producerSemaphore.release();
    }

    public void put(int item){
        try{
            producerSemaphore.acquire();
        } catch(InterruptedException e ){
            System.out.println("Exception: " + e);
        }

        this.item = item;
        System.out.println("Produced item: " + item);
        consumerSemaphore.release();
    }
}

class Consumer implements Runnable {
    private CustomQueue myQueue;
    Consumer(CustomQueue myQueue) {
		this.myQueue = myQueue;
	}
    public void run(){
        while(true){
            myQueue.get();
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println("Exception: " + e);
            }
        }
    }
}

class Producer implements Runnable {
	private CustomQueue myQueue;

	Producer(CustomQueue myQueue) {
		this.myQueue = myQueue;
	}

	public void run() {
		while (true) {
			Random random = new Random();
			int data = random.nextInt(100);
			// producer put items
			myQueue.put(data);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ProducerAndConsumerTest {

	public static void main(String[] args) {
		// creating buffer queue
        CustomQueue myQueue = new CustomQueue();
       
        Producer producer = new Producer(myQueue);
        Consumer consumer = new Consumer(myQueue);
        Thread producerThread = new Thread(producer);
        
        // starting producer thread
        producerThread.start();
        
        Thread consumerThread = new Thread(consumer);
        // starting consumer thread
        consumerThread.start();
	}

}