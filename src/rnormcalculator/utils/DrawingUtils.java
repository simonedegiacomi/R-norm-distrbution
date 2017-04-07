package rnormcalculator.utils;

import java.awt.*;

/**
 * Created by Slava on 06/04/2017.
 */
public class DrawingUtils {
    protected static final double minValueShownInImage = -3.5;
    protected static final double maxValueShownInImage =  3.5;

    public static int calculateSpaceUntilRightBorder(int fromPosition, Dimension imageDim) {
        return (int) (imageDim.getWidth() - fromPosition);
    }

    public static int calculateSpaceUntilLeftBorder(int fromPosition) {
        return fromPosition;
    }

    public static void hideRect(Rectangle rect) {
        rect.width = 0;
    }

    public static int findXPositionOnAxis(double x, Dimension imageDim, Dimension componentDim) {
        int minX = getStartingX(imageDim, componentDim);

        if (x <= minValueShownInImage) {
            return minX;
        } else if (x >= maxValueShownInImage) {
            return (int) (minX + imageDim.getWidth());
        } else {
            double pixelsPerUnit = calculateUnitsPerPixel(imageDim);
            double zeroedX = x - minValueShownInImage;

            return (int) (minX + (zeroedX * pixelsPerUnit));
        }
    }

    private static double calculateUnitsPerPixel(Dimension imageDim) {
        return imageDim.getWidth() / (maxValueShownInImage-minValueShownInImage);
    }

    private static int getStartingX(Dimension imageDim, Dimension componentDim) {
        return (componentDim.width - imageDim.width)/2;
    }

    public static int complementary(int width, Dimension imageDim, Dimension componentDim) {
        int dimDiff = imageDim.width - componentDim.width;
        return dimDiff + (imageDim.width - width);
    }
}
