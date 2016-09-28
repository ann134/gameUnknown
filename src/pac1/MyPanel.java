package pac1;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPanel extends JPanel{
    public BufferedImage smile;

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawImage(g2d);
        drawFigure(g2d);

        drawAxes(g2d);
        g2d.rotate(Math.PI / 10);
        drawAxes(g2d);
        g2d.translate(100, 200);
        drawAxes(g2d);
    }

    public MyPanel() throws IOException {
        smile = ImageIO.read(new File("smile.png"));
    }

    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(0, 0, 100, 0);
        g2d.setColor(Color.RED);
        g2d.drawLine(0, 0, 0, 100);
    }

    private void drawFigure(Graphics2D g){
        g.setColor(new Color(50, 50, 50));
        g.fillRect(250, 250, 300, 300);

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 980, 1920, 300);
    }

    private void drawImage(Graphics2D g){
        /*
        Graphics2D gg = (Graphics2D) g.create();
        g.translate(100, 100);
        gg.rotate(Math.PI / 4, 1000, 300);
        gg.drawImage(smile, 1000, 300, null);
        gg.dispose();
        */

        AffineTransform transform = g.getTransform();
        g.rotate(Math.PI / 4, 1000, 300);
        g.drawImage(smile, 1000, 300, null);
        g.setTransform(transform);
    }

}
