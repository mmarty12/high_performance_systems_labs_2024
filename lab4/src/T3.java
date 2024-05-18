public class T3 extends Thread {
    private Data data;
    private int a3, p3, e3;
    private int threadIndex = 3;
    static int[] Xh;

    public T3(Data data) {
        this.data = data;
    }

    public void inputAndCalculate() {
        // Введення R, MD
        data.R = data.vectorInput(data.N);
        data.MD = data.matrixInput(data.N);

        // Сигнал задачі Т1, Т2, Т4 про введення R, MD
        data.monitor1.signalIn();

        // Чекати на введення e у задачі Т1; C, MA у задачі Т2; B, p у задачі Т4
        data.monitor1.waitIn();

        // Обчислення1 a3 = max(C * (MA * MDн))
        int startIndex = (threadIndex * data.H) - data.H;
        int[][] matrixMultiply = data.multiplyMatricesPartialResult(data.MA, data.MD, startIndex, startIndex + data.H);
        int[] matrixVectorMultiply = data.multiplyMatrixVector(matrixMultiply, data.C);
        a3 = data.findMaxValue(matrixVectorMultiply);

        // Обчислення2 a = max(a, a3)
        data.monitor2.findMax(a3); // КД1

        // Сигнал Т1, Т2, Т4 про завершення обчислень а
        data.monitor1.signalCalcMax();

        // Чекати на завершення обчислень а в Т1, Т2, T4
        data.monitor1.waitCalcMax();

        // Копія а3 = а
        a3 = data.monitor2.copyA(); // КД2

        // Копія p3 = p
        p3 = data.monitor2.copyP(); // КД3

        // Копія e3 = e
        e3 = data.monitor2.copyE(); // КД4

        // Обчислення3 Хн = p3 * a3 * Rн + e3 * Bн
        int[] Rh = new int[data.H];
        System.arraycopy(data.R, startIndex, Rh, 0, data.H);
        int[] Bh = new int[data.H];
        System.arraycopy(data.B, startIndex, Bh, 0, data.H);
        Xh = data.calculateXh(p3, a3, e3, Rh, Bh);

        // Сигнал Т1 про завершення обчислень Хн
        data.monitor1.signalCalcXh();
    }

    @Override
    public void run(){
        System.out.println("T3 is started");
        inputAndCalculate();
        System.out.println("T3 is finished");
    }
}
