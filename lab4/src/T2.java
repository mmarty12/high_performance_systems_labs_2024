import java.util.Arrays;

public class T2 extends Thread {
    private Data data;
    private int a2, p2, e2;
    private int threadIndex = 2;
    static int[] Xh;

    public T2(Data data) {
        this.data = data;
    }

    public void inputAndCalculate() {
        // Введення C, MA
        data.C = data.vectorInput(data.N);
        data.MA = data.matrixInput(data.N);

        // Сигнал задачі Т1, Т3, Т4 про введення С, MA
        data.monitor1.signalIn();

        // Чекати на введення e у задачі Т1; R, MD у задачі Т3; B, p у задачі Т4
        data.monitor1.waitIn();

        // Обчислення1 a2 = max(C * (MA * MDн))
        int startIndex = (threadIndex * data.H) - data.H;
        int[][] matrixMultiply = data.multiplyMatricesPartialResult(data.MA, data.MD, startIndex, startIndex + data.H);
        int[] matrixVectorMultiply = data.multiplyMatrixVector(matrixMultiply, data.C);

        matrixVectorMultiply[0] = 2; // Встановлення відмінного від інших значення елемента для перевірки функції max
        a2 = data.findMaxValue(matrixVectorMultiply);

        System.out.println("C * (MA * MDн) = " + Arrays.toString(matrixVectorMultiply));
        System.out.println("a2 = " + a2);

        // Обчислення2 a = max(a, a2)
        data.monitor2.findMax(a2); // КД1

        // Сигнал Т1, Т3, Т4 про завершення обчислень а
        data.monitor1.signalCalcMax();

        // Чекати на завершення обчислень а в Т1, Т3, T4
        data.monitor1.waitCalcMax();

        // Копія а2 = а
        a2 = data.monitor2.copyA(); // КД2

        // Копія p2 = p
        p2 = data.monitor2.copyP(); // КД3

        // Копія e2 = e
        e2 = data.monitor2.copyE(); // КД4

        // Обчислення3 Хн = p2 * a2 * Rн + e2 * Bн
        int[] Rh = new int[data.H];
        System.arraycopy(data.R, startIndex, Rh, 0, data.H);
        int[] Bh = new int[data.H];
        System.arraycopy(data.B, startIndex, Bh, 0, data.H);
        Xh = data.calculateXh(p2, a2, e2, Rh, Bh);

        // Сигнал Т1 про завершення обчислень Хн
        data.monitor1.signalCalcXh();

    }

    @Override
    public void run(){
        System.out.println("T2 is started");
        inputAndCalculate();
        System.out.println("T2 is finished");
    }
}
