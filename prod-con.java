import java.util.concurrent.Semaphore;

class Q {
    int item;

    static Semaphore semCon = new Semaphore(0);
    static Semaphore semProd = new Semaphore(1);

    void get(){
        try{
            semCon.acquire();
        }catch(InterruptedException e){
            System.out.println("InterruptedException caught");
        }

        System.out.println("Consumer consumed item: "+item);

        semProd.release();
    }

    void put(int item){
        try {
            semProd.acquire();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }

        this.item = item;

        System.out.println("Producer produced item : " + item);

        semCon.release();
    }
}

class Producer implements Runnable {
    Q q;
    Producer(Q q){
        this.q = q;
        new Thread(this, "Producer").start();
    }

    public void run(){
        System.out.println("Producer runnable is running...");
        for(int i=0; i<5; i++){
            System.out.println("++++++ Producing item "+i);
            q.put(i);
        }
    }
}

class Consumer implements Runnable{
    Q q;
    Consumer(Q q){
        this.q = q;
        new Thread(this, "Consumer").start();
    }

    public void run(){
        System.out.println("Consumer runnable is running...");
        for(int i=0; i<5; i++){
            System.out.println("------ Consuming item "+ i);
            q.get();
        }
    }
}

class PC {
    public static void main(String args[]){
        Q q = new Q();
        new Consumer(q);
        new Producer(q);
        
    }
}