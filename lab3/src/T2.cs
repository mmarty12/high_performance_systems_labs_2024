namespace lab3
{
    internal class T2 {
        public static int[] Z; 
        public static int d;
        public static long p;
        public static int[,] MAн;
        private static int threadIndex = 2;

        public static void inputAndCalculate() {
            try {
                Console.WriteLine("T2 is started");

                // Введення Z, d, p
                Z = Data.VectorInput(Lab3.N);
                d = 1;
                p = 1;

                //Сигнал задачі Т1, Т3, Т4 про введення Z, d, p
                Data.e2.Set();

                // Чекати на введення MC, B у задачі Т1; MM у задачі Т3; MR, MZ у задачі Т4
                Data.e1.WaitOne();
                Data.e3.WaitOne();
                Data.e4.WaitOne();

                // Обчислення1 a2 = ( Bн * Zн)
                int startIndex = (threadIndex * Lab3.H) - Lab3.H;

                int[] Zн = new int[Lab3.H];
                Array.Copy(Z, startIndex, Zн, 0, Lab3.H);
                int[] Bн = new int[Lab3.H];
                Array.Copy(T1.B, startIndex, Bн, 0, Lab3.H);

                int a2 = Data.VectorMultiply(Bн, Zн);

                // Обчислення2 a = a + a2
                Interlocked.Add(ref Data.a, a2);    // КД1

                // Чекати на завершення обчислень а в Т1, Т3, T4  
                Data.b1.SignalAndWait();

                // Копія а2 = а
                lock (Data.cs1){
                    a2 = Data.a;    // КД2
                }

                int p2 = (int) Interlocked.Read(ref p); // КД3

                // Обчислення3 MBн  = a2 * (MZ * MMн) * р1
                int[,] MBн = Data.CalculateMBн(T4.MZ, T3.MM, startIndex, startIndex + Lab3.H, a2, p2);

                // Копія d2 = d
                Data.m1.WaitOne();
                int d2 = d; // КД4
                Data.m1.ReleaseMutex();

                // Обчислення4 MAн = (MR * MCн) * d2 + MBн 
                MAн = Data.CalculateMA(T4.MR, T1.MC, startIndex, startIndex + Lab3.H, d2, MBн);

                // Сигнал Т3 про завершення обчислень MAн
                Data.sem2.Release();

                Console.WriteLine("T2 is finished");
            } catch (Exception e) {
            Console.WriteLine("EXCEPTION: " + e);
        }
        }
    }
}