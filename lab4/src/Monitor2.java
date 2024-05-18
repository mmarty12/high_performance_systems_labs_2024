public class Monitor2 {
    private int a, p, e;

    public synchronized void findMax(int ai) {
        this.a = Math.max(a, ai);
    }

    public synchronized void writeP(int value) {
        this.p = value;
    }

    public synchronized void writeE(int value) {
        this.e = value;
    }

    public synchronized int copyA() {
        return this.a;
    }

    public synchronized int copyP() {
        return this.p;
    }

    public synchronized int copyE() {
        return this.e;
    }
}
