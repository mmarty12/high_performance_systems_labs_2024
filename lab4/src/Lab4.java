//Дисципліна "Програмне забезпечення високопродуктивних комп’ютерних систем"
//Лабораторна робота ЛР4 Варіант 8
//X = p * max(C * (MA * MD)) * R + e * B
//Мартинюк Марія Павлівна
//Дата 26.04.2024

public class Lab4 {
    public static void main(String []args) {
        double start, end, totalTime;
        start = System.currentTimeMillis();

        Data data = new Data();

        T1 T1 = new T1(data);
        T2 T2 = new T2(data);
        T3 T3 = new T3(data);
        T4 T4 = new T4(data);

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

        end = System.currentTimeMillis();
        totalTime = end - start;

        System.out.println("TIME " + totalTime + "ms");
    }
}
