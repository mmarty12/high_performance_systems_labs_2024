import java.util.Arrays;
import java.util.Scanner;

public class T3_F3 extends Thread {
    int[] P, O;
    int[][] MR, MS;

    public void inputAndCalculate() {
        try (Scanner scan = new Scanner(System.in)) {
            System.out.print("Enter N: ");
            int N = scan.nextInt();

            if (N == 4) {
                //input vector values
                System.out.print("Enter P values (seperated by spaces): ");
                P = Data.vectorInput(N, scan);

                //input matrix values
                System.out.println("Enter MR values: ");
                MR = Data.matrixInput(N, scan);

                System.out.println("Enter MS values: ");
                MS = Data.matrixInput(N, scan);

                //final result calculation
                O = Data.F3(P, MR, MS);
            } else if (N > 1000) {
                System.out.println("Vector and matrix values are to be read from file.");
                System.out.println("Press 1 to generate random values or 2 for them to be equal 3: ");
                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        //vector files creation
                        System.out.println("Enter file name for P vector: ");
                        P = Data.vectorCreate(N, true, scan, 3);

                        //matrix files creation
                        System.out.println("Enter file name for MR matrix: ");
                        MR = Data.matrixCreate(N, true, scan, 3);

                        System.out.println("Enter file name for MS matrix: ");
                        MS = Data.matrixCreate(N, true, scan, 3);

                        //final result calculation and writing it to a file
                        O = Data.F3(P, MR, MS);
                        System.out.println("Enter file name for O (final result) vector: ");
                        Data.writeResultToFile(O, scan);
                        break;
                    case 2:
                        //vector files creation
                        System.out.println("Enter file name for P vector: ");
                        P = Data.vectorCreate(N, false, scan, 3);

                        //matrix files creation
                        System.out.println("Enter file name for MR matrix: ");
                        MR = Data.matrixCreate(N, false, scan, 3);

                        System.out.println("Enter file name for MS matrix: ");
                        MS = Data.matrixCreate(N, false, scan, 3);

                        //final result calculation and writing it to a file
                        O = Data.F3(P, MR, MS);
                        System.out.println("Enter file name for O (final result) vector: ");
                        Data.writeResultToFile(O, scan);
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
        System.out.println("T3 is started");
        inputAndCalculate();
        System.out.println("O = " + Arrays.toString(O));
        System.out.println("T3 is finished");
    }
}
