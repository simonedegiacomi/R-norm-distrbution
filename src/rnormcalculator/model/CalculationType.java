package rnormcalculator.model;

import java.awt.*;

/**
 * Created by Slava on 06/04/2017.
 */
public abstract class CalculationType {
    public static final int CALCULATION_TYPE_GREATER = 1;
    public static final int CALCULATION_TYPE_LESSER = 2;
    public static final int CALCULATION_TYPE_BETWEEN = 3;
    public static final int CALCULATION_TYPE_OUTSIDE = 4;

    public abstract double calculateValue();

    public abstract void calculateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim);

    public static CalculationType build(int calculationType) {
        if(calculationType == CALCULATION_TYPE_GREATER){
            return new GreatherThanCalculation();
        } else if(calculationType == CALCULATION_TYPE_LESSER) {
            return new LesserThanCalculation();
        } else if(calculationType == CALCULATION_TYPE_OUTSIDE) {
            return new OutsideCalculation();
        } else if(calculationType == CALCULATION_TYPE_BETWEEN) {
            return new BetweenCalculation();
        }

        throw new RuntimeException();
    }

}
