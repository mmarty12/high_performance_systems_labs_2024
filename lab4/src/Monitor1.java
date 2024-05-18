public class Monitor1 {
    private int F1 = 0;
    private int F2 = 0;
    private int F3 = 0;

    public synchronized void signalIn() {
        F1 += 1;
        if (F1 == 4) {
            notifyAll();
        }
    }

    public synchronized void waitIn() {
        try {
            if (F1 != 4) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcMax() {
        F2 += 1;
        if (F2 == 4) {
            notifyAll();
        }
    }

    public synchronized void waitCalcMax() {
        try {
            if (F2 != 4) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcXh() {
        F3 += 1;
        if (F3 == 3) {
            notify();
        }
    }

    public synchronized void waitCalcXh() {
        try {
            if (F3 != 3) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
