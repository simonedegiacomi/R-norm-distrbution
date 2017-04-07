package rnormcalculator.gui.components;

import rnormcalculator.model.CalculationType;
import rnormcalculator.utils.DrawingUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Slava on 06/04/2017.
 */
public class NormalDistributionView extends JComponent implements ComponentListener {

    private Rectangle rect1, rect2;

    private BufferedImage normalImage;

    private double currentX = 0;

    public NormalDistributionView() {
        try {
            normalImage = ImageIO.read(getClass().getResource("norm-distrib.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load normal distribution image!");
        }

        rect1 = new Rectangle();
        rect2 = new Rectangle();

        addComponentListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(normalImage.getWidth(), normalImage.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.fillRect(rect1.x, rect1.y, rect1.width, rect1.height);
        g.fillRect(rect2.x, rect2.y, rect2.width, rect2.height);

        g.drawImage(normalImage, 0, 0, this);

        int xPosition = DrawingUtils.findXPositionOnAxis(currentX, getPreferredSize(), getSize())-3;

        g.setColor(Color.BLACK);
        g.drawString("X", xPosition, 218);

    }

    public void update(CalculationType calculationType, double x) {
        this.currentX = x;
        calculationType.calculateRectsPosition(x, rect1, rect2, getPreferredSize(), getSize());

        repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //Quando ridimensioniamo il componente, i rettangoli potrebbero non coprire più per intero l'altezza
        //dell'immagine. Aggioriamo le dimensioni dei retangoli qui:
        rect1.height = getHeight();
        rect2.height = getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }
}
