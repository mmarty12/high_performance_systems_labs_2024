using System.Text;

namespace lab3 {
public class Data {
    public static Mutex m1 = new Mutex();
    public static Barrier b1 = new Barrier(4);
    public static EventWaitHandle e1 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public static EventWaitHandle e2 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public static EventWaitHandle e3 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public static EventWaitHandle e4 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public static Semaphore sem1 = new Semaphore(0, 3);
    public static Semaphore sem2 = new Semaphore(0, 3);
    public static Semaphore sem3 = new Semaphore(0, 3);

    public static object cs1 = new object();
    public static int a;

    // Helper method to fill a vector with ones
    public static int[] VectorInput(int N) {
        int[] vector = new int[N];
        Array.Fill(vector, 1);
        return vector;
    }

    // Helper method to fill a matrix with ones
    public static int[,] MatrixInput(int N) {
        int[,] result = new int[N,N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i,j] = 1;
            }
        }
        return result;
    }

    // Helper method to calculate matrix multiplication
    public static int[,] MultiplyMatricesPartialResult(int[,] MX, int[,] MTн, int startRow, int endRow) {
        int rows = endRow - startRow;
        int cols = MTн.GetLength(1);
        int[,] partialResult = new int[rows, cols];
    
        for (int i = startRow; i < endRow; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                for (int k = 0; k < MTн.GetLength(0); k++)
                {
                    partialResult[i - startRow, j] += MX[i, k] * MTн[k, j];
                }
            }
        }
    
        return partialResult;
    }

    // Helper method to calculate ai = Bн * Zн
    public static int VectorMultiply(int[] Bн, int[] Zн) {
        return Bн.Zip(Zн, (b, z) => b * z).Sum();
    }

    // Helper method to multiply a matrix by a scalar
    public static int[,] MatrixByScalarMultiply(int[,] matrix, int scalar) {
        int rows = matrix.GetLength(0);
        int cols = matrix.GetLength(1);

        int[,] result = new int[rows, cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                result[i, j] = matrix[i, j] * scalar;
            }
        }

        return result;
    }

    //  Helper method to calculate matrices addition
        public static int[,] MatricesAdd(int[,] matrix1, int[,] matrix2) {
        int rows = matrix1.GetLength(0);
        int cols = matrix1.GetLength(1);

        int[,] result = new int[rows, cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                result[i,j] = matrix1[i, j] + matrix2[i, j];
            }
        }

        return result;
    }

    // Helper method to calculate MBн  = a * (MZ * MMн) * р
    public static int[,] CalculateMBн(int[,] MZ, int[,] MMн, int startRow, int endRow, int a, int p) {
        int[,] MZ_MMн = MultiplyMatricesPartialResult(MZ, MMн, startRow, endRow); // MZ * MMн
        int[,] a_MBн = MatrixByScalarMultiply(MZ_MMн, a); // a * (MZ * MMн)
        int[,] MBн = MatrixByScalarMultiply(a_MBн, p); // a * (MZ * MMн) * р

        return MBн;
    }

    // Helper method to calculate MA = (MR * MCн) * d + MBн
    public static int[,] CalculateMA(int[,] MR, int[,] MC, int startRow, int endRow, int d, int[,] MBн) {
        int[,] MR_MCн = MultiplyMatricesPartialResult(MR, MC, startRow, endRow); // MR * MCн
        int[,] d_MR_MCн = MatrixByScalarMultiply(MR_MCн, d); // (MR * MCн) * d
        int[,] MA = MatricesAdd(d_MR_MCн, MBн); // (MR * MCн) * d + MBн

        return MA;
    }

    // Method to copy a part of a matrix to the final matrix MA
    public static void CopyToFinalMatrixMA(int[,] sourceMatrix, int[,] destinationMatrix, int startRow, int startCol)
    {
        int rows = sourceMatrix.GetLength(0);
        int cols = sourceMatrix.GetLength(1);

        for (int i = 0; i < rows; i++)
        {
        for (int j = 0; j < cols; j++)
        {
            destinationMatrix[startRow + i, startCol + j] = sourceMatrix[i, j];
        }
        }
    }

    public static int[,] GetResult(int[,] MA1, int[,] MA2, int[,] MA3, int[,] MA4) {
        int rows = MA4.GetLength(0);
        int cols = MA4.GetLength(1);
    
        int[,] result = new int[4* rows, cols];

        CopyToFinalMatrixMA(MA1, result, 0, 0);
        CopyToFinalMatrixMA(MA2, result, rows, 0);
        CopyToFinalMatrixMA(MA3, result, 2* rows, 0);
        CopyToFinalMatrixMA(MA4, result, 3*rows, 0);

        return result;
    }
    
    public static string ArrayToString(int[,] data) {
        int rows = data.GetLength(0);
        int cols = data.GetLength(1);

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
             builder.Append(data[i, j]).Append(' ');
            }

            builder.AppendLine(); 
        }

        return builder.ToString();
    }
}
}