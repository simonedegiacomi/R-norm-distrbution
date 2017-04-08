package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;
import java.util.Locale;

/**
 * Created by Slava on 06/04/2017.
 */
public class LesserThanCalculation extends CalculationType {

    @Override
    public void updateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
        rect1.x = 0;
        rect1.width = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);

        //Rect2 is not used here
        DrawingUtils.hideRect(rect2);
    }

    @Override
    public String generateRInstruction(double x, double mean, double deviation) {
        //The Locale.US here prevents the double from being parsed with "," as separator.
        return String.format(Locale.UK,"pnorm(%f, %f, %f)", x, mean, deviation);
    }

}
