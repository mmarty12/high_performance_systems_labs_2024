public class T4 extends Thread {
    private Data data;
    private int a4, p4, e4;
    private int threadIndex = 4;
    static int[] Xh;

    public T4(Data data) {
        this.data = data;
    }

    public void inputAndCalculate() {
        // Введення B, p
        data.B = data.vectorInput(data.N);
        data.monitor2.writeP(1);

        // Сигнал задачі Т1, Т2, Т3 про введення B, p
        data.monitor1.signalIn();

        // Чекати на введення e у задачі Т1; C, MA у задачі Т2; R, MD у задачі Т3
        data.monitor1.waitIn();

        // Обчислення1 a4 = max(C * (MA * MDн))
        int startIndex = (threadIndex * data.H) - data.H;
        int[][] matrixMultiply = data.multiplyMatricesPartialResult(data.MA, data.MD, startIndex, startIndex + data.H);
        int[] matrixVectorMultiply = data.multiplyMatrixVector(matrixMultiply, data.C);
        a4 = data.findMaxValue(matrixVectorMultiply);

        // Обчислення2 a = a + a4
        data.monitor2.findMax(a4); // КД1

        // Сигнал Т1, Т2, Т3 про завершення обчислень а
        data.monitor1.signalCalcMax();

        // Чекати на завершення обчислень а в Т1, Т2, T3
        data.monitor1.waitCalcMax();

        // Копія а4 = а
        a4 = data.monitor2.copyA(); // КД2

        // Копія p4 = p
        p4 = data.monitor2.copyP(); // КД3

        // Копія e4 = e
        e4 = data.monitor2.copyE(); // КД4

        // Обчислення3 Хн = p4 * a4 * Rн + e4 * Bн
        int[] Rh = new int[data.H];
        System.arraycopy(data.R, startIndex, Rh, 0, data.H);
        int[] Bh = new int[data.H];
        System.arraycopy(data.B, startIndex, Bh, 0, data.H);
        Xh = data.calculateXh(p4, a4, e4, Rh, Bh);

        // Сигнал Т1 про завершення обчислень Хн
        data.monitor1.signalCalcXh();
    }

    @Override
    public void run(){
        System.out.println("T4 is started");
        inputAndCalculate();
        System.out.println("T4 is finished");
    }
}
