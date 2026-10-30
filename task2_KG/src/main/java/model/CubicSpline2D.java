package model;

public class CubicSpline2D {
    private double[] x;
    private double[] y;
    private CubicSpline sx;
    private CubicSpline sy;

    private double[] params;

    public CubicSpline2D(double[] x, double[] y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("arrayX and arrayY cannot be null");
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("arrayX and arrayY must be the same length");
        }
        if (x.length < 2) {
            throw new IllegalArgumentException("arrayX and arrayY must contain at least 2 points");
        }

        this.x = x.clone();
        this.y = y.clone();

        this.params = calculateParams(x, y);

        this.sx = new CubicSpline(this.params, x);
        this.sy = new CubicSpline(this.params, y);
    }

    public double[] point(double param) {
        double x = sx.point(param); // x(t)
        double y = sy.point(param); // y(t)

        return new double[] {x, y}; // P(t) = [x(t), y(t)]
    }

    public double getTotalLength() {
        return params[params.length - 1];
    }

    private double[] calculateParams(double[] x, double[] y) {
        double[] t = new double[x.length];

        for (int i = 1; i < t.length; i++) {
            double dx = x[i] - x[i - 1];
            double dy = y[i] - y[i - 1];

            double ds = Math.sqrt(dx * dx + dy * dy);

            if (ds == 0.0) {
                throw new IllegalArgumentException("Points must be different");
            }

            t[i] = t[i - 1] + ds;
        }

        return t;
    }


}
