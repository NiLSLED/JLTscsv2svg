import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);
        new Main().basicCsvToSvg(args[0],args[1]);
    }

    public void basicCsvToSvg(String pathCsv, String pathSvg) {
        double[][] table = tableToDouble(readTableFile(pathCsv));
        StringBuilder svg = new StringBuilder();
        double minX = minValueInColumn(table, 0);
        double maxX = maxValueInColumn(table, 0);
        svg.append("<svg version=\"1.1\"\n\twidth=\"600\" height=\"400\"\n\txmlns=\"http://www.w3.org/2000/svg\">\n\n");
        for (int i = 1; i < table[0].length; i++) {
            svg.append("\t");
            svg.append(generatePolyline(600,400, minX, maxX, minValueInColumn(table, i), maxValueInColumn(table, i), "black", table, i));
            svg.append("\n");
        }
        svg.append("</svg>\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(pathSvg, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.print(svg);
        writer.close();
    }

    private String[][] readTableFile(String filePath) {
        File file = new File(filePath);
        Scanner lineScanner = null;
        try {
            lineScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("file not found: "+filePath);
            return null;
        }
        int columnsPerLine =0;
        ArrayList<String[]> table = new ArrayList<>();
        while (lineScanner.hasNextLine()) {
            //Read line
            Scanner scanner = new Scanner(lineScanner.nextLine());
            //TODO bode
            //scanner.useDelimiter("[\\p{javaWhitespace}(),]+");
            if (columnsPerLine == 0) {
                ArrayList<String> row = new ArrayList<>(2);
                while (scanner.hasNext()) {
                    row.add(scanner.next());
                }
                columnsPerLine = row.size();
                table.add(new String[columnsPerLine]);
                table.set(0,row.toArray(table.get(0)));
                continue;
            }
            String[] row = new String[columnsPerLine];
            int currentIndex =0;
            while (scanner.hasNext()) {
                row[currentIndex] = scanner.next();
                currentIndex++;
            }
            scanner.close();
            table.add(row);
        }
        lineScanner.close();
        String[][] tableArray = new String[table.size()][];
        tableArray = table.toArray(tableArray);
        return tableArray;
    }

    private double[][] tableToDouble(String[][] table) {
        double[][] doubleTable = new double[table.length-1][table[0].length]; //First row are comments
        for (int i = 1; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                doubleTable[i-1][j] = Double.parseDouble(table[i][j]);
            }
        }
        return doubleTable;
    }

    private String generatePolyline(int pixelX, int pixelY, double fromX, double toX, double fromY, double toY, String color, double[][] data, int colum) {
        StringBuilder polyline = new StringBuilder();
        polyline.append("<polyline stroke=\"");
        polyline.append(color);
        polyline.append("\" fill=\"transparent\" points=\"");
        for (double[] datum : data) {
            polyline.append(getRelative(pixelX, fromX, toX, datum[0]));
            polyline.append(",");
            polyline.append(pixelY-getRelative(pixelY, fromY, toY, datum[colum]));
            polyline.append(" ");
        }
        polyline.append("\"/>\n");
        return polyline.toString();
    }

    private float getRelative(int pixels, double from, double to, double value) {
        to -= from;
        value -= from;
        return Math.round(pixels*(value/to)*100)/100f; //round to two digits
    }

    private double maxValueInColumn(double[][] table, int column) {
        double max = -Double.MAX_VALUE;
        for (double[] row : table) {
            if (row[column] > max) {
                max = row[column];
            }
        }
        return max;
    }

    private double minValueInColumn(double[][] table, int column) {
        double min = Double.MAX_VALUE;
        for (double[] row : table) {
            if (row[column] < min) {
                min = row[column];
            }
        }
        return min;
    }
}