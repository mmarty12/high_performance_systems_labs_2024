import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

public class T4 extends Thread {
    public static int [] Xн;
    int N, H;
    static int[] Z;
    static int[][] MT;
    static int d;
    Semaphore sem2, sem4;
    Semaphore sem6, sem7, sem8;
    static int[][] MAн;
    int threadIndex = 4;
    private int a4;

    public T4(int N, int H, Semaphore sem2, Semaphore sem4, Semaphore sem6, Semaphore sem7, Semaphore sem8) {
        this.N = N;
        this.H = H;
        this.sem2 = sem2;
        this.sem4 = sem4;
        this.sem6 = sem6;
        this.sem7 = sem7;
        this.sem8 = sem8;
    }

    public void inputAndCalculate() {
        try {
            // Введення A, Z, MТ, d
            d = 1;
            Z = Data.vectorInput(N);
            MT = Data.matrixInput(N);

            // Чекати на введення MM, B, MX у задачі Т1; р у задачі Т3
            try {
                Data.b1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks1
            int d4;
            Data.ks1.lock();
                d4 = d; // Копія d4 = d  КД1
            Data.ks1.unlock();

            // Обчислення Xн = sort((d1 * Bн + Z * MMн))
            int [] Bн = new int[H];
            int startIndex = (threadIndex * H) - H;
            System.arraycopy(T1.B, startIndex, Bн, 0, H);

            Xн = Data.XнCalculate(Bн, T4.Z, T1.MM, d4, startIndex, startIndex + H);
            //System.out.println("X4 calcaulated Xn: " + Arrays.toString(Xн));

            sem2.release(); // Cигнал Т3 завершення обчислень Xn

            sem4.acquire(); // Чекати на завершення обчислень X у задачі Т1

            // Обчислення2 MAн = MX * MTн
            MAн = Data.multiplyMatricesPartialResult(T1.MX, T4.MT,startIndex, startIndex + H);

            Data.sem5.acquire();
            int p4 = T3.p; // Копія p4 = p  КД2
            Data.sem5.release();

            // Обчислення3 Dн = p4 * (X * MAн)
            int [] Dн = Data.calculateDн(T1.X, MAн, p4);

            // Обчислення4 a4 = ( Bн * Zн)
            int[] Zн = new int[H];
            System.arraycopy(T4.Z, startIndex, Zн, 0, H);
            a4 = Data.vectorMultiply(Bн, Zн);

            // Обчислення5 a = a + a4
            Data.a.updateAndGet(a -> a + a4); // КД3

            try {
                Data.b2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks2
            synchronized (Data.ks2) {
                a4 = Data.a.intValue(); // Копія a4 = a  КД4
            }

            // Обчислення6 Aн = Dн + a_4 * Zн
            int [] Aн = Data.calculateAн(Dн, a4, Zн);

            sem6.acquire(); // Чекати на завершення обчислень Aн у задачі Т1
            sem7.acquire(); // Чекати на завершення обчислень Aн у задачі Т2
            sem8.acquire(); // Чекати на завершення обчислень Aн у задачі Т3

            // Вивід Ан
            int totalLength = T1.Aн.length + T2.Aн.length + T3.Aн.length + Aн.length;
            int[] A = new int[totalLength];
            System.arraycopy(T1.Aн, 0, A, 0, T1.Aн.length);
            System.arraycopy(T2.Aн, 0, A, T1.Aн.length, T2.Aн.length);
            System.arraycopy(T3.Aн, 0, A, T1.Aн.length + T2.Aн.length, T3.Aн.length);
            System.arraycopy(Aн, 0, A, T1.Aн.length + T2.Aн.length + T3.Aн.length, Aн.length);

            System.out.println("A = " + Arrays.toString(A));

        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("T4 is started");
        inputAndCalculate();
        System.out.println("T4 is finished");
    }
}
