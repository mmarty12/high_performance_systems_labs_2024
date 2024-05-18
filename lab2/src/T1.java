import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
    int N, H;
    static int [] B;
    static int [][] MM;
    static int [][] MX;
    Semaphore sem1, sem3, sem4;
    Semaphore sem6;
    static int[] X, Aн;
    int threadIndex = 1;
    private int a1;

    public T1(int N, int H, Semaphore sem1, Semaphore sem3, Semaphore sem4, Semaphore sem6) {
        this.N = N;
        this.H = H;
        this.sem1 = sem1;
        this.sem3 = sem3;
        this.sem4 = sem4;
        this.sem6 = sem6;
    }

    public void inputAndCalculate() {
        try {
            // Введення MM, B, MX
            B = Data.vectorInput(N);
            MM = Data.matrixInput(N);
/*                MM = new int[N][N];
                for (int i = 0; i < MM.length; i++) {
                    for (int j = 0; j < MM[i].length; j++) {
                        if (i == 1) {
                            MM[i][j] = 2; // Set the second column to 2
                        } else {
                            MM[i][j] = 1; // Set other elements to 1
                        }
                    }
                }*/
            MX = Data.matrixInput(N);

            // Чекати на введення р у задачі Т3; Z, MТ, d у задачі Т4
            try {
                Data.b1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks1
            int d1;
            Data.ks1.lock();
                d1 = T4.d;// Копія d1 = d  КД1
            Data.ks1.unlock();

            // Обчислення1 Xн = sort((d1 * Bн + Z * MMн))
            int [] Bн = new int[H];
            int startIndex = (threadIndex * H) - H;
            System.arraycopy(T1.B, startIndex, Bн, 0, H);

            int [] Xн = Data.XнCalculate(Bн, T4.Z, MM, d1, startIndex, startIndex + H);
            //System.out.println("X1 calcaulated Xn: " + Arrays.toString(Xн));

            sem1.acquire(); // Чекати на завершення обчислень Xn у задачі Т2

            // Обчислення2 X2н = sortzl(Xн, Xн)
            int[] X2н = new int[Xн.length + T2.Xн.length];
            System.arraycopy(Xн, 0, X2н, 0, Xн.length);
            System.arraycopy(T2.Xн, 0, X2н, Xн.length, T2.Xн.length);
            Arrays.sort(X2н);

            sem3.acquire(); // Чекати на завершення обчислень X2n у задачі Т3

            // Обчислення3 Х = sortzl(X2н, Xн)
            X = new int[X2н.length + T3.X2н.length];
            System.arraycopy(X2н, 0, X, 0, X2н.length);
            System.arraycopy(T3.X2н, 0, X, X2н.length, T3.X2н.length);
            Arrays.sort(X);

            sem4.release(3); // Cигнал Т2, T3, T4 про завершення обчислень X

            // Обчислення4 MAн = MX * MTн
            int [][] MAн = Data.multiplyMatricesPartialResult(MX, T4.MT,startIndex, startIndex + H);

            Data.sem5.acquire();
            int p1 = T3.p; // Копія p1 = p  КД2
            Data.sem5.release();

            // Обчислення5 Dн = p1 * (X * MAн)
            int [] Dн = Data.calculateDн(X, MAн, p1);

            int [] Zн;

            // Обчислення6 a1 = ( Bн * Zн)
            Zн = new int[H];
            System.arraycopy(T4.Z, startIndex, Zн, 0, H);
            a1 = Data.vectorMultiply(Bн, Zн);

            // Обчислення7 a = a + a1
            Data.a.updateAndGet(a -> a + a1); // КД3

            try {
                Data.b2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            // ks2
            synchronized (Data.ks2) {
                a1 = Data.a.intValue(); // Копія a1 = a  КД4
            }

            // Обчислення8 Aн = Dн + a1 * Zн
            Aн = Data.calculateAн(Dн, a1, Zн);

            sem6.release(); // Сигнал Т4 про завершення обчислень Aн

        } catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    @Override
    public void run(){
        System.out.println("T1 is started");
        inputAndCalculate();
        System.out.println("T1 is finished");
    }
}
