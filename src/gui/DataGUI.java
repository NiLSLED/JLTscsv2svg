package gui;

import javax.swing.*;
import java.awt.*;

public class DataGUI extends JFrame {
    JTextField label;
    JLabel useAxis;
    JTextField axisName;
    JLabel[] rgbLabels;
    JTextField[] colors; //RGB
    JLabel preview;
    JLabel labelFrom;
    JLabel labelTo;

    public DataGUI() {
        this.setSize(200, 40);
        label = new JTextField();

        useAxis = new JLabel("useAxis:");

        axisName = new JTextField();

        rgbLabels = new JLabel[3];

        colors = new JTextField[3];

        preview = new JLabel();

        labelFrom = new JLabel("from: ");
        labelTo = new JLabel("to: ");
    }

    void setBasics(String label, int axisNumber, double from, double to) {
        this.label.setText(label);
        this.axisName.setText(String.valueOf(axisNumber));
        this.setVisible(true);
        labelFrom.setText("from: "+from);
        labelTo.setText("to: "+to);
    }
}
