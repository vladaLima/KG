package model;

public class Main {
    public static void main(String[] args) {
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 0.0};

        CubicSpline spline = new CubicSpline(x, y);

        for (int i = 0; i < x.length; i++) {
            System.out.println(spline.point(x[i]));
        }
    }
}