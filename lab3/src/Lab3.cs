//Дисципліна "Програмне забезпечення високопродуктивних комп’ютерних систем"
//Лабораторна робота ЛР3 Варіант 7
//MA = (MR * MC) * d + (B * Z) * (MZ * MM) * p
//Мартинюк Марія Павлівна
//Дата 11.04.2024

using System.Diagnostics;

namespace lab3
{
    internal class Lab3
    {
        public static int N = 8;
        public static int P = 4;
        public static int H = N/P;
        static Stopwatch watchApp = new Stopwatch();
        
        public static void Main(string[] args)
        {
            watchApp.Start();

            var t1 = new Thread(() => T1.inputAndCalculate());
            var t2 = new Thread(() => T2.inputAndCalculate());
            var t3 = new Thread(() => T3.inputAndCalculate());
            var t4 = new Thread(() => T4.inputAndCalculate());
            t1.Start();
            t2.Start();
            t3.Start();
            t4.Start();
            
            t1.Join();
            t2.Join();
            t3.Join();
            t4.Join();

            watchApp.Stop();
            Console.WriteLine("Time: " + watchApp.ElapsedMilliseconds + " ms");

        } 
    }
}