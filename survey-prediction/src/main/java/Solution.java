import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        Map<String, Integer> genres = new LinkedHashMap<>();
        List<int[]> X = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        List<int[]> testX = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] answer = in.nextLine().split(" ");
            int d1 = Integer.parseInt(answer[0]);
            int d2 = convertGender(answer[1]);
            if (answer.length == 3) {
                int d3 = genres.getOrDefault(answer[2], genres.size());
                genres.put(answer[2], d3);
                labels.add(d3);
                X.add(new int[]{1, d1, d2});
            } else {
                testX.add(new int[]{1, d1, d2});
            }
        }
        List<int[]> Y = new ArrayList<>(labels.size());
        for (Integer label : labels) {
            int[] possibilities = new int[genres.size()];
            possibilities[label] = 1;
            Y.add(possibilities);
        }


        System.err.println("X=" + debugList(X));
        System.err.println("labels=" + labels);
        System.err.println("Y=" + debugList(Y));
        System.err.println("genres=" + genres);
        System.err.println("testX=" + debugList(testX));

        LogisticRegression logisticRegression = new LogisticRegression(X.get(0).length, Y.get(0).length);
        logisticRegression.train(X, Y);
        System.err.println("weights " + Arrays.deepToString(logisticRegression.weights));
        for (int[] data : testX) {
            double[] result = logisticRegression.classify(data);
            System.err.printf("For x=%s y=%s\n", Arrays.toString(data), Arrays.toString(result));
        }


        System.out.println("hip-s");
        System.out.println("hip-s");
        System.out.println("hip-s");
    }


    private static String debugList(List<int[]> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(Arrays.toString(list.get(i)));
            sb.append(", ");
        }
        if (list.size() - 1 >= 0) {
            sb.append(Arrays.toString(list.get(list.size() - 1)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static int convertGender(String g) {
        return g.equals("female") ? 0 : 1;
    }

    // Will not work with such a little data. Try decision trees.
    static class LogisticRegression {

        private final static int ITERATIONS = 1000000;
        private final double rate;

        private final double[][] weights;

        public LogisticRegression(int xSize, int ySize) {
            this.rate = 0.001;
            weights = new double[ySize][xSize];
        }

        private double[] softmax(double[] z) {
            double[] predicted = new double[z.length];
            double sum = 0.0;
            for (double zk : z) {
                sum += Math.exp(zk);
            }
            for (int i = 0; i < z.length; i++) {
                predicted[i] = Math.exp(z[i]) / sum;
            }
            return predicted;
        }

        public void train(List<int[]> X, List<int[]> Y) {

            for (int n = 0; n < ITERATIONS; n++) {
                for (int k = 0; k < X.size(); k++) {
                    int[] x = X.get(k);
                    double[] predicted = classify(x);
                    int[] y = Y.get(k);
                    for (int i = 0; i < weights.length; i++) {
                        for (int j = 0; j < weights[i].length; j++) {
                            weights[i][j] = weights[i][j] + rate * (y[i] - predicted[i]) * x[j];
                        }
                    }
                }
            }
        }

        private double[] classify(int[] x) {
            double[] ys = new double[weights.length];
            for (int i = 0; i < weights.length; i++) {
                double logit = .0;
                for (int j = 0; j < weights[i].length; j++) {
                    logit += weights[i][j] * x[j];
                }
                ys[i] = logit;
            }

            return softmax(ys);
        }
    }
}