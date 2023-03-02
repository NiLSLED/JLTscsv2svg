package gui;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link JFrame} the represents an Axis. According to this axis each datatable is plotted
 */
public class AxisGUI extends JPanel {
    private JLabel titleName;
    protected JTextField name;
    private JLabel labelFrom;
    protected JTextField from;
    private JLabel labelTo;
    protected JTextField to;

    public AxisGUI() {
        super();
        super.setLayout(null);

        titleName = new JLabel();
        titleName.setLocation(10, 10);
        titleName.setSize(20, 100);
        super.add(titleName);

        name = new JTextField();
        name.setLocation(10, 110);
        name.setSize(20, 100);
        super.add(name);

        labelFrom = new JLabel();
        labelFrom.setText("from:");
        labelFrom.setLocation(10, 230);
        labelFrom.setSize(50, 20);
        super.add(labelFrom);

        from = new JTextField();
        from.setText("From:");
        from.setLocation(10, 290);
        from.setSize(50, 20);
        super.add(from);

        labelTo = new JLabel();
        labelTo.setText("To:");
        labelTo.setSize(40,20);
        labelTo.setLocation(10, 350);
        super.add(labelTo);

        to = new JTextField();
        to.setSize(50, 20);
        to.setLocation(10, 400);
        super.add(to);
    }

    public void setBasic(String name, double from, double to) {
        this.name.setText(name);
        this.from.setText(Double.toString(from));
        this.to.setText(Double.toString(to));
        super.setVisible(true);
    }
}
