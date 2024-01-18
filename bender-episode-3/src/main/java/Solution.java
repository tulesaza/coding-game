import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

class Solution {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] X = new int[N];
        int[] Y = new int[N];
        for (int i = 0; i < N; i++) {
            int num = in.nextInt();
            int t = in.nextInt();
            X[i] = num;
            Y[i] = t;
        }

        double minError = Double.POSITIVE_INFINITY;
        Complexity minComplexity = null;

        for (Map.Entry<Complexity, LeastSquareError> entry : leastSquareErrorMap.entrySet()) {
            double currentError = entry.getValue().calculate(X, Y);
            if (currentError < minError) {
                minError = currentError;
                minComplexity = entry.getKey();
            }
        }


        assert minComplexity != null;
        System.out.println(minComplexity.label);
    }

    enum Complexity {
        CONSTANT("O(1)"),
        LOG("O(log n)"),
        LINEAR("O(n)"),
        N_LOG("O(n log n)"),
        SQUARE("O(n^2)"),
        SQUARE_LOG("O(n^2 log n)"),
        N_3("O(n^3)"),
        POW_2("O(2^n)");

        public final String label;

        Complexity(String label) {
            this.label = label;
        }
    }

    static EnumMap<Complexity, LeastSquareError> leastSquareErrorMap = new EnumMap<>(Complexity.class) {{
        // g(x) = 1 => log(g(x)) = 0;
        put(Complexity.CONSTANT, new LeastSquareError(n -> 0.0));
        // g(x) = log(x) => log(g(x)) = log(log(x))
        put(Complexity.LOG, new LeastSquareError(n -> Math.log(Math.log(n))));
        // g(x) = x => log(g(x)) = log(x)
        put(Complexity.LINEAR, new LeastSquareError(Math::log));
        // g(x) = x * log(x) => log(g(x)) = log(x) + log(log(x))
        put(Complexity.N_LOG, new LeastSquareError(n -> Math.log(n) + Math.log(Math.log(n))));
        // g(x) = x^2 => log(g(x)) = 2 * log(x)
        put(Complexity.SQUARE, new LeastSquareError(n -> 2 * Math.log(n)));
        // g(x) = x^2 * log(x) => log(g(x)) = 2 * log(x) + log(log(x))
        put(Complexity.SQUARE_LOG, new LeastSquareError(n -> 2 * Math.log(n) + Math.log(Math.log(n))));
        // g(x) = x^3 => log(g(x)) = 3 * log(x)
        put(Complexity.N_3, new LeastSquareError(n -> 3 * Math.log(n)));
        // g(x) = 2^x => log(g(x)) = x * log(2)
        put(Complexity.POW_2, new LeastSquareError(n -> n * Math.log(2)));
    }};

    static class LeastSquareError {
        private final Function<Integer, Double> function;

        public LeastSquareError(Function<Integer, Double> function) {
            this.function = function;
        }

        /*
            |f(x)|<= C * |g(x)|
            MSE = 1/n * sum [1;n] ((f(x) - C * g(x)) ^2)
            for larger number better to use log scale, so:
            MSE = 1/n * sum[1;n] (log(f(x) - logC - log(g(x)))^2, where logC = log(f(z)) - log(g(z)), z is any from 1 to n
         */
        public double calculate(int[] X, int[] Y) {
            int n = X.length;
            double sum = 0;
            double logC = Math.log(Y[n - 1]) - function.apply(X[n - 1]);
            for (int i = 0; i < n; i++) {
                sum += Math.pow((Math.log(Y[i]) - logC - function.apply(X[i])), 2);
            }
            return sum / n;
        }
    }


}