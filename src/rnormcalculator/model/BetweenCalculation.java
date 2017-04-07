package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;

/**
 * Created by Slava on 07/04/2017.
 */
public class BetweenCalculation extends CalculationType {

    @Override
    public double calculateValue() {
        return 0;
    }

    @Override
    public void calculateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {


        rect1.x = 0;
        rect1.width = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);

        rect2.x = DrawingUtils.complementary(rect1.width, imageDim, componentDim);
        rect2.width = rect1.width;
    }

}
