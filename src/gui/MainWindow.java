package gui;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    JTextField csvFilePath;
    JButton load;
    ArrayList<DataGUI> dataGUIS;
    ArrayList<AxisGUI> axisGUIS;
    JTextField svgFilePath;
    JButton export;
    double[][] dataTable;
    String[][] dataTableString;
    Logic.MainLogic logic = new Logic.MainLogic();

    public static void main(String[] args) {
        if (args.length == 0) {
            new MainWindow();
        } else if (args.length == 1) {
            new MainWindow(args[0]);
        }
    }

    public MainWindow() {
        this(null);
    }

    public MainWindow(String cvsFile) {
        super();
        super.setLayout(null);
        dataGUIS = new ArrayList<>();
        axisGUIS = new ArrayList<>();
        super.setSize(600, 100);

        csvFilePath = new JTextField(cvsFile);
        csvFilePath.setText("/home/nils/Dokumente/Uni/2/PR_ST/Draft5.txt");
        csvFilePath.setLocation(10,10);
        csvFilePath.setSize(300,20);
        super.add(csvFilePath);

        load = new JButton("load");
        load.setSize(100,20);
        load.setLocation(350, 10);
        load.addActionListener(e -> loadData());
        super.add(load);

        svgFilePath = new JTextField();
        super.add(svgFilePath);

        export = new JButton();
        super.add(export);

        super.setVisible(true);
    }

    /**
     * Loads data initial into {@link MainWindow#dataGUIS} and {@link MainWindow#axisGUIS}.
     * It opens the file specified by {@link MainWindow#svgFilePath} and phrases its content.
     */
    private void loadData() {
        dataGUIS.clear();
        axisGUIS.clear();
        dataTableString = logic.readTableFile(csvFilePath.getText());
        dataTable = logic.tableToDouble(dataTableString);
        loadIntoAxisGUI();
        loadIntoDataGUI();
        reRender();
    }

    private void reRender() {
        for (int i = 0; i < dataGUIS.size(); i++) {
            super.add(dataGUIS.get(i));
            dataGUIS.get(i).setLocation(10, (1+i)*40);
        }
        for (int i = 0; i < axisGUIS.size(); i++) {
            super.add(axisGUIS.get(i));
            axisGUIS.get(i).setLocation(10, (1+i+dataGUIS.size())*40);
        }
        System.out.println("finish reRender");
    }

    /**
     * Initializes {@link MainWindow#dataGUIS} it depends on {@link MainWindow#dataTable} and {@link MainWindow#dataTableString}
     */
    private void loadIntoDataGUI() {
        for (int i = 0; i < dataTableString[0].length; i++) {
            DataGUI dataGUI = new DataGUI();
            dataGUI.setBasics(dataTableString[0][i],i, logic.minValueInColumn(dataTable, i), logic.maxValueInColumn(dataTable, i));
            dataGUIS.add(dataGUI);

        }
    }

    /**
     * Initializes {@link MainWindow#axisGUIS} it depends on {@link MainWindow#dataTable} and {@link MainWindow#dataTableString}
     */
    private void loadIntoAxisGUI() {
        for (int i = 1; i < dataTableString[0].length; i++) {
            AxisGUI axisGUI = new AxisGUI();
            axisGUIS.add(axisGUI);
            axisGUI.setBasic(dataTableString[0][i], logic.minValueInColumn(dataTable, i), logic.maxValueInColumn(dataTable, i));
        }
    }
}
