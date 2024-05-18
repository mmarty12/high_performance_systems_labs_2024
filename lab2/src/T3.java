import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class T3 extends Thread {
    int H;
    static int p;
    static int [] X2н;
    static int [][] MAн;
    Semaphore sem2, sem3, sem4;
    Semaphore sem8;
    static int [] Aн;
    int threadIndex = 3;
    private int a3;

    public T3(int H, Semaphore sem2, Semaphore sem3, Semaphore sem4, Semaphore sem8) {
        this.H = H;
        this.sem2 = sem2;
        this.sem3 = sem3;
        this.sem4 = sem4;
        this.sem8 = sem8;
    }

    public void inputAndCalculate() {
        try {
            // Введення р
            p = 1;

            try {
                Data.b1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks1
            int d3;
            Data.ks1.lock();
                d3 = T4.d; // Копія d3 = d  КД1
            Data.ks1.unlock();

            // Обчислення1 Хн = sort((d3 * Bн + Z * MMн)
            int [] Bн = new int[H];
            int startIndex = (threadIndex * H) - H;
            System.arraycopy(T1.B, startIndex, Bн, 0, H);

            int [] Xн = Data.XнCalculate(Bн, T4.Z, T1.MM, d3, startIndex, startIndex + H);
            //System.out.println("X3 calcaulated Xn: " + Arrays.toString(Xн));

            sem2.acquire(); // Чекати на завершення обчислень Xn у задачі Т4

            // Обчислення2 X2н = sortzl(Xн, Xн)
            X2н = new int[Xн.length + T4.Xн.length];
            System.arraycopy(Xн, 0, X2н, 0, Xн.length);
            System.arraycopy(T4.Xн, 0, X2н, Xн.length, T4.Xн.length);
            Arrays.sort(X2н);

            sem3.release(); // Cигнал Т1 про завершення обчислень X2n

            sem4.acquire(); // Чекати на завершення обчислень X у задачі Т1

            // Обчислення3 MAн = MX * MTн
            MAн = Data.multiplyMatricesPartialResult(T1.MX, T4.MT,startIndex, startIndex + H);

            Data.sem5.acquire();
            int p3 = T3.p; // Копія p3 = p  КД2
            Data.sem5.release();

            // Обчислення4 Dн = p3 * (X * MAн)
            int[] Dн = Data.calculateDн(T1.X, MAн, p3);

            // Обчислення5 a3 = ( Bн * Zн)
            int[] Zн;
            Zн = new int[H];
            System.arraycopy(T4.Z, startIndex, Zн, 0, H);
            a3 = Data.vectorMultiply(Bн, Zн);

            // Обчислення6 a = a + a3
            Data.a.updateAndGet(a -> a + a3); // КД3

            try {
                Data.b2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks2
            synchronized (Data.ks2) {
                a3 = Data.a.intValue(); // Копія a3 = a  КД4
            }

            // Обчислення7 Aн = Dн + a3 * Zн
            Aн = Data.calculateAн(Dн, a3, Zн);

            sem8.release(); // Сигнал Т4 про завершення обчислень Aн

        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("T3 is started");
        inputAndCalculate();
        System.out.println("T3 is finished");
    }
}
