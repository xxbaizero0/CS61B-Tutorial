public class HelloNumbers {
    public static void DrawTriangle(int n){

        int i,j=1;
        while (n > 0) {
            String star = "*";
            i = j;
            while(i > 0) {
                System.out.print(star);
                i--;
            }
            System.out.println();
            n--;
            j++;
        }
    }
    public static void num() {
        String star = "*";
        int n = 1;
        int j;
        while (n < 6) {
            j = n;
            while(j > 0) {
                System.out.print(star);
                j--;
            }
            System.out.println();
            n++;
        }
    }

    public static void windowPosSum(int[] a, int n) {
        /** your code here */

        for (int i = 0; i < a.length; i++) {
            if (a[i] > 0) {
                for (int j = 0; j < n; j++) {
                    if (i + j + 1 < a.length) {
                        a[i] += a[i + j + 1];
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }


} 
