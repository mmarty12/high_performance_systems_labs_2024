//Дисципліна "Програмне забезпечення високопродуктивних комп’ютерних систем"
//Лабораторна робота ЛР1 Варіант 13
//F1: E = A + C * (MA * ME) + B
//F2: MF = MG * (MK * ML) - MK
//F3: O = SORT(P) * (MR * MS)
//Мартинюк Марія Павлівна
//Дата 17.02.2024

public class Lab1 {
    public static void main(String[] args) throws InterruptedException {
        //variables to estimate process time
        double start, end, totalTime;

        start = System.currentTimeMillis();

        T1_F1 T1 = new T1_F1();
        T2_F2 T2 = new T2_F2();
        T3_F3 T3 = new T3_F3();

        //threads start
        T1.start();
        T2.start();
        T3.start();

        try {
            T1.join();
            T2.join();
            T3.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        end = System.currentTimeMillis();;
        totalTime = end - start;

        System.out.println("TIME " + totalTime + "ms");

    }
}
