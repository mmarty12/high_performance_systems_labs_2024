import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Data {
    //formula 1 for T1
    public static int[] F1(int[] A, int[] B, int[] C, int[][] MA, int[][] ME) {
        //E = A + C * (MA * ME) + B

        //Step 1: MA * ME
        int[][] resultMatrixMultiply = multiplyMatrices(MA, ME);

        //Step 2: C * (MA * ME)
        int[] intermediateResult = multiplyMatrixWithVector(resultMatrixMultiply, C);

        //Step 3: E = A + C * (MA * ME) + B
        int[] E = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            E[i] = A[i] + intermediateResult[i] + B[i];
        }
        return E;
    }

    //formula 2 for T2
    public static int[][] F2(int[][] MK, int[][] ML, int[][] MG) {
        //MF = MG * (MK * ML) - MK

        //Step 1: MK * ML
        int[][] resultMatrixMultiply = multiplyMatrices(MK, ML);

        //Step 2: MG * (MK * ML)
        int[][] resultMatrixMultiply2 = multiplyMatrices(MG, resultMatrixMultiply);

        //Step 3: MF = MG * (MK * ML) - MK
        int[][] MF = new int[MG.length][MG[0].length];
        for (int i = 0; i < MG.length; i++) {
            for (int j = 0; j < MG[0].length; j++) {
                MF[i][j] = resultMatrixMultiply2[i][j] - MK[i][j];
            }
        }
        return MF;
    }

    //formula 3 for T3
    public static int[] F3(int[] P, int[][] MR, int[][] MS) {
        //O = SORT(P) * (MR * MS)

        //Step 1: MR * MS
        int[][] resultMatrixMultiply = multiplyMatrices(MR, MS);

        //Step 2: SORT(P)
        int[] SortP = Arrays.copyOf(P, P.length);
        Arrays.sort(SortP);

        //Step 3: O = SORT(P) * (MR * MS)
        int[] O = multiplyMatrixWithVector(resultMatrixMultiply, SortP);
        return O;
    }

    //helping method to multiply matrices
    private static int[][] multiplyMatrices(int[][] matrixOne, int[][] matrixTwo) {
        int rowsMatrixOne = matrixOne.length;
        int colsMatrixOne = matrixOne[0].length;
        int colsMatrixTwo = matrixTwo[0].length;

        int[][] result = new int[rowsMatrixOne][colsMatrixTwo];

        for (int i = 0; i < rowsMatrixOne; i++) {
            for (int j = 0; j < colsMatrixTwo; j++) {
                for (int k = 0; k < colsMatrixOne; k++) {
                    result[i][j] += matrixOne[i][k] * matrixTwo[k][j];
                }
            }
        }
        return result;
    }

    //helping method to multiply matrix with vector
    private static int[] multiplyMatrixWithVector(int[][] matrix, int[] vector) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[] result = new int[rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    //helping method to input vector values via keyboard
    public static int[] vectorInput(int N, Scanner scan) {
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = scan.nextInt();
        }
        return vector;
    }

    //helping method to input matrix values via keyboard
    public static int[][] matrixInput(int N, Scanner scan) {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = scan.nextInt();
            }
        }
        return matrix;
    }

    //helping method to write and read a file for matrix elements
    public static int[][] matrixCreate(int N, boolean generateRandom, Scanner scan, int inputNumberParam) {
        String filename = scan.nextLine();
        int[][] matrix = new int[N][N];

        //write
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    int value = generateRandom ? new Random().nextInt(10) : inputNumberParam;
                    writer.write(value + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        //read
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < values.length; col++) {
                    matrix[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            return matrix;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //helping method to write and read a file for vector elements
    public static int[] vectorCreate(int N, boolean generateRandom, Scanner scan, int inputNumberParam) {
        String filename = scan.nextLine();
        int[] vector = new int[N];

        //write
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < vector.length; i++) {
                int value = generateRandom ? new Random().nextInt(10) : inputNumberParam;
                writer.write(value + " ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        //read
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line != null) {
                String[] values = line.trim().split("\\s+");
                vector = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    vector[i] = Integer.parseInt(values[i]);
                }
                return vector;
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return vector;
    }

    //helping method to write a file for the final result (both vector and matrix)
    public static <T> void writeResultToFile(T res, Scanner scan) {
        String filename = scan.nextLine();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            if (res instanceof int[]) {
                for (int value : (int[]) res) {
                    writer.write(value + " ");
                }
            } else if (res instanceof int[][]) {
                for (int[] row : (int[][]) res) {
                    for (int value : row) {
                        writer.write(value + " ");
                    }
                    writer.newLine();
                }
            } else {
                throw new IllegalArgumentException("Unsupported data type");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}