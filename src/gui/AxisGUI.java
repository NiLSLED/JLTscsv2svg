package gui;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link JPanel} the represents an Axis. According to this axis each datatable is plotted
 */
public class AxisGUI extends JPanel {
    public static void main(String[] args) {
        //FOR TESTING!
        new AxisGUI().setBasic("V", 0, 1);
    }
    JLabel titleName;
    JTextField name;
    JLabel labelFrom;
    JTextField from;
    JLabel labelTo;
    JTextField to;

    public AxisGUI() {
        super();
        this.setLayout(null);
        this.setSize(620, 40);

        titleName = new JLabel();
        titleName.setLocation(10, 10);
        titleName.setSize(100, 20);
        this.add(titleName);

        name = new JTextField();
        name.setLocation(110, 10);
        name.setSize(100, 20);
        this.add(name);

        labelFrom = new JLabel();
        labelFrom.setText("from:");
        labelFrom.setLocation(230, 10);
        labelFrom.setSize(50, 20);
        this.add(labelFrom);

        from = new JTextField();
        from.setLocation(270, 10);
        from.setText("0");
        from.setSize(70, 20);
        this.add(from);

        labelTo = new JLabel();
        labelTo.setText("To:");
        labelTo.setSize(40,20);
        labelTo.setLocation(350, 10);
        this.add(labelTo);

        to = new JTextField();
        to.setSize(70, 20);
        to.setLocation(380, 10);
        to.setText("1");
        this.add(to);
    }

    public void setBasic(String name, double from, double to) {
        this.name.setText(name);
        this.from.setText(Double.toString(from));
        this.to.setText(Double.toString(to));
        this.setVisible(true);
    }

    public double[] getAxisData() {
        return new double[] {Double.parseDouble(from.getText()), Double.parseDouble(to.getText())};
    }

}
