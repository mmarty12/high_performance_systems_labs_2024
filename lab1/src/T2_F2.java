import java.util.Arrays;
import java.util.Scanner;

public class T2_F2 extends Thread{
    int[][] MK, ML, MG, MF;

    public void inputAndCalculate() {
        try (Scanner scan = new Scanner(System.in)) {
            System.out.print("Enter N: ");
            int N = scan.nextInt();

            if (N == 4) {
                //input matrix values
                System.out.println("Enter MK values:");
                MK = Data.matrixInput(N, scan);

                System.out.println("Enter ML values:");
                ML = Data.matrixInput(N, scan);

                System.out.println("Enter MG values:");
                MG = Data.matrixInput(N, scan);

                //final result calculation
                MF = Data.F2(MK, ML, MG);
            } else if (N > 1000) {
                System.out.println("Matrix values are to be read from file.");
                System.out.println("Press 1 to generate random values or 2 for them to be equal 2: ");
                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        //matrix files creation
                        System.out.println("Enter file name for MK matrix: ");
                        MK = Data.matrixCreate(N, true, scan, 2);

                        System.out.println("Enter file name for ML matrix: ");
                        ML = Data.matrixCreate(N, true, scan, 2);

                        System.out.println("Enter file name for MG matrix: ");
                        MG = Data.matrixCreate(N, true, scan, 2);

                        //final result calculation and writing it to a file
                        MF = Data.F2(MK, ML, MG);
                        System.out.println("Enter file name for MF (final result) matrix: ");
                        Data.writeResultToFile(MF, scan);
                        break;
                    case 2:
                        //matrix files creation
                        System.out.println("Enter file name for MK matrix: ");
                        MK = Data.matrixCreate(N, false, scan, 2);

                        System.out.println("Enter file name for ML matrix: ");
                        ML = Data.matrixCreate(N, false, scan, 2);

                        System.out.println("Enter file name for MG matrix: ");
                        MG = Data.matrixCreate(N, false, scan, 2);

                        //final result calculation and writing it to a file
                        MF = Data.F2(MK, ML, MG);
                        System.out.println("Enter file name for MF (final result) matrix: ");
                        Data.writeResultToFile(MF, scan);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1 or 2.");
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void run(){
        System.out.println("T2 is started");
        inputAndCalculate();
        System.out.println("MF = " + Arrays.deepToString(MF));
        System.out.println("T2 is finished");
    }
}
