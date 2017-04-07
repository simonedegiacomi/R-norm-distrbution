package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;

/**
 * Created by Slava on 07/04/2017.
 */
public class OutsideCalculation extends CalculationType {

    @Override
    public double calculateValue() {
        return 0;
    }

    @Override
    public void calculateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
        if (x >= 0) {
            rect1.x = DrawingUtils.findXPositionOnAxis(-x, imageDim, componentDim);

            int rightX = DrawingUtils.complementary(rect1.x, imageDim, componentDim);
            rect1.width = rightX - rect1.x;
        } else { //x < 0
            rect1.x = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);

            int rightX = DrawingUtils.complementary(rect1.x, imageDim, componentDim);
            rect1.width = rightX - rect1.x;
        }

        //Rect 2 not used
        DrawingUtils.hideRect(rect2);

    }

}
