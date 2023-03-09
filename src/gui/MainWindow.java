package gui;

import javax.swing.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    JTextField csvFilePath;
    JButton load;
    ArrayList<DataGUI> dataGUIS;
    ArrayList<AxisGUI> axisGUIS;
    JButton addAxisButton;
    JTextField svgFilePath;
    JButton exportButton;
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
        csvFilePath.setText("/home/nils/Dokumente/Uni/2/PR_ST/Draft4.txt");
        csvFilePath.setLocation(10,10);
        csvFilePath.setSize(300,20);
        super.add(csvFilePath);

        load = new JButton("load");
        load.setSize(100,20);
        load.setLocation(350, 10);
        load.addActionListener(e -> loadData());
        super.add(load);

        addAxisButton = new JButton("Add Axis");
        addAxisButton.setSize(100, 20);
        addAxisButton.addActionListener(e -> addAxis());
        addAxisButton.setVisible(false);
        super.add(addAxisButton);

        svgFilePath = new JTextField();
        svgFilePath.setSize(300, 20);
        svgFilePath.setVisible(false);
        super.add(svgFilePath);

        exportButton = new JButton("export");
        exportButton.setSize(100, 20);
        exportButton.setVisible(false);
        exportButton.addActionListener(e -> export());
        super.add(exportButton);

        super.setVisible(true);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addAxis() {
        AxisGUI axisGUI = new AxisGUI();
        axisGUIS.add(axisGUI);
        reRender();
    }

    /**
     * Loads data initial into {@link MainWindow#dataGUIS} and {@link MainWindow#axisGUIS}.
     * It opens the file specified by {@link MainWindow#svgFilePath} and phrases its content.
     */
    private void loadData() {
        for (DataGUI gui: dataGUIS) {
            super.remove(gui);
        }
        for (AxisGUI gui: axisGUIS) {
            super.remove(gui);
        }
        dataGUIS.clear();
        axisGUIS.clear();
        dataTableString = logic.readTableFile(csvFilePath.getText());
        dataTable = logic.tableToDouble(dataTableString);
        loadIntoAxisGUI();
        loadIntoDataGUI();
        svgFilePath.setText(csvFilePath.getText() + ".svg");
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
        addAxisButton.setLocation(10, (1+dataGUIS.size()+ axisGUIS.size()) *40);
        addAxisButton.setVisible(true);
        svgFilePath.setLocation(10, (2+dataGUIS.size()+ axisGUIS.size()) *40);
        exportButton.setLocation(350,(2+dataGUIS.size()+axisGUIS.size())*40);
        svgFilePath.setVisible(true);
        exportButton.setVisible(true);
        super.setSize(700, (4+dataGUIS.size()+axisGUIS.size())*40);
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
        for (int i = 0; i < dataTableString[0].length; i++) {
            AxisGUI axisGUI = new AxisGUI();
            axisGUI.setBasic(dataTableString[0][i], logic.minValueInColumn(dataTable, i), logic.maxValueInColumn(dataTable, i));
            axisGUIS.add(axisGUI);
            if (i == 0) {
                axisGUI.titleName.setText("y");
            } else {
                axisGUI.titleName.setText("x"+i);
            }
        }
    }

    private void export() {
        logic.startBuild(900, 600, dataTable, getAxisData());
        for (int i = 1; i < dataGUIS.size(); i++) {
            logic.addData(dataGUIS.get(i).getColor(), i, dataGUIS.get(i).getAxis());
        }
        logic.exportBuild(svgFilePath.getText());
    }

    private double[][] getAxisData() {
        double[][] axisData = new double[axisGUIS.size()][];
        for (int i = 0; i < axisGUIS.size(); i++) {
            axisData[i] = axisGUIS.get(i).getAxisData();
        }
        return axisData;
    }
}
