import java.util.Arrays;
import java.util.Scanner;

public class T1_F1 extends Thread {
    //local variables
    int [] A, C, B, E;
    int [][] MA, ME;

    public void inputAndCalculate() {
        try (Scanner scan = new Scanner(System.in)) {
            System.out.print("Enter N: ");
            int N = scan.nextInt();

            if (N == 4) {
                //input vector values
                System.out.print("Enter A values (separated by spaces): ");
                A = Data.vectorInput(N, scan);

                System.out.print("Enter B values (separated by spaces): ");
                B = Data.vectorInput(N, scan);

                System.out.print("Enter C values (separated by spaces): ");
                C = Data.vectorInput(N, scan);

                //input matrix values
                System.out.println("Enter MA values:");
                MA = Data.matrixInput(N, scan);

                System.out.println("Enter ME values:");
                ME = Data.matrixInput(N, scan);

                //final result calculation
                E = Data.F1(A, B, C, MA, ME);
            } else if (N > 1000) {
                System.out.println("Vector and matrix values are to be read from file.");
                System.out.println("Press 1 to generate random values or 2 for them to be equal 1: ");
                int choice = scan.nextInt();
                scan.nextLine();

                switch (choice) {
                    case 1:
                        //vector files creation
                        System.out.println("Enter file name for A vector: ");
                        A = Data.vectorCreate(N,true, scan, 1);

                        System.out.println("Enter file name for B vector: ");
                        B = Data.vectorCreate(N,true, scan, 1);

                        System.out.println("Enter file name for C vector: ");
                        C = Data.vectorCreate(N,true, scan, 1);

                        //matrix files creation
                        System.out.println("Enter file name for MA matrix: ");
                        MA = Data.matrixCreate(N, true, scan, 1);

                        System.out.println("Enter file name for ME matrix: ");
                        ME = Data.matrixCreate(N, true, scan, 1);

                        //final result calculation and writing it to a file
                        E = Data.F1(A, B, C, MA, ME);
                        System.out.println("Enter file name for E (final result) vector: ");
                        Data.writeResultToFile(E, scan);
                        break;
                    case 2:
                        //vector files creation
                        System.out.println("Enter file name for A vector: ");
                        A = Data.vectorCreate(N,false, scan, 1);

                        System.out.println("Enter file name for B vector: ");
                        B = Data.vectorCreate(N, false, scan, 1);

                        System.out.println("Enter file name for C vector: ");
                        C = Data.vectorCreate(N, false, scan, 1);

                        //matrix files creation
                        System.out.println("Enter file name for MA matrix: ");
                        MA = Data.matrixCreate(N, false, scan, 1);

                        System.out.println("Enter file name for ME matrix: ");
                        ME = Data.matrixCreate(N,false, scan, 1);

                        //final result calculation and writing it to a file
                        E = Data.F1(A, B, C, MA, ME);
                        System.out.println("Enter file name for E (final result) vector: ");
                        Data.writeResultToFile(E, scan);
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
        System.out.println("T1 is started");
        inputAndCalculate();
        System.out.println("E = " + Arrays.toString(E));
        System.out.println("T1 is finished");
    }
}
