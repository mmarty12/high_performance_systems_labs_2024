public class T1 extends Thread {
    private Data data;
    private int a1, p1, e1;
    private int threadIndex = 1;
    static int[] Xh;

    public T1(Data data) {
        this.data = data;
    }

    public void inputAndCalculate() {
        // Введення e
        data.monitor2.writeE(1);

        //Сигнал задачі Т2, Т3, Т4 про введення e
        data.monitor1.signalIn();

        // Чекати на введення C, MA у задачі Т2; R, MD у задачі Т3; B, p у задачі Т4
        data.monitor1.waitIn();

        // Обчислення1 a1 = max(C * (MA * MDн))
        int startIndex = (threadIndex * data.H) - data.H;
        int[][] matrixMultiply = data.multiplyMatricesPartialResult(data.MA, data.MD, startIndex, startIndex + data.H);
        int[] matrixVectorMultiply = data.multiplyMatrixVector(matrixMultiply, data.C);
        a1 = data.findMaxValue(matrixVectorMultiply);

        // Обчислення2 a = max(a, a1)
        data.monitor2.findMax(a1); // КД1

        // Сигнал Т2, Т3, Т4 про завершення обчислень а
        data.monitor1.signalCalcMax();

        // Чекати на завершення обчислень а в Т2, Т3, T4
        data.monitor1.waitCalcMax();

        // Копія а1 = а
        a1 = data.monitor2.copyA(); // КД2

        // Копія p1 = p
        p1 = data.monitor2.copyP(); // КД3

        // Копія e1 = e
        e1 = data.monitor2.copyE(); // КД4

        // Обчислення3 Хн = p1 * a1 * Rн + e1 * Bн
        int[] Rh = new int[data.H];
        System.arraycopy(data.R, startIndex, Rh, 0, data.H);
        int[] Bh = new int[data.H];
        System.arraycopy(data.B, startIndex, Bh, 0, data.H);
        Xh = data.calculateXh(p1, a1, e1, Rh, Bh);

        // Чекати на завершення обчислень Хн в Т2, Т3, T4
        data.monitor1.waitCalcXh();

        // Вивід Х
        data.mergeAndPrintX();

    }

    @Override
    public void run(){
        System.out.println("T1 is started");
        inputAndCalculate();
        System.out.println("T1 is finished");
    }
}
