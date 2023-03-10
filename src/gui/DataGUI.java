package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A {@link JPanel} that is intended to be used as sub frame. It represents colum in the file or a part if it is stepped
 */
public class DataGUI extends JPanel {

    public static void main(String[] args) {
        //FOR TESTING!
        new DataGUI().setBasics("I", 0, 0,1);
    }
    private final JTextField name; //Name of column
    private final JLabel useAxis;
    private final JTextField axisName;
    private final JLabel[] rgbLabels;
    private final JTextField[] colors; //RGB
    private final JLabel preview;
    private final JLabel labelFrom;
    private final JLabel labelTo;

    public DataGUI() {
        super();
        this.setLayout(null);
        this.setSize(620, 40);
        FocusListener focusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                update();
            }

            @Override
            public void focusLost(FocusEvent e) {
                update();
            }
        };

        name = new JTextField();
        name.setLocation(10,10);
        name.setSize(60,20);
        name.addActionListener(e -> update());
        name.addFocusListener(focusListener);
        this.add(name);

        useAxis = new JLabel("useAxis:");
        useAxis.setLocation(80,10);
        useAxis.setSize(50,20);
        this.add(useAxis);

        axisName = new JTextField();
        axisName.setSize(60,20);
        axisName.setLocation(130, 10);
        this.add(axisName);

        rgbLabels = new JLabel[3];
        String[] rgb = {"r:","g:","b:"};
        for (int i = 0; i < 3; i++) {
            rgbLabels[i] = new JLabel(rgb[i]);
            rgbLabels[i].setSize(20,20);
            rgbLabels[i].setLocation(200+50*i,10);
            this.add(rgbLabels[i]);
        }

        colors = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            colors[i] = new JTextField("0");
            colors[i].setSize(30,20);
            colors[i].setLocation(215+50*i,10);
            colors[i].addActionListener(e -> update());
            colors[i].addFocusListener(focusListener);
            this.add(colors[i]);
        }

        preview = new JLabel();
        preview.setLocation(360,10);
        preview.setSize(70,20);
        this.add(preview);

        labelFrom = new JLabel("from: ");
        labelFrom.setSize(90,20);
        labelFrom.setLocation(440,10);
        this.add(labelFrom);

        labelTo = new JLabel("to: ");
        labelTo.setSize(90,20);
        labelTo.setLocation(530,10);
        this.add(labelTo);
    }

    void setBasics(String label, int axisNumber, double from, double to) {
        this.name.setText(label);
        this.axisName.setText(String.valueOf(axisNumber));
        labelFrom.setText("from: "+from);
        labelTo.setText("to: "+to);
        update();
        this.setVisible(true);
    }

    private void update() {
        preview.setText(name.getText());
        int[] color = getColor();
        preview.setForeground(new Color(color[0], color[1], color[2]));
    }

    public int[] getColor() {
        int[] color = new int[3];
        for (int i = 0; i < 3; i++) {
            try {
                color[i] = Integer.decode(colors[i].getText());
                if (color[i] > 255) {
                    colors[i].setText("255");
                    color[i] = 255;
                } else if (color[i] < 0) {
                    colors[i].setText("0");
                    color[i] = 0;
                }
            } catch (NumberFormatException ignored) {
                colors[i].setText("0");
            }
        }
        return color;
    }

    public int getAxis() {
        return Integer.decode(axisName.getText());
    }
}
