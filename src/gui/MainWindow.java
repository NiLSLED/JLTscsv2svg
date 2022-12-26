package gui;

import javax.swing.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    JTextField csvFilePath;
    JButton load;
    ArrayList<DataGUI> dataGUIS;
    ArrayList<AxisGUI> axisGUIS;
    JTextField svgFilePath;
    JButton export;

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
        super("JLTscsv2svg");
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
     * Loads data initial into {@link MainWindow#dataGUIS} and {@link MainWindow#axisGUIS}
     */
    private void loadData() {
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
    }

    private void loadIntoDataGUI() {

    }

    private void loadIntoAxisGUI() {

    }
}
