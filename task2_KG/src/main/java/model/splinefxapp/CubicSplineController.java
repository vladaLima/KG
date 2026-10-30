package model.splinefxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import model.CubicSpline2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CubicSplineController {

    @FXML
    private AnchorPane canvasPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Label coordinatesLabel;

    private final List<Double> pointsX = new ArrayList<>();
    private final List<Double> pointsY = new ArrayList<>();

    // Расстояние между линиями сетки в математических координатах
    private static final double GRID_STEP = 1.0;

    // Отступ для подписей
    private static final double MARGIN = 35.0;

    // Максимальные координаты, в которых разрешено ставить точки
    private static final double MIN_CLICK_COORD = -21.0;
    private static final double MAX_CLICK_COORD = 21.0;

    @FXML
    private void initialize() {
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        canvas.widthProperty().addListener((obs, oldValue, newValue) -> draw());
        canvas.heightProperty().addListener((obs, oldValue, newValue) -> draw());

        // Добавление точки нажатием левой кнопки мыши
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            double x = toMathX(event.getX());
            double y = toMathY(event.getY());

            // Ограничиваем именно добавление точек диапазоном [-21; 21]
            if (x < MIN_CLICK_COORD || x > MAX_CLICK_COORD || y < MIN_CLICK_COORD || y > MAX_CLICK_COORD) {
                coordinatesLabel.setText("Можно добавлять точки только в диапазоне от -21 до 21");
                return;
            }

            // Не допускается добавление уже существующей точки
            for (int i = 0; i < pointsX.size(); i++) {
                if (Math.hypot(x - pointsX.get(i), y - pointsY.get(i)) < 1e-9) { // Расстояние между уже сущ.точками примерно = 0
                    coordinatesLabel.setText(
                            "Такая точка уже добавлена: x = "
                                    + formatCoordinate(pointsX.get(i))
                                    + ", y = "
                                    + formatCoordinate(pointsY.get(i))
                    );
                    return;
                }
            }

            pointsX.add(x);
            pointsY.add(y);

            coordinatesLabel.setText(String.format(Locale.US, "Добавлена точка: x = %.3f, y = %.3f", x, y));

            draw();
        });

        // Отображение координат под курсором
        canvas.setOnMouseMoved(event -> {
            double x = toMathX(event.getX());
            double y = toMathY(event.getY());

            coordinatesLabel.setText(
                    String.format(
                            Locale.US,
                            "x = %.3f, y = %.3f",
                            x, y
                    )
            );
        });

        draw();
    }

    // Масштаб: одинаковое количество пикселей на единицу по обеим осям
    // Определяет сколько экранных пикселей соответствует одной математической единице
    private double getScale() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        if (width <= 2.0 * MARGIN || height <= 2.0 * MARGIN) {
            return 1.0;
        }

        // Сетка охватывает всё окно, а не только диапазон кликов
        // Масштаб выбирается так, чтобы единицы по X и Y были одинаковыми
        return Math.min((width - 2.0 * MARGIN) / 30.0, (height - 2.0 * MARGIN) / 30.0);
    }

    // Математические координаты -> экранные (ось y вниз направлена в Canvas)
    private double toScreenX(double x) {
        return canvas.getWidth() / 2.0 + x * getScale();
    }

    private double toScreenY(double y) {
        return canvas.getHeight() / 2.0 - y * getScale();
    }

    // Экранные координаты -> математические
    private double toMathX(double screenX) {
        return (screenX - canvas.getWidth() / 2.0) / getScale();
    }

    private double toMathY(double screenY) {
        return (canvas.getHeight() / 2.0 - screenY) / getScale();
    }

    private void draw() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        drawGridAndAxes(gc);

        // Отрисовка сплайна
        if (pointsX.size() >= 2) {
            try {
                drawSpline(gc);
            } catch (IllegalArgumentException | ArithmeticException ex) {
                coordinatesLabel.setText("Не удалось построить сплайн: " + ex.getMessage());
            }
        }

        // Если точка лежит прямо на линии кривой, её синий крестик должен оставаться видимым поверх зелёной линии
        drawPoints(gc);
    }

    private void drawGridAndAxes(GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double scale = getScale();

        // Вычисляем математические координаты видимых краёв окна
        double minX = -width / (2.0 * scale);
        double maxX = width / (2.0 * scale);
        double minY = -height / (2.0 * scale);
        double maxY = height / (2.0 * scale);

        gc.setFont(Font.font(12));
        gc.setStroke(Color.rgb(230, 230, 230));
        gc.setLineWidth(1.0);

        // Вертикальные линии сетки по всей ширине окна
        for (double x = Math.ceil(minX / GRID_STEP) * GRID_STEP; x <= maxX; x += GRID_STEP) {
            double px = toScreenX(x);
            gc.strokeLine(px, 0, px, height); // Рисуем вертикальную линию от верхнего края Canvas до нижнего
        }

        // Горизонтальные линии сетки по всей высоте окна
        for (double y = Math.ceil(minY / GRID_STEP) * GRID_STEP; y <= maxY; y += GRID_STEP) {
            double py = toScreenY(y);
            gc.strokeLine(0, py, width, py);
        }

        // Ищем экранные координаты осей X и Y
        double axisY = toScreenY(0);
        double axisX = toScreenX(0);

        gc.setFill(Color.DIMGRAY);

        // Подписи X вдоль горизонтальной оси
        if (axisY >= 0 && axisY <= height) {
            for (double x = Math.ceil(minX / GRID_STEP) * GRID_STEP; x <= maxX; x += GRID_STEP) {
                if (Math.abs(x) < 1e-9) { // Пропускаем 0, его мы отдельно рисуем в конце метода
                    continue;
                }

                double px = toScreenX(x);

                if (px >= 0 && px <= width - 20) {
                    gc.fillText(
                            formatCoordinate(x),
                            px + 3,
                            Math.min(height - 3, axisY + 16)
                    );
                }
            }
        }

        // Подписи Y вдоль вертикальной оси
        if (axisX >= 0 && axisX <= width) {
            for (double y = Math.ceil(minY / GRID_STEP) * GRID_STEP; y <= maxY; y += GRID_STEP) {
                if (Math.abs(y) < 1e-9) {
                    continue;
                }

                double py = toScreenY(y);

                if (py >= 12 && py <= height - 3) {
                    gc.fillText(
                            formatCoordinate(y),
                            Math.min(width - 25, axisX + 7),
                            py - 3
                    );
                }
            }
        }

        // Оси доходят до краёв окна
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);

        if (axisY >= 0 && axisY <= height) {
            gc.strokeLine(0, axisY, width, axisY);
        }

        if (axisX >= 0 && axisX <= width) {
            gc.strokeLine(axisX, 0, axisX, height);
        }

        gc.setFill(Color.BLACK);

        if (axisY >= 0 && axisY <= height) {
            gc.fillText("X", width - 18, Math.max(12, axisY - 8));
        }

        if (axisX >= 0 && axisX <= width) {
            gc.fillText("Y", Math.min(width - 15, axisX + 7), 15);
        }

        if (axisX >= 0 && axisX <= width
                && axisY >= 0 && axisY <= height) {
            gc.fillText("0", axisX + 5, axisY + 15);
        }
    }

    private String formatCoordinate(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-9) {
            return String.valueOf((long) Math.rint(value));
        }

        return String.format(Locale.US, "%.1f", value);
    }

    private void drawSpline(GraphicsContext gc) {
        double[] x = pointsX.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = pointsY.stream().mapToDouble(Double::doubleValue).toArray();

        CubicSpline2D spline = new CubicSpline2D(x, y);

        // Накопление расстояний для t = [0, L]
        double totalLength = 0.0;

        for (int i = 1; i < x.length; i++) {
            totalLength += Math.hypot(x[i] - x[i - 1], y[i] - y[i - 1]);
        }

        if (totalLength <= 0.0) {
            return;
        }

        int samples = 500;

        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2.0);

        double[] previous = spline.point(0.0);

        for (int i = 1; i <= samples; i++) {
            double t = totalLength * i / samples;
            double[] current = spline.point(t);

            gc.strokeLine(
                    toScreenX(previous[0]),
                    toScreenY(previous[1]),
                    toScreenX(current[0]),
                    toScreenY(current[1])
            );

            // В конце текущая точка становится предыдущей для следующей итерации
            previous = current;
        }
    }

    private void drawPoints(GraphicsContext gc) {
        gc.setStroke(Color.DODGERBLUE);
        gc.setLineWidth(2.0);

        for (int i = 0; i < pointsX.size(); i++) {
            double px = toScreenX(pointsX.get(i));
            double py = toScreenY(pointsY.get(i));
            double size = 5.0;

            // Рисуем одну диагональ и вторую и таким образом обозначаем точку крестиком x
            gc.strokeLine(
                    px - size, py - size,
                    px + size, py + size
            );

            gc.strokeLine(
                    px - size, py + size,
                    px + size, py - size
            );
        }
    }
}
