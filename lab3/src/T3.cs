namespace lab3
{
    internal class T3 {
        public static int[,] MM, MAн;
        private static int threadIndex = 3;

         public static void inputAndCalculate() {
            try {
                Console.WriteLine("T3 is started");

                // Введення MM
                MM = Data.MatrixInput(Lab3.N);

               //Сигнал задачі Т1, Т2, Т4 про введення MM
                Data.e3.Set();

                // Чекати на введення MC, B у задачі Т1; Z, d, р у задачі Т2; MR, MZ у задачі Т4  
                Data.e1.WaitOne();
                Data.e2.WaitOne();
                Data.e4.WaitOne();

                // Обчислення1 a3 = ( Bн * Zн)
                int startIndex = (threadIndex * Lab3.H) - Lab3.H;

                int[] Zн = new int[Lab3.H];
                Array.Copy(T2.Z, startIndex, Zн, 0, Lab3.H);
                int[] Bн = new int[Lab3.H];
                Array.Copy(T1.B, startIndex, Bн, 0, Lab3.H);

                int a3 = Data.VectorMultiply(Bн, Zн);

                // Обчислення2 a = a + a3
                Interlocked.Add(ref Data.a, a3);    // КД1

                // Чекати на завершення обчислень а в Т1, Т2, T4  
                Data.b1.SignalAndWait();

                // Копія а3 = а
                lock (Data.cs1){
                    a3 = Data.a;    // КД2
                }

                int p3 = (int) Interlocked.Read(ref T2.p);  // КД3

                // Обчислення3 MBн  = a1 * (MZ * MMн) * р3
                int[,] MBн = Data.CalculateMBн(T4.MZ, MM, startIndex, startIndex + Lab3.H, a3, p3);

                // Копія d3 = d
                Data.m1.WaitOne();
                int d3 = T2.d;  // КД4
                Data.m1.ReleaseMutex();

                // Обчислення4 MAн = (MR * MCн) * d3 + MBн 
                MAн = Data.CalculateMA(T4.MR, T1.MC, startIndex, startIndex + Lab3.H, d3, MBн);

                // Чекати на завершення обчислень MAн в Т1, Т2, T4
                Data.sem1.WaitOne();
                Data.sem2.WaitOne();
                Data.sem3.WaitOne();
                
                Console.WriteLine("T1.MA: " + Data.ArrayToString(T1.MAн));
                Console.WriteLine("T2.MA: " + Data.ArrayToString(T2.MAн));
                Console.WriteLine("T3.MA: " + Data.ArrayToString(MAн));
                Console.WriteLine("T4.MA: " + Data.ArrayToString(T4.MAн));

                int[,] MA = Data.GetResult(T1.MAн, T2.MAн, MAн, T4.MAн);
                Console.WriteLine("MA: \n" + Data.ArrayToString(MA));

                Console.WriteLine("T3 is finished");
            } catch (Exception e) {
            Console.WriteLine("EXCEPTION: " + e);
        }
        }
    }
}