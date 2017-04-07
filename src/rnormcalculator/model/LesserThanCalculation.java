package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;

/**
 * Created by Slava on 06/04/2017.
 */
public class LesserThanCalculation extends CalculationType {
    @Override
    public double calculateValue() {
        return 0;
    }

    @Override
    public void calculateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
        rect1.x = 0;
        rect1.width = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);

        //Rect2 is not used here
        DrawingUtils.hideRect(rect2);
    }
}
