import java.util.*;
import java.io.*;
import java.math.*;

class Solution {

    public static void main(String[] args) {

        Grid grid = new Grid(new Scanner(System.in));
        grid.debugToFile("/home/aza/out.txt");

        System.out.println("AQ DH");
    }

    static class Grid {
        private final int w;
        private final int h;
        private final boolean[][] grid;


        public Grid(Scanner scanner) {
            this.w = scanner.nextInt();
            this.h = scanner.nextInt();
            grid = new boolean[h][w];
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            parseImage(scanner);
            scanner.close();
        }

        private void parseImage(Scanner scanner) {
            LinkedHashSet<Integer> lines = new LinkedHashSet<>();
            int rowIndex = 0, columnIndex = 0;
            while (scanner.hasNext()) {
                String colour = scanner.next();
                int length = scanner.nextInt();
                if (colour.equals("W")) {
                    rowIndex += (columnIndex + length) / w;
                    columnIndex = (columnIndex + length) % w;
                } else {
                    if(length > w/4){
                        lines.add(rowIndex);
                    }
                    for (int i = 0; i < length; i++) {
                        columnIndex++;
                        grid[rowIndex][columnIndex] = true;
                    }
                }

            }
            System.err.println(lines);
        }

        public void debugToConsole() {
            debug(System.err);
        }

        public void debugToFile(String filePath) {
            try {
                PrintStream printStream = new PrintStream(filePath);
                debug(printStream);
            } catch (FileNotFoundException exception) {
                System.err.println("File " + filePath + " is not found");
            }

        }

        /*
            Each symbol SPACE or DOT represents white or black pixel
         */
        private void debug(PrintStream printStream) {
            System.setErr(printStream);
            for (int i = 0; i < w; i++) {
                System.err.print("-");
            }
            System.err.println();
            for (int i = 0; i < h; i++) {
                System.err.print("|");
                for (int j = 0; j < w; j++) {
                    System.err.print((grid[i][j] ? "." : " "));
                }
                System.err.println("|");

            }
            for (int i = 0; i < w; i++) {
                System.err.print("-");
            }
            System.err.println();

            System.setErr(System.err);
        }


    }
}