package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;

/**
 * Created by Slava on 06/04/2017.
 */
class GreatherThanCalculation extends CalculationType {

    @Override
    public void updateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
        rect1.x     = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);
        rect1.width = DrawingUtils.calculateSpaceUntilRightBorder(rect1.x, imageDim);

        //Rect2 is not used here
        DrawingUtils.hideRect(rect2);

    }

}
