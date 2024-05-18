import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class T2 extends Thread {
    int H;
    static int [] Xн;
    static int [][] MAн;
    Semaphore sem1, sem4;
    Semaphore sem7;
    static int [] Aн;
    int threadIndex = 2;
    private int a2;

    public T2(int H, Semaphore sem1, Semaphore sem4, Semaphore sem7) {
        this.H = H;
        this.sem1 = sem1;
        this.sem4 = sem4;
        this.sem7 = sem7;
    }

    public void calculate() {
        try {
            // Чекати на введення MM, B, MX у задачі Т1; р у задачі Т3; Z, MТ, d у задачі Т4
            try {
                Data.b1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks1
            int d2;
            Data.ks1.lock();
                d2 = T4.d; // Копія d2 = d  КД1
            Data.ks1.unlock();

            // Обчислення1 Xн = sort((d1 * Bн + Z * MMн))
            int [] Bн = new int[H];
            int startIndex = (threadIndex * H) - H;
            System.arraycopy(T1.B, startIndex, Bн, 0, H);

            Xн = Data.XнCalculate(Bн, T4.Z, T1.MM, d2, startIndex, startIndex + H);
            //System.out.println("X2 calcaulated Xn: " + Arrays.toString(Xн));

            sem1.release(); // Cигнал Т1 завершення обчислень Xn

            sem4.acquire(); // Чекати на завершення обчислень X у задачі Т1

            // Обчислення2 X2н = sortzl(Xн, Xн)
            MAн = Data.multiplyMatricesPartialResult(T1.MX, T4.MT,startIndex, startIndex + H);

            Data.sem5.acquire();
            int p2 = T3.p; // Копія p2 = p  КД2
            Data.sem5.release();

            // Обчислення3 Dн = p2 * (X * MAн)
            int [] Dн = Data.calculateDн(T1.X, MAн, p2);

            // Обчислення4 a2 = ( Bн * Zн)
            int[] Zн = new int[H];
            System.arraycopy(T4.Z, startIndex, Zн, 0, H);
            a2 = Data.vectorMultiply(Bн, Zн);

            // Обчислення5 a = a + a2
            Data.a.updateAndGet(a -> a + a2); // КД3

            try {
                Data.b2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks2
            synchronized (Data.ks2) {
                a2 = Data.a.intValue(); // Копія a2 = a  КД4
            }

            // Обчислення6 Aн = Dн + a2 * Zн
            Aн = Data.calculateAн(Dн, a2, Zн);

            sem7.release(); // Сигнал Т4 про завершення обчислень Aн

        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void run(){
        System.out.println("T2 is started");
        calculate();
        System.out.println("T2 is finished");
    }
}
