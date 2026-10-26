package ru.vsu.cs.nazarova;

import java.awt.*;
import java.awt.geom.*;

public class Cat {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color furColor;
    private boolean isLookingRight;
    private final double baseWidth = 177;
    private final double baseHeight = 300;
    private final double baseScale = 3.4;
    private int blinkTicksFromStart;
    private boolean isEyesClosed = false;

    public Cat(int x, int y, int width, int height, Color furColor, boolean isLookingRight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.furColor = furColor;
        this.isLookingRight = isLookingRight;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        double scaleX = width / baseWidth;
        double scaleY = height / baseHeight;
        double scale = baseScale * Math.min(scaleX, scaleY);

        // mirroring

        AffineTransform catTransform = g2d.getTransform(); // сохраняем текущее состояние холста, чтобы потом вернуть всё назад

        if (!isLookingRight) {
            g2d.translate(this.x + this.width, this.y); // точку отсчета сдвинули на x = 277, y = 600 (start лапы черн.кошки)
            g2d.scale(-1, 1); // отражаем эту точку относительно OX, теперь (0,0) в правом нижнем углу кошки (в лапе ее)
        } else {
            g2d.translate(this.x, this.y); // если смотрит вправо, то сдвигаем начало координат (0,0) в позицию кошки x = 100, y = 600
        }

        // neck

        AffineTransform neckTransform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (16 * scale), -(int) (65 * scale));
        g2d.rotate(Math.toRadians(20));
        g2d.fillOval((int) (-20/2 * scale), (int) (-47/2 * scale), (int) (20 * scale), (int) (55 * scale));

        // neck circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (-20/2 * scale), (int) (-47/2 * scale), (int) (20 * scale), (int) (55 * scale));

        g2d.setTransform(neckTransform);

        // body

        AffineTransform bodyTransform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (37 * scale), -(int) (25 * scale));   // сдвигаем центр координат (0,0) в центр тела кошки
        g2d.rotate(Math.toRadians(-120));
        g2d.fillOval(- (int) (50/2 * scale), - (int) (55/2 * scale), (int) (70 * scale), (int) (35 * scale));  // сдвиг по x, y я взяла как с моей картинки, а ширину и высоту произвольные, чтобы пропорциональнее смотрелось

        // body circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(- (int) (50/2 * scale), - (int) (55/2 * scale), (int) (70 * scale), (int) (35 * scale));

        g2d.setTransform(bodyTransform);   // возвращаем холст в исходное состояние, в неповернутое

        // extra parts of neck

        AffineTransform neckPart1Transform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (17 * scale), (int) (-65 * scale)); // мини эллипс1 для перекрытия контура между телом и шеей
        g2d.rotate(Math.toRadians(21));
        g2d.fillOval((int) (-20/2 * scale), (int) (-23.5/2 * scale), (int) (19.4 * scale), (int) (35 * scale));

        g2d.setTransform(neckPart1Transform);

        AffineTransform neckPart2Transform = g2d.getTransform();

        g2d.translate((int) (15.59 * scale), (int) (-63 * scale)); // мини эллипс2 для перекрытия контура между телом и шеей
        g2d.rotate(Math.toRadians(24));
        g2d.fillOval((int) (-20/2 * scale), (int) (-23.5/2 * scale), (int) (7.2 * scale), (int) (26.79 * scale));

        g2d.setTransform(neckPart2Transform);

        // head

        AffineTransform headTransform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (34 * scale), (int) (-81 * scale));
        g2d.rotate(Math.toRadians(55));
        g2d.fillOval((int) (-26/2 * scale), (int) (-10/2 * scale), (int) (30 * scale), (int) (20 * scale));

        // head circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (-26/2 * scale), (int) (-10/2 * scale), (int) (30 * scale), (int) (20 * scale));

        g2d.setTransform(headTransform);

        // extra parts of head

        AffineTransform headPart1Transform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (17 * scale), (int) (-66 * scale)); // мини эллипс1 (шея) для перекрытия линии между головой и шеей
        g2d.rotate(Math.toRadians(32));
        g2d.fillOval((int) (-20/2 * scale), (int) (-47/2 * scale), (int) (18 * scale), (int) (37 * scale));

        g2d.setTransform(headPart1Transform);

        AffineTransform headPart2Transform = g2d.getTransform();

        g2d.translate((int) (18 * scale), (int) (-66 * scale)); // мини эллипс2 (шея) для перекрытия линии между головой и шеей
        g2d.rotate(Math.toRadians(32));
        g2d.fillOval((int) (-20/2 * scale), (int) (-47/2 * scale), (int) (7.2 * scale), (int) (35.1 * scale));

        g2d.setTransform(headPart2Transform);

        // ears

        g2d.setColor(furColor);
        int[] pointsX1 = {(int) (17 * scale), (int) (27 * scale), (int) (30 * scale)}; // подбираем цифры, сохраняя пропорции как на фотке с сеткой
        int[] pointsY1 = {(int) (-84 * scale), (int) (-85 * scale), (int) (-100 * scale)};

        int[] pointsX2 = {(int) (27 * scale), (int) (36 * scale), (int) (35 * scale)};
        int[] pointsY2 = {(int) (-89 * scale), (int) (-97 * scale), (int) (-86 * scale)};

        g2d.fillPolygon(pointsX1, pointsY1, 3);
        g2d.fillPolygon(pointsX2, pointsY2, 3);

        // ears circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke((2)));
        g2d.drawLine((int) (17 * scale), (int) (-84 * scale), (int) (30 * scale), (int) (-100 * scale));
        g2d.drawLine((int) (28 * scale), (int) (-90 * scale), (int) (30 * scale), (int) (-100 * scale));
        g2d.drawLine((int) (27 * scale), (int) (-89 * scale), (int) (36 * scale), (int) (-97 * scale));
        g2d.drawLine((int) (35 * scale), (int) (-86 * scale), (int) (36 * scale), (int) (-97 * scale));

        // eyes

         // white base

        AffineTransform eyeBaseTransform = g2d.getTransform();

        g2d.setColor(new Color(171, 171, 171));
        g2d.translate((int) (34 * scale), (int) (-79 * scale));
        g2d.rotate(Math.toRadians(-120));
        g2d.fillOval((int) (-12/2 * scale), (int) (-5/2 * scale), (int) (12 * scale), (int) (5 * scale));

        g2d.setTransform(eyeBaseTransform);

         // eyeliner

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine((int)(33 * scale), (int)(-84.5 * scale), (int)(29 * scale), (int)(-86 * scale)); // задние стрелки
        g2d.drawLine((int)(33 * scale), (int)(-77.1 * scale), (int)(29 * scale), (int)(-86 * scale));

        g2d.drawLine((int)(33.5 * scale), (int)(-77 * scale), (int)(39 * scale), (int)(-72 * scale)); // передние стрелки
        g2d.drawLine((int)(37 * scale), (int)(-80.5 * scale), (int)(39 * scale), (int)(-72 * scale));

         // iris

        AffineTransform irisTransform = g2d.getTransform();

        g2d.setColor(Color.BLACK);
        g2d.translate((int) (35 * scale), - (int) (79 * scale));
        g2d.rotate(Math.toRadians(-120));
        g2d.fillOval((int) (-4.5/2 * scale), (int) (-4.5/2 * scale), (int) (4.7 * scale), (int) (4.7 * scale));

        g2d.setTransform(irisTransform);

         // pupil

        if (!isEyesClosed) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        AffineTransform pupilTransform = g2d.getTransform();

        g2d.translate((int) (36 * scale), - (int) (79.1 * scale));
        g2d.rotate(Math.toRadians(-105));
        g2d.fillOval((int) (-1.5/2 * scale), (int) (-1.5/2 * scale), (int) (1.5 * scale), (int) (1.5 * scale));

        g2d.setTransform(pupilTransform);


        // eyes circuit

        AffineTransform eyeCircuit1Transform = g2d.getTransform();

        g2d.setStroke(new BasicStroke(2));

        if (isLookingRight) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        g2d.translate((int) (34 * scale), (int) (-79 * scale));
        g2d.rotate(Math.toRadians(-120));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc((int) (-12/2 * scale), (int) (-5/2 * scale), (int) (12 * scale), (int) (5 * scale), 20, 360);

        g2d.setTransform(eyeCircuit1Transform);

        // nose

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        int[] pointsX = {(int)(40 * scale), (int)(42 * scale), (int)(44 * scale)};
        int[] pointsY = {(int)(-70 * scale), (int)(-64 * scale), (int)(-70 * scale)};
        g2d.fillPolygon(pointsX, pointsY, 3);

        // mouth

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke((int) 2.9));
        g2d.drawLine((int)(38 * scale), (int)(-65 * scale), (int)(37 * scale), (int)(-67 * scale));
        g2d.drawLine((int)(36.3 * scale), (int)(-66.3 * scale), (int)(37.3 * scale), (int)(-67.3 * scale));
        g2d.drawLine((int)(38 * scale), (int)(-65 * scale), (int)(42 * scale), (int)(-65 * scale));

        // whiskers

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(1));

            // left whisker
        g2d.drawLine((int)(36 * scale), (int)(-69.6 * scale), (int)(29 * scale), (int)(-66.6 * scale));
        g2d.drawLine((int)(29 * scale), (int)(-66.6 * scale), (int)(26 * scale), (int)(-58.6 * scale));

            // middle whisker
        g2d.drawLine((int)(37 * scale), (int)(-69.6 * scale), (int)(29.4 * scale), (int)(-64.9 * scale));
        g2d.drawLine((int)(29.4 * scale), (int)(-64.9 * scale), (int)(27 * scale), (int)(-56.6 * scale));

           // right whisker
        g2d.drawLine((int)(36.6 * scale), (int)(-69.0 * scale), (int)(30 * scale), (int)(-63.6 * scale));
        g2d.drawLine((int)(30 * scale), (int)(-63.6 * scale), (int)(29 * scale), (int)(-57.6 * scale));

        // forefeet

        g2d.setColor(furColor);
        g2d.fillRect(0, 0, (int) (5 * scale), (int) (4 * scale));
        g2d.fillRect((int) (5 * scale), (int) (1 * scale), (int) (11 * scale), (int) (5 * scale));

        // forepaws

        g2d.setColor(furColor);
        g2d.fillRect((int) (5 * scale), (int) (-29 * scale), (int) (5 * scale), (int) (30 * scale));
        g2d.fillRect((int) (11 * scale), (int) (-19 * scale), (int) (5 * scale), (int) (23 * scale));

        // forepaws circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine((int) (5 * scale), (int) (-27.9 * scale), (int) (5 * scale), (int) (5 * scale)); //обводка левой ноги
        g2d.drawLine((int) (10 * scale), (int) (-18 * scale), (int) (10 * scale), (int) (1 * scale));

        g2d.drawLine((int) (11 * scale), (int) (-17 * scale), (int) (11 * scale), (int) (1 * scale));
        g2d.drawLine((int) (16 * scale), (int) (-10 * scale), (int) (16 * scale), (int) (5 * scale));

        // feet circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, (int) (5 * scale), (int) (4 * scale));
        g2d.drawRect((int) (5 * scale), (int) (1 * scale), (int) (11 * scale), (int) (5 * scale));


        // tail

        g2d.setColor(furColor);

        double extX = -20 * scale; // беру числа, немного отличающиеся от моих дуг, чтобы эллипсы точно создали кольцо, т.е. один был внутри другого
        double extY = -6 * scale;
        double extW = 65 * scale;
        double extH = 25 * scale;

        double intX = -14 * scale;
        double intY = -4 * scale;
        double intW = 50 * scale;
        double intH = 15 * scale;

        Area externalEllipse = new Area(new Ellipse2D.Double(extX, extY, extW, extH));
        Area internalEllipse = new Area(new Ellipse2D.Double(intX, intY, intW, intH));
        Area tailShape = new Area(externalEllipse);
        tailShape.subtract(internalEllipse); // big ellipse - small = circle

        Rectangle2D rectCutter = new Rectangle2D.Double(extX - 50 * scale, extY - 200 * scale, extW + 45 * scale, (205 * scale));

        Area cutArea = new Area(rectCutter);
        tailShape.subtract(cutArea);

//        Color oldColor = g2d.getColor(); //отрисовка rectCutter
//        g2d.setColor(Color.RED);
//        g2d.draw(rectCutter);
//        g2d.setColor(oldColor);

        g2d.fill(tailShape);
        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.draw(tailShape);

        // bodyThigh

        AffineTransform bodyThighTransform = g2d.getTransform();

        g2d.setColor(furColor);
        g2d.translate((int) (33 * scale), -(int) (10 * scale));   //сдвигаем центр координат (0,0) в центр тела кошки
        g2d.rotate(Math.toRadians(245));
        g2d.fillOval((int) (-34/2 * scale), (int) (-25/2 * scale), (int) (34 * scale), (int) (25 * scale));


        // bodyThigh circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (-34/2 * scale), (int) (-25/2 * scale), (int) (34 * scale), (int) (25 * scale));

        g2d.setTransform(bodyThighTransform);

        // hindpaw

        g2d.setColor(furColor);
        g2d.fillRect((int) (19 * scale), (int) (1 * scale), (int) (20 * scale), (int) (5 * scale));

        // hindpaw circuit

        if (isLookingRight) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.drawRect((int) (19 * scale), (int) (1 * scale), (int) (20 * scale), (int) (5 * scale));

        if (isLookingRight) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(Color.BLACK);
        }

        g2d.setStroke(new BasicStroke(2)); //у задней лапки стерла частично полную обводку лапки
        g2d.drawLine((int) (27 * scale), (int) (1 * scale), (int) (40 * scale), (int) (1 * scale));


        g2d.setTransform(catTransform); //возвращаем холст в исходное состояние, иначе все др.объекты на холсте тоже первернутся, холст останется в перевернутом состоянии
    }

    public void updateEyes() {
        ++blinkTicksFromStart;

        if (blinkTicksFromStart >= 25) {
            isEyesClosed = false;
            blinkTicksFromStart = 0;
        } else if (blinkTicksFromStart >= 20) {
            isEyesClosed = true;
        }

    }


}
