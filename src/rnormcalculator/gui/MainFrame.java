package rnormcalculator.gui;

import rnormcalculator.delegator.ConfigurationInvalidException;
import rnormcalculator.gui.components.NormalDistributionView;
import rnormcalculator.model.CalculationType;
import rnormcalculator.delegator.InvalidResponseException;
import rnormcalculator.delegator.RDelegator;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemListener;
import java.io.IOException;


/**
 * The form where all the magic happens.
 */
public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JSpinner meanSpinner;
    private JSpinner standardDeviationSpinner;
    private JRadioButton prXGreater;
    private JRadioButton prXLesser;
    private JRadioButton prXBetween;
    private JRadioButton prXOutside;
    private JButton calcolaButton;
    private NormalDistributionView distributionView;
    private JSpinner xSpinner;
    private JTextField resultField;

    /**
     * The calculation type that's actually selected. It gets updated on selection.
     */
    private CalculationType currentCalculationType;

    private RDelegator delegator;

    public MainFrame(RDelegator delegator) throws ConfigurationInvalidException {
        //Binding delegator
        this.delegator = delegator;

        setTitle("R-norm distribution");
        setContentPane(contentPane);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Creating fields:
        currentCalculationType = CalculationType.build(CalculationType.CALCULATION_TYPE_GREATER);

        //Binding spinners:
        meanSpinner             .setModel(getGenericNumericSpinnerModel(0));
        standardDeviationSpinner.setModel(getGenericNumericSpinnerModel(1));
        xSpinner                .setModel(getGenericNumericSpinnerModel(1));

        //Button groups for calculation types
        ButtonGroup calculationTypesGroup = new ButtonGroup();
        calculationTypesGroup.add(prXGreater);
        calculationTypesGroup.add(prXLesser);
        calculationTypesGroup.add(prXBetween);
        calculationTypesGroup.add(prXOutside);

        calculationTypesGroup.setSelected(prXGreater.getModel(), true);

        //Adding listeners:
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateView();
            }
        });

        xSpinner.addChangeListener(e -> updateView());

        prXGreater.addItemListener(getCalculationTypeChangeListener());
        prXLesser.addItemListener(  getCalculationTypeChangeListener());
        prXBetween.addItemListener( getCalculationTypeChangeListener());
        prXOutside.addItemListener( getCalculationTypeChangeListener());

        calcolaButton.addActionListener(event -> {
            try {
                updateResultField("Sto contattando R...");

                String function = currentCalculationType.generateRInstruction(getSelectedX(), getMean(), getStandardDeviation());
                double result = this.delegator.delegate(function);

                updateResultField(String.valueOf(result));
            } catch (IOException e) {
                e.printStackTrace();

                updateResultField("Errore: " + e.getMessage());
            } catch (InvalidResponseException e) {
                e.printStackTrace();

                updateResultField("R ha risposto in maniera non corretta: " + e.getMessage());
            } catch (Exception e) {
                updateResultField("Si è verificato un errore imprevisto: " + e.getMessage());
            }
        });
    }

    private void updateResultField(String text) {
        resultField.setText(text);
    }

    private ItemListener getCalculationTypeChangeListener() {
        return e -> {
            currentCalculationType = findSelectedCalculationType(e.getSource());
            updateView();
        };
    }

    private void updateView() {
        distributionView.update(getCurrentCalculationType(), getSelectedX());
    }

    private double getSelectedX() {
        //This method is not called getX() because it would clash with the already existing getX() method that return
        //the X position of this frame on the screen.
        return (double) xSpinner.getValue();
    }

    private CalculationType findSelectedCalculationType(Object source) {
        if(prXGreater.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_GREATER);
        } else if (prXLesser.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_LESSER);
        } else if (prXBetween.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_BETWEEN);
        } else if (prXOutside.equals(source)){
            return CalculationType.build(CalculationType.CALCULATION_TYPE_OUTSIDE);
        } else {
            throw new RuntimeException("CalculationType not recognized!");
        }
    }

    private double getStandardDeviation() {
        return (double) standardDeviationSpinner.getValue();
    }

    private double getMean() {
        return (double) meanSpinner.getValue();
    }

    private SpinnerNumberModel getGenericNumericSpinnerModel(int defaultValue) {
        return new SpinnerNumberModel(defaultValue, -5000, 5000, 0.1);
    }

    private CalculationType getCurrentCalculationType() {
        return currentCalculationType;
    }
}
