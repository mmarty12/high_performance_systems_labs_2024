//Дисципліна "Програмне забезпечення високопродуктивних комп’ютерних систем"
//Лабораторна робота ЛР2 Варіант 16
//A = p * (sort(d * B + Z * MM) * (MX * MT)) + (B * Z) * Z
//Мартинюк Марія Павлівна
//Дата 24.03.2024

import java.util.concurrent.Semaphore;

public class Lab2 {
    static int N = 8;
    static int P = 4;
    static int H = N/P;


    public static void main(String[] args) throws InterruptedException {
        double start, end, totalTime;
        start = System.currentTimeMillis();


        Semaphore sem1 = new Semaphore(0);
        Semaphore sem2 = new Semaphore(0);
        Semaphore sem3 = new Semaphore(0);
        Semaphore sem4 = new Semaphore(0);
        Semaphore sem6 = new Semaphore(0);
        Semaphore sem7 = new Semaphore(0);
        Semaphore sem8 = new Semaphore(0);


        T1 T1 = new T1(N, H, sem1, sem3, sem4, sem6);
        T2 T2 = new T2(H, sem1, sem4, sem7);
        T3 T3 = new T3(H, sem2, sem3, sem4, sem8);
        T4 T4 = new T4(N, H, sem2, sem4, sem6, sem7, sem8);


        //threads start
        T1.start();
        T2.start();
        T3.start();
        T4.start();


        try {
            T1.join();
            T2.join();
            T3.join();
            T4.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


        end = System.currentTimeMillis();;
        totalTime = end - start;


        System.out.println("TIME " + totalTime + "ms");
    }
}
