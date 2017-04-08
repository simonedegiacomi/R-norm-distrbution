package rnormcalculator.gui;

import rnormcalculator.gui.components.NormalDistributionView;
import rnormcalculator.model.CalculationType;
import rnormcalculator.model.delegator.InvalidResponseException;
import rnormcalculator.model.delegator.RDelegator;

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
    private JSpinner mediaSpinner;
    private JSpinner varianzaSpinner;
    private JRadioButton prXMaggiore;
    private JRadioButton prXMinore;
    private JRadioButton prXInMezzo;
    private JRadioButton prXEstremi;
    private JButton calcolaButton;
    private NormalDistributionView distributionView;
    private JSpinner xSpinner;
    private JTextField resultField;
    private JButton copyButton;

    /**
     * The calculation type that's actually selected. It gets updated on selection.
     */
    private CalculationType currentCalculationType;

    private RDelegator delegator;

    public MainFrame() {
        setTitle("R-norm distribution");
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Creating fields:
        delegator = RDelegator.createFromEnvironment();
        currentCalculationType = CalculationType.build(CalculationType.CALCULATION_TYPE_GREATER);

        //Binding spinners:
        mediaSpinner   .setModel(getGenericNumericSpinnerModel(0));
        varianzaSpinner.setModel(getGenericNumericSpinnerModel(1));
        xSpinner       .setModel(getGenericNumericSpinnerModel(1));

        //Button groups for calcualtion types
        ButtonGroup calculationTypesGroup = new ButtonGroup();
        calculationTypesGroup.add(prXMaggiore);
        calculationTypesGroup.add(prXMinore);
        calculationTypesGroup.add(prXInMezzo);
        calculationTypesGroup.add(prXEstremi);

        calculationTypesGroup.setSelected(prXMaggiore.getModel(), true);

        //Adding listeners:
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateView();
            }
        });

        xSpinner.addChangeListener(e -> updateView());

        prXMaggiore.addItemListener(getCalculationTypeChangeListener());
        prXMinore.addItemListener(  getCalculationTypeChangeListener());
        prXInMezzo.addItemListener( getCalculationTypeChangeListener());
        prXEstremi.addItemListener( getCalculationTypeChangeListener());

        copyButton.addActionListener(e -> resultField.copy());

        calcolaButton.addActionListener(event -> {
            try {
                updateResultField("Sto contattando R...");

                String function = currentCalculationType.generateRInstruction(getSelectedX(), getMedia(), getVarianza());
                double result = delegator.delegate(function);

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

    private SpinnerNumberModel getGenericNumericSpinnerModel(int defaultValue) {
        return new SpinnerNumberModel(defaultValue, -5000, 5000, 0.1);
    }

    private CalculationType getCurrentCalculationType() {
        return currentCalculationType;
    }
}
