package model;

public class CubicSpline {
    private double[] a;
    private double[] b;
    private double[] c;
    private double[] d;

    private double[] x;
    private double[] y;

    public CubicSpline(double[] x, double[] y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("arrayX and arrayY cannot be null");
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("arrayX and arrayY must be the same length");
        }
        if (x.length < 2) {
            throw new IllegalArgumentException("arrayX and arrayY must contain at least 2 points");
        }

        for (int i = 1; i < x.length; i++) {
            if (x[i] <= x[i - 1]) {
                throw new IllegalArgumentException("x values must be strictly increasing");
            }
        }
        this.x = x.clone();
        this.y = y.clone();

        a = getA();
        c = getC();
        b = getB();
        d = getD();
    }

    public double[] getX() {
        return x.clone();
    }

    public double[] getY() {
        return y.clone();
    }

    private double[] calculateH() {
        double[] h = new double[x.length - 1];

        for (int i = 0; i < x.length - 1; i++) {
            h[i] = x[i+1] - x[i];
        }

        return h;
    }

    private double[] getA() {
        a = y.clone();

        return a;
    }

    // Метод прогонки трехдиагональной матрицы

    private double[] getC() {
        int countPoints = x.length;
        c = new double[countPoints];
        double[][] coeffs = buildCoeffsMatrix();
        double[] cInner = new double[coeffs.length];
        double[] result = buildFreeMembersMatrix();
        double[] v = new double[countPoints - 2];
        double[] u = new double[countPoints - 2];

        // Прямой ход - вычисление v[i] и u[i]

        for (int i = 0; i < coeffs.length; i++) {
            if (coeffs.length == 1) {
                u[i] = result[i] / coeffs[i][i];
            } else if (i == 0) {
                v[i] = -coeffs[i][i+1] / coeffs[i][i];
                u[i] = result[i] / coeffs[i][i];
            } else if (i < coeffs.length - 1) {
                v[i] = -coeffs[i][i+1] / (coeffs[i][i] + coeffs[i][i-1] * v[i-1]);
                u[i] = (result[i] - coeffs[i][i-1] * u[i-1]) / (coeffs[i][i] + coeffs[i][i-1] * v[i-1]);
            } else {
                // Последняя строка: Cn-1 = Vn-1 * Cn (=0) + Un-1 = Un-1
                u[i] = (result[i] - coeffs[i][i-1] * u[i-1]) / (coeffs[i][i] + coeffs[i][i-1] * v[i-1]);
            }
        }

        // Обратный ход - вычисление ci по найденным v[i] и u[i]

        for (int i = coeffs.length - 1; i >= 0; i--) {
            if (i == coeffs.length - 1) {
                cInner[i] = u[i];
            } else {
                cInner[i] = v[i] * cInner[i+1] + u[i];
            }
        }

        c[0] = 0.0;

        for (int i = 0; i < cInner.length; i++) {
            c[i+1] = cInner[i];
        }

        c[countPoints - 1] = 0.0;

        return c;
    }

    private double[] getB() {
        b = new double[x.length - 1];
        double [] h = calculateH();

        for (int i = 0; i < x.length - 1; i++) {
            b[i] = (a[i+1] - a[i]) / h[i] - (h[i] / 3) * (c[i+1] + 2 * c[i]);
        }

        return b;
    }

    private double[] getD() {
        d = new double[x.length - 1];
        double [] h = calculateH();

        for (int i = 0; i < x.length - 1; i++) {
            d[i] = (c[i+1] - c[i]) / (3 * h[i]);
        }

        return d;
    }

    private double[][] buildCoeffsMatrix() {
        double[] h = calculateH();
        int countPoints = x.length;

        double[][] coeffsMatrix = new double[countPoints - 2][countPoints - 2];

        for (int i = 0; i < countPoints - 2; i++) {
            coeffsMatrix[i][i] = 2.0 * (h[i] + h[i+1]);

            if (i < countPoints - 3) {
                coeffsMatrix[i][i+1] = h[i+1];
            }

            if (i > 0) {
                coeffsMatrix[i][i-1] = h[i];
            }
        }

        return coeffsMatrix;
    }

    private double[] buildFreeMembersMatrix() {
        double[] h = calculateH();
        int countPoints = x.length;

        double[] freeMembersMatrix = new double[countPoints - 2];

        for (int i = 0; i < countPoints - 2; i++) {
            freeMembersMatrix[i] = 3.0 * ((a[i+2] - a[i+1]) / h[i+1] - (a[i+1] - a[i]) / h[i]);
        }

        return freeMembersMatrix;
    }

    public double point(double param) throws IllegalArgumentException{
        if (param < x[0] || param > x[x.length - 1]) {
            throw new IllegalArgumentException("Invalid parameter");
        }

        int i = searchSegmentIndex(param);
        double dx = param - x[i];
        double result = a[i] + b[i] * dx + c[i] * Math.pow(dx, 2) + d[i] * Math.pow(dx, 3);

        return result;
    }

    private int searchSegmentIndex(double x) {
        if (x == this.x[this.x.length - 1]) {
            return this.x.length - 2;
        }

        int l = 0;
        int hi = this.x.length;

        while (l < hi) {
            int mid = (l + hi) / 2;

            if (x < this.x[mid]) {
                hi = mid;
            } else {
                l = mid + 1;
            }
        }

        return l - 1;
    }
}
