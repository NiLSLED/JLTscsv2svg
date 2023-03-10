package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainLogic {
    private int pixelX;
    private int pixelY;
    private double[][] data;
    private double[][] axis;
    private StringBuilder build;
    private boolean logarithmicX;
    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);
        new MainLogic().basicCsvToSvg(args[0],args[1]);
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
        PrintWriter writer;
        try {
            writer = new PrintWriter(pathSvg, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.print(svg);
        writer.close();
    }

    public String[][] readTableFile(String filePath) {
        File file = new File(filePath);
        Scanner lineScanner;
        try {
            lineScanner = new Scanner(file, "ISO-8859-15");
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
            scanner.useDelimiter("\t");
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
                if (row[currentIndex].startsWith("Step Information: ")) {
                    scanner.close();
                    lineScanner.close();
                    return readTableFileWithStep(filePath);
                }
                if (row[currentIndex].startsWith("(")) {
                    scanner.close();
                    lineScanner.close();
                    return readTableFileWithBode(filePath);
                }
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

    public double[][] tableToDouble(String[][] table) {
        double[][] doubleTable = new double[table.length-1][table[0].length]; //First row are comments
        for (int i = 1; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                doubleTable[i-1][j] = Double.parseDouble(table[i][j]);
            }
        }
        return doubleTable;
    }

    private String generatePolylineLogarithmic(int pixelX, int pixelY, double fromX, double toX, double fromY, double toY, String color, double[][] data, int colum, boolean logarithmicX, boolean logarithmicY) {
        StringBuilder polyline = new StringBuilder();
        polyline.append("<polyline stroke=\"");
        polyline.append(color);
        polyline.append("\" fill=\"transparent\" points=\"");
        for (double[] datum : data) {
            polyline.append(getLogarithmicRelative(pixelX, fromX, toX, datum[0], logarithmicX));
            polyline.append(",");
            polyline.append(pixelY-getLogarithmicRelative(pixelY, fromY, toY, datum[colum], logarithmicY));
            polyline.append(" ");
        }
        polyline.append("\"/>\n");
        return polyline.toString();
    }

    private String generatePolyline(int pixelX, int pixelY, double fromX, double toX, double fromY, double toY, String color, double[][] data, int colum) {
        return generatePolylineLogarithmic(pixelX, pixelY, fromX, toX, fromY, toY, color, data, colum, false, false);
    }


    private float getRelative(int pixels, double from, double to, double value) {
        to -= from;
        value -= from;
        float relative = Math.round(pixels*(value/to)*100)/100f; //round to two digits
        if (relative< 0) {
            return 0;
        } else if (relative > pixels) {
            return pixels;
        } else {
            return relative;
        }
    }

    private float getLogarithmicRelative(int pixels, double from, double to, double value, boolean logarithmic) {
        if (logarithmic) {
            value = Math.log10(value);
            from = Math.log10(from);
            to = Math.log10(to);
        }
        return getRelative(pixels, from, to, value);
    }

    public double maxValueInColumn(double[][] table, int column) {
        double max = -Double.MAX_VALUE;
        for (double[] row : table) {
            if (row[column] > max) {
                max = row[column];
            }
        }
        return max;
    }

    public double minValueInColumn(double[][] table, int column) {
        double min = Double.MAX_VALUE;
        for (double[] row : table) {
            if (row[column] < min) {
                min = row[column];
            }
        }
        return min;
    }

    private String[][] readTableFileWithStep(String filePath) {
        File file = new File(filePath);
        Scanner lineScanner;
        try {
            lineScanner = new Scanner(file, "ISO-8859-15");
        } catch (FileNotFoundException e) {
            System.err.println("file not found: "+filePath);
            return null;
        }
        int columnsPerLine =0;
        int columnsPerLineDataLine =0;
        int totalRuns = 0;
        ArrayList<String[]> table = new ArrayList<>();
        lineLoop:
        while (lineScanner.hasNextLine()) {
            //Read line
            Scanner scanner = new Scanner(lineScanner.nextLine());
            scanner.useDelimiter("\t");
            if (columnsPerLine == 0) {
                ArrayList<String> row = new ArrayList<>(2);
                while (scanner.hasNext()) {
                    row.add(scanner.next());
                }
                columnsPerLine = row.size();
                columnsPerLineDataLine = columnsPerLine;
                table.add(new String[columnsPerLine]);
                table.set(0,row.toArray(table.get(0)));
                continue;
            }
            String[] row = new String[columnsPerLine];
            int currentIndex =0;
            boolean lineCorrect = true;
            while (scanner.hasNext()) {
                row[currentIndex] = scanner.next();
                if (row[currentIndex].startsWith("Step Information: ")) {
                    if (totalRuns == 0) {
                        int runNumberStart = row[currentIndex].indexOf("(Run: 1/") + 8;
                        int runNumberEnd = row[currentIndex].indexOf(")");
                        totalRuns = Integer.decode(row[currentIndex].substring(runNumberStart, runNumberEnd));
                        columnsPerLine = (columnsPerLineDataLine-1)*totalRuns+1;
                        String[] newFirstLine = new String[columnsPerLine];

                        table.set(0, Arrays.copyOf(table.get(0),columnsPerLine));
                        lineCorrect = false;
                        continue;
                    } else {
                        break lineLoop;
                    }
                }
                currentIndex++;
            }
            scanner.close();
            if (lineCorrect) {
                table.add(row);
            }
        }
        //readData from step 2...n
        int currentRun = 2; //Counted from 1 ongoing
        int currentColumn = 1; //first line is info comment
        while (lineScanner.hasNextLine()) {
            Scanner scanner = new Scanner(lineScanner.nextLine());
            scanner.useDelimiter("\t");
            if(scanner.next().startsWith("Step Information: ")) {//skip first colum because identical to step 1
                currentRun++;
                currentColumn = 1;
                continue;
            }
            String[] row = table.get(currentColumn);
            int currentIndex = 1;
            while (scanner.hasNext()) {
                row[currentIndex+(columnsPerLineDataLine-1)*(currentRun-1)] = scanner.next();
                currentIndex++;
            }
            currentColumn++;
        }
        lineScanner.close();
        String[][] tableArray = new String[table.size()][];
        tableArray = table.toArray(tableArray);
        return tableArray;
    }

    private String[][] readTableFileWithBode(String filePath) {
        File file = new File(filePath);
        Scanner lineScanner = null;
        try {
            lineScanner = new Scanner(file, "ISO-8859-15");
        } catch (FileNotFoundException e) {
            System.err.println("file not found: "+filePath);
            return null;
        }
        int columnsPerLine =0;
        ArrayList<String[]> table = new ArrayList<>();
        while (lineScanner.hasNextLine()) {
            //Read line
            Scanner scanner = new Scanner(lineScanner.nextLine());
            scanner.useDelimiter("\t");
            if (columnsPerLine == 0) {
                ArrayList<String> row = new ArrayList<>(2);
                while (scanner.hasNext()) {
                    row.add(scanner.next());
                }
                columnsPerLine = row.size()*2-1;
                table.add(new String[columnsPerLine]);
                table.set(0,row.toArray(table.get(0)));
                continue;
            }
            scanner.useDelimiter("[\tdB,(°)]+");
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

    public void startBuild(int pixelX, int pixelY, double[][] data, double[][] axis, boolean log) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.axis = axis;
        this.data = data;
        this.logarithmicX = log;
        build = new StringBuilder("<svg version=\"1.1\"\n\twidth=\""+ pixelX +"\" height=\""+pixelY+"\"\n\txmlns=\"http://www.w3.org/2000/svg\">\n\n");
    }

    public void addData(int[] color, int colum, int axisNumber) {
        addDataLogarithmic(color, colum, axisNumber, false);
    }
    public void addDataLogarithmic(int[] color, int colum, int axisNumber, boolean logarithmic) {
        build.append("\t");
        build.append(generatePolylineLogarithmic(pixelX, pixelY, axis[0][0], axis[0][1], axis[axisNumber][0], axis[axisNumber][1], convertSVGColor(color), data, colum,logarithmicX, logarithmic));
        build.append("\n");
    }

    public void exportBuild(String svgPath) {
        build.append("</svg>\n");
        PrintWriter writer;
        try {
            writer = new PrintWriter(svgPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.print(build);
        writer.close();

    }

    private String convertSVGColor(int[] color) {
        return "rgb(" + color[0] + "," + color[1] + "," + color[2] + ")";
    }
}