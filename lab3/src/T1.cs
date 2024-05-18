namespace lab3
{
    internal class T1 {
        public static int[,] MC, MAн;
        public static int[] B;
        private static int threadIndex = 1;

    public static void inputAndCalculate() {
           try {
                Console.WriteLine("T1 is started");

                // Введення MC, B
                B = Data.VectorInput(Lab3.N);
                MC = Data.MatrixInput(Lab3.N);

                //Сигнал задачі Т2, Т3, Т4 про введення MC, B
                Data.e1.Set();

                // Чекати на введення Z, d, р у задачі Т2; MM у задачі Т3; MR, MZ у задачі Т4
                Data.e2.WaitOne();
                Data.e3.WaitOne();
                Data.e4.WaitOne();

                // Обчислення1 a1 = ( Bн * Zн)
                int startIndex = (threadIndex * Lab3.H) - Lab3.H;

                int[] Zн = new int[Lab3.H];
                Array.Copy(T2.Z, startIndex, Zн, 0, Lab3.H);
                int[] Bн = new int[Lab3.H];
                Array.Copy(B, startIndex, Bн, 0, Lab3.H);

                int a1 = Data.VectorMultiply(Bн, Zн);

                // Обчислення2 a = a + a1
                Interlocked.Add(ref Data.a, a1);    // КД1

                // Чекати на завершення обчислень а в Т2, Т3, T4  
                Data.b1.SignalAndWait();

                // Копія а1 = а
                lock (Data.cs1) {
                    a1 = Data.a;    // КД2
                }

                int p1 = (int) Interlocked.Read(ref T2.p);  // КД3

                // Обчислення3 MBн  = a1 * (MZ * MMн) * р1
                int[,] MBн = Data.CalculateMBн(T4.MZ, T3.MM, startIndex, startIndex + Lab3.H, a1, p1);

                // Копія d1 = d
                Data.m1.WaitOne();
                int d1 = T2.d;  // КД4
                Data.m1.ReleaseMutex();

                // Обчислення4 MAн = (MR * MCн) * d1 + MBн 
                MAн = Data.CalculateMA(T4.MR, MC, startIndex, startIndex + Lab3.H, d1, MBн);

                // Сигнал Т3 про завершення обчислень MAн
                Data.sem1.Release();

                Console.WriteLine("T1 is finished");
        } catch (Exception e) {
            Console.WriteLine("EXCEPTION: " + e);
        }
        }
}
}