package rnormcalculator.gui;

import rnormcalculator.gui.components.NormalDistributionView;
import rnormcalculator.model.CalculationType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JSpinner mediaSpinner;
    private JSpinner varianzaSpinner;
    private JRadioButton prXMaggiore;
    private JRadioButton prXMinore;
    private JRadioButton prXInMezzo;
    private JRadioButton prXEstremi;
    private JButton calcolaButton;
    private NormalDistributionView distributionView;
    private JSpinner xSpinner;

    private CalculationType currentCalculationType;

    public MainFrame() {
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Binding spinners:
        mediaSpinner   .setModel(getGenericNumericSpinnerModel());
        varianzaSpinner.setModel(getGenericNumericSpinnerModel());
        xSpinner       .setModel(getGenericNumericSpinnerModel());

        //Button groups for calcualtion types
        ButtonGroup calculationTypesGroupy = new ButtonGroup();
        calculationTypesGroupy.add(prXMaggiore);
        calculationTypesGroupy.add(prXMinore);
        calculationTypesGroupy.add(prXInMezzo);
        calculationTypesGroupy.add(prXEstremi);

        calculationTypesGroupy.setSelected(prXMaggiore.getModel(), true);

        //Adding listeners:

        xSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateView();
            }
        });

        currentCalculationType = CalculationType.build(CalculationType.CALCULATION_TYPE_GREATER);

        prXMaggiore.addItemListener(getCalculationTypeChangeListener());
        prXMinore.addItemListener(  getCalculationTypeChangeListener());
        prXInMezzo.addItemListener( getCalculationTypeChangeListener());
        prXEstremi.addItemListener( getCalculationTypeChangeListener());

        //Forcing view drawing
        updateView();
    }

    private ItemListener getCalculationTypeChangeListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                currentCalculationType = findSelectedCalculationType(e.getSource());
                updateView();
            }
        };
    }

    private void updateView() {
        distributionView.update(getCurrentCalculationType(), getSelectedX());
    }

    private double getSelectedX() {
        return (double) xSpinner.getValue();
    }

    private CalculationType findSelectedCalculationType(Object source) {
        if(prXMaggiore.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_GREATER);
        } else if (prXMinore.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_LESSER);
        } else if (prXInMezzo.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_BETWEEN);
        } else if (prXEstremi.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_OUTSIDE);
        } else {
            throw new RuntimeException("CalculationType not recognized!");
        }

    }

    private double getVarianza() {
        return (double) varianzaSpinner.getValue();
    }

    private double getMedia() {
        return (double) mediaSpinner.getValue();
    }

    private SpinnerNumberModel getGenericNumericSpinnerModel() {
        return new SpinnerNumberModel(1, -5000, 5000, 0.1);
    }

    public static void main(String[] args) {
        MainFrame dialog = new MainFrame();
        dialog.pack();
        dialog.setVisible(true);
    }

    public CalculationType getCurrentCalculationType() {
        return currentCalculationType;
    }
}
