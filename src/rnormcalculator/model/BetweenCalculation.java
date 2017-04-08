package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;
import java.util.Locale;

/**
 * Created by Slava on 07/04/2017.
 */
public class BetweenCalculation extends CalculationType {

    @Override
    public void updateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
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

    @Override
    public String generateRInstruction(double x, double mean, double deviation) {
        //The Locale.US here prevents the double from being parsed with "," as separator.
        if (x > 0) {
            x = -x;
        }
        return String.format(Locale.UK,"2*(0.5-pnorm(%f, %f, %f))", x, mean, deviation);
    }

}
