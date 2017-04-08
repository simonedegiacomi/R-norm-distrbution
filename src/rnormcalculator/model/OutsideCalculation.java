package rnormcalculator.model;

import rnormcalculator.utils.DrawingUtils;

import java.awt.*;
import java.util.Locale;

/**
 * Created by Slava on 07/04/2017.
 */
public class OutsideCalculation extends CalculationType {

    @Override
    public void updateRectsPosition(double x, Rectangle rect1, Rectangle rect2, Dimension imageDim, Dimension componentDim) {
        if(x > 0){
            x = -x;
        }

        rect1.x = 0;
        rect1.width = DrawingUtils.findXPositionOnAxis(x, imageDim, componentDim);

        rect2.x = DrawingUtils.complementary(rect1.width, imageDim, componentDim);
        rect2.width = rect1.width;
    }

    @Override
    public String generateRInstruction(double x, double mean, double deviation) {
        if(x > 0){
            x = -x;
        }

        //The Locale.US here prevents the double from being parsed with "," as separator.
        return String.format(Locale.UK,"2 * pnorm(%f, %f, %f)", x, mean, deviation);
    }

}
