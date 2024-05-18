import java.util.Arrays;

public class Data {
    static int N = 8;
    static int P = 4;
    static int H = N/P;

    public Monitor1 monitor1 = new Monitor1();
    public Monitor2 monitor2 = new Monitor2();

    static int[] C, R, B;
    static int[][] MA, MD;

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

    //допоміжний метод для розрахунку добутку матриць
    public static int[][] multiplyMatricesPartialResult(int[][] matrix1, int[][] matrix2, int startRow, int endRow) {
        int[][] partialResult = new int[endRow - startRow][matrix2[0].length];
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix2.length; k++) {
                    partialResult[i - startRow][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return partialResult;
    }

    //допоміжний метод для розрахунку добутку матриці на вектор
    public static int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        int[] result = new int[numRows];

        for (int i = 0; i < numRows; i++) {
            int sum = 0;
            for (int j = 0; j < numCols; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }
        return result;
    }

    //допоміжний метод для пошуку максимального значення у векторі
     public static int findMaxValue(int[] vector) {
        int max = vector[0];

        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > max) {
                max = vector[i];
            }
        }
        return max;
     }

    //допоміжний метод для розрахунку добутку вектора на скаляр
    public static int[] multiplyVectorScalar(int[] vector, int scalar) {
        int length = vector.length;
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    //допоміжний метод для розрахунку суми векторів
    public static int[] addVectors(int[] vector1, int[] vector2) {
        int length = vector1.length;
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    //допоміжний метод для розрахунку Хн = pi * ai * Rн + ei * Bн
    public static int[] calculateXh(int pi, int ai, int ei, int[] R, int[] B) {
        int x = pi * ai;
        int[] resPart1 = multiplyVectorScalar(R, x);
        int[] resPart2 = multiplyVectorScalar(B, ei);
        int[] Xh = addVectors(resPart1, resPart2);
        return Xh;
    }

    //допоміжний метод для об'єднання та виводу результуючого вектора
    public static void mergeAndPrintX() {
        int totalLength = T1.Xh.length + T2.Xh.length + T3.Xh.length + T4.Xh.length;
        int[] X = new int[totalLength];
        System.arraycopy(T1.Xh, 0, X, 0, T1.Xh.length);
        System.arraycopy(T2.Xh, 0, X, T1.Xh.length, T2.Xh.length);
        System.arraycopy(T3.Xh, 0, X, T1.Xh.length + T2.Xh.length, T3.Xh.length);
        System.arraycopy(T4.Xh, 0, X, T1.Xh.length + T2.Xh.length + T3.Xh.length, T4.Xh.length);
        System.out.println("X = " + Arrays.toString(X));
    }
}
