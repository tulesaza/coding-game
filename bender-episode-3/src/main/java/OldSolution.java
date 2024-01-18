import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class OldSolution {

    public static void main(String args[]) {


        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        LinearRegression lr = new LinearRegression(N,8);
        for (int i = 0; i < N; i++) {
            int num = in.nextInt();
            int t = in.nextInt();
            lr.feed(num, t, i);
        }

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");
        System.err.println(lr.X[N-1][7]);
        lr.train();
        System.out.println("answer");
    }


    //TODO X,Y need to be normalized,because of NaN and Infinity 

    public static class LinearRegression {

        private final double [][] X;
        private final double [] b;
        private final double[] Y;
        private final int n;
        private final int m;
        private final static  int EPOCH = 100;
        private final static double alpha = 0.1;

        LinearRegression(int n,int m){
            this.X = new double [n][];
            this.b = createRandomB(m);
            this.Y = new double [n];
            this.n = n;
            this.m = m;
        }

        public void printB(){
            System.err.print("B is (");
            for (int j =0; j<m;j++){
                String a = (j<m-1)?";":"";
                System.err.print(b[j]+a);
            }
            System.err.println(")");

        }

        public void feed(int num, int time, int inx){
            this.Y[inx] = Math.log(time);
            this.X[inx] = createVector(Math.log(num));
        }

        public void train() {
            for(int epoch = 0;epoch<EPOCH; epoch++){
                System.err.println("EPOCH "+epoch);
                printB();
                double [] oldB = this.b;
                for (int j =0; j< m;j++){
                    double [] Y1 = calculateY1(oldB);
                    this.b[j] = oldB[j] - alpha * derivative(oldB, j,Y1);
                }
            }
        }

        private double derivative(double [] oldB, int j, double [] Y1 ){
            double sum = 0;
            for (int i=0;i<n;i++){
                sum+= (Y1[i] - Y[i])*X[i][j];
            }
            return sum*2/n;
        }


        private double [] calculateY1 (double [] old) {
            double [] res = new double [n];
            for (int i=0;i<n;i++){
                int sum = 0;
                for (int j=0;j<m;j++){
                    sum+= X[i][j]*old[j];
                }
                res[i] = sum;
            }

            return res;
        }

        private double[] createRandomB (int m){
            double [] res = new double[m];
            for(int i = 0; i < m ; i++){
                res[i] = 1.0;
            }
            return res;
        }

        private double [] createVector(double a){
            double [] res = new double[m];
            res[0] =  1;
            res[1] =  Math.log(a);
            res[2] =  a;
            res[3] = res[1]*res[2];
            res[4] = res[2]*res[2];
            res[5] = res[4]*res[1];
            res[6] = res[4]*res[2];
            res[7] = Math.pow(2, res[2]);


            return res;
        }

    }
}