package rnormcalculator.model;

import rnormcalculator.gui.components.NormalDistributionView;

import java.awt.*;

/**
 * A calculation type is an abstraction of what kind of information we want from the normal distribution.<br>
 * Actually this class has two duties:<br>
 * <ul>
 * <li>Building the relative R instruction</li>
 * <li>Being able to draw itself on the {@link NormalDistributionView}</li>
 * </ul>
 */
public abstract class CalculationType {
    public static final int CALCULATION_TYPE_GREATER = 1;
    public static final int CALCULATION_TYPE_LESSER = 2;
    public static final int CALCULATION_TYPE_OUTSIDE = 3;
    public static final int CALCULATION_TYPE_BETWEEN = 4;

    /**
     * Recalculates the positions of the rectangles that will be used to display the data we want in the
     * {@link NormalDistributionView}
     * @param x the "x" of the distribution
     * @param rect1 the first rectangle
     * @param rect2 the second rectangle
     * @param imageDim the dimensions of the image
     * @param componentDim the dimension of the {@link NormalDistributionView}
     */
    public abstract void updateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim);

    /**
     * Crates a new instance of calculation type based on the parameter
     */
    public static CalculationType build(int calculationType) {
        if (calculationType == CALCULATION_TYPE_GREATER) {
            return new GreatherThanCalculation();
        } else if (calculationType == CALCULATION_TYPE_LESSER) {
            return new LesserThanCalculation();
        } else if (calculationType == CALCULATION_TYPE_BETWEEN) {
            return new BetweenCalculation();
        } else if (calculationType == CALCULATION_TYPE_OUTSIDE) {
            return new OutsideCalculation();
        }

        throw new RuntimeException();
    }

    /**
     * Creates the instruction that will be executed in R.
     */
    public abstract String generateRInstruction(double x, double mean, double deviation);

}
