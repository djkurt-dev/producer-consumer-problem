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
            System.out.println("+++ consumerSemaphore.acquire()");
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            System.out.println("Exception: " + e);
        }

        System.out.println("Consumer consumed item: " + item);

        producerSemaphore.release();
        System.out.println("--- producerSemaphore.release()");
        
    }

    public void put(int item){
        try{
            producerSemaphore.acquire();
            System.out.println("+++ producerSemaphore.acquire()");
            Thread.sleep(1000);
        } catch(InterruptedException e ){
            System.out.println("Exception: " + e);
        }

        this.item = item;
        System.out.println("Producer produced item: " + item);

        consumerSemaphore.release();
        System.out.println("--- consumerSemaphore.release()");
    }
}

class Consumer implements Runnable {
    private CustomQueue myQueue;
    Consumer(CustomQueue myQueue) {
		this.myQueue = myQueue;
	}
    public void run(){
        int i=0;
        while(i < 5){
            myQueue.get();
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println("Exception: " + e);
            }
            i++;
        }
    }
}

class Producer implements Runnable {
	private CustomQueue myQueue;

	Producer(CustomQueue myQueue) {
		this.myQueue = myQueue;
	}

	public void run() {
        int i=0;
		while (i < 5) {
			Random random = new Random();
			int data = random.nextInt(100);
			// producer put items
			myQueue.put(data);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            i++;
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
        producerThread.start();
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
	}

}