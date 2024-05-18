namespace lab3
{
    internal class T4 {
        public static int[,] MR, MZ, MAн;
        private static int threadIndex = 4;

        public static void inputAndCalculate() {
            try {
                Console.WriteLine("T4 is started");

                // Введення MR, MZ
                MR = Data.MatrixInput(Lab3.N);
                MZ = Data.MatrixInput(Lab3.N);

                // //Сигнал задачі Т1, Т3, Т4 про введення Z, d, p
                Data.e4.Set();

                // // Чекати на введення MC, B у задачі Т1; Z, d, р у задачі Т2; MM у задачі Т3
                Data.e1.WaitOne();
                Data.e2.WaitOne();
                Data.e3.WaitOne();

                // Обчислення1 a4 = ( Bн * Zн)
                int startIndex = (threadIndex * Lab3.H) - Lab3.H;

                int[] Zн = new int[Lab3.H];
                Array.Copy(T2.Z, startIndex, Zн, 0, Lab3.H);
                int[] Bн = new int[Lab3.H];
                Array.Copy(T1.B, startIndex, Bн, 0, Lab3.H);

                int a4 = Data.VectorMultiply(Bн, Zн);

                // Обчислення2 a = a + a4
                Interlocked.Add(ref Data.a, a4);    // КД1

                // Чекати на завершення обчислень а в Т1, Т3, T4  
                Data.b1.SignalAndWait();

                // Копія а2 = а
                lock (Data.cs1){
                    a4 = Data.a;    // КД2
                }

                int p4 = (int) Interlocked.Read(ref T2.p);  // КД3

                // Обчислення3 MBн  = a2 * (MZ * MMн) * р1
                int[,] MBн = Data.CalculateMBн(MZ, T3.MM, startIndex, startIndex + Lab3.H, a4, p4);

                // Копія d4 = d
                Data.m1.WaitOne();
                int d4 = T2.d;  // КД4
                Data.m1.ReleaseMutex();

                // Обчислення4 MAн = (MR * MCн) * d4 + MBн 
                MAн = Data.CalculateMA(MR, T1.MC, startIndex, startIndex + Lab3.H, d4, MBн);

                // Сигнал Т3 про завершення обчислень MAн
                Data.sem3.Release();

                Console.WriteLine("T4 is finished");
            } catch (Exception e) {
            Console.WriteLine("EXCEPTION: " + e);
        }
        }
    }
}