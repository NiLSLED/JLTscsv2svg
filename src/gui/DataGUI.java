package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A {@link JFrame} that is intended to be used as sub frame. It represents colum in the file or a part if it is stepped
 */
public class DataGUI extends JPanel {

    public static void main(String[] args) {
        //FOR TESTING!
        new DataGUI().setBasics("I", 0, 0,1);
    }
    JTextField name; //Name of column
    JLabel useAxis;
    JTextField axisName;
    JLabel[] rgbLabels;
    JTextField[] colors; //RGB
    JLabel preview;
    JLabel labelFrom;
    JLabel labelTo;

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
        this.setVisible(true);
    }

    private void update() {
        System.out.println("update");
        preview.setText(name.getText());
        preview.setForeground(new Color(Integer.decode(colors[0].getText()), Integer.decode(colors[1].getText()), Integer.decode(colors[2].getText())));
    }

    String[] collectData() {
        String[] data = new String[5];
        data[0] = name.getText();
        data[1] = axisName.getText();
        data[2] = colors[0].getText();
        data[3] = colors[1].getText();
        data[4] = colors[2].getText();
        return data;
    }
}
