import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Data {
    static Semaphore sem5 = new Semaphore(1);
    static CyclicBarrier b1 = new CyclicBarrier(4);
    static CyclicBarrier b2 = new CyclicBarrier(4);
    static final Object ks2 = new Object();
    static final ReentrantLock ks1 = new ReentrantLock();
    static AtomicInteger a = new AtomicInteger(0);

    //допоміжний метод для заповнення вектору одиницями
    public static int[] vectorInput(int N) {
        int[] vector = new int[N];
        Arrays.fill(vector, 1);
        return vector;
    }

    //допоміжний метод для заповнення матриці одиницями
    public static int[][] matrixInput(int N) {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = 1;
            }
        }
        return matrix;
    }

    //допоміжний метод для розрахунку Хн = sort((d * Bн + Z * MMн)
    public static int[] XнCalculate(int[] Bн, int[] Z, int[][] MMн, int d, int startRow, int endRow) {
        int[][] MMpart = new int[endRow - startRow][MMн[0].length];
        for (int i = startRow; i < endRow; i++) {
            MMpart[i - startRow] = Arrays.copyOf(MMн[i], MMн[i].length);
        }

        int[] result = new int[Bн.length];

        for (int i = 0; i < Bн.length; i++) {
            result[i] = d * Bн[i];
        }

        for (int i = 0; i < MMpart.length; i++) {
            int rowSum = 0;
            for (int j = 0; j < Z.length; j++) {
                rowSum += Z[j] * MMpart[i][j];
            }
            result[i] += rowSum;
        }

        Arrays.sort(result);
        return result;
    }

    //допоміжний метод для розрахунку MAн = MX * MTн
    public static int[][] multiplyMatricesPartialResult(int[][] MX, int[][] MTн, int startRow, int endRow) {
        int[][] partialResult = new int[endRow - startRow][MTн[0].length];
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < MTн[0].length; j++) {
                for (int k = 0; k < MTн.length; k++) {
                    partialResult[i - startRow][j] += MX[i][k] * MTн[k][j];
                }
            }
        }
        return partialResult;
    }

    //допоміжний метод для розрахунку  Dн = p * (X * MAн)
    public static int[] calculateDн(int[] X, int[][] MAн, int p) {
        int[] Dн = new int[MAн.length];

        for (int i = 0; i < X.length; i++) {
            X[i] *= p;
        }
        for (int i = 0; i < MAн.length; i++) {
            for (int j = 0; j < X.length; j++) {
                Dн[i] += X[j] * MAн[i][j];
            }
        }
        return Dн;
    }

    // допоміжний метод для розрахунку ai = Bн * Zн
    public static int vectorMultiply(int[] Bн, int[] Zн) {
        int result = 0;

        for (int i = 0; i < Bн.length; i++) {
            result += Bн[i] * Zн[i];
        }

        return result;
    }

    // допоміжний метод для розрахунку Aн = Dн + (a * Z)н
    public static int[] calculateAн(int[] Dн, int a, int[] Zн) {
        int[] Aн = new int[Dн.length];

        for (int i = 0; i < Zн.length; i++) {
            Zн[i] *= a;
        }

        for (int i = 0; i < Dн.length; i++) {
            Aн[i] = Dн[i] + Zн[i];
        }
        return Aн;
    }
}
