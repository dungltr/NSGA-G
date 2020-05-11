//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package NSGAS;

public class matrixPrint {
    public matrixPrint() {
    }

    public static void printArray(int[] a) {
        int m = a.length;

        for(int i = 0; i < m; ++i) {
            System.out.print(a[i]);
            System.out.print(" ");
        }

    }

    public static void printArray(double[] a) {
        int m = a.length;

        for(int i = 0; i < m; ++i) {
            System.out.printf("%.2f", a[i]);
            System.out.print(" ");
        }

    }

    public static void printMatrix(double[][] a) {
        int m = a.length;
        int n = a[0].length;

        for(int i = 0; i < m; ++i) {
            for(int j = 0; j < n; ++j) {
                System.out.printf("%.2f", a[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }

    }
}
