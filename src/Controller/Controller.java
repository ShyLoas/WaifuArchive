package Controller;

import java.awt.*;
import java.util.*;

public class Controller {

    private int row, col;
    private int[][] matrix;
    MainFrame frame;

    //khởi tạo constructor của hàm controller
    public Controller(MainFrame frame, int row, int col) {
        this.frame = frame;
        this.row = row;
        this.col = col;
        createMatrix();
    }

    //tạo ma trận để xử lí hai icon
    public void createMatrix() {
        matrix = new int[row][col];
        //gán giá trị 0 cho các hàng và cột ngoài rìa
        for (int i = 0; i < col; i++) {
            matrix[0][i] = matrix[row - 1][i] = 0;
        }
        for (int i = 0; i < row; i++) {
            matrix[i][0] = matrix[i][col - 1] = 0;
        }
        //tạo danh sách các button theo icon khả dụng
        Random random = new Random();
        int imgCount = 14, max = imgCount / 2, arr[] = new int[imgCount + 1];
        ArrayList<Point> listPoint = new ArrayList<Point>();
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                listPoint.add(new Point(i, j));
            }
        }
        int i = 0;
        //phân phối ngẫu nhiên các icon
        do {
            int index = random.nextInt(imgCount) + 1;
            if (arr[index] < max) {
                arr[index] += 2;
                for (int j = 0; j < 2; j++) {
                    try {
                        int size = listPoint.size();
                        int pointIndex = random.nextInt(size);
                        matrix[listPoint.get(pointIndex).x][listPoint.get(pointIndex).y] = index;
                        listPoint.remove(pointIndex);
                    } catch (Exception e) {
                    }
                }
                i++;
            }
        } while (i < row * col / 2); //lặp lại cho đến khi đã phân phối các icon cho đến khi chia hết cho 2
    }

    //kiểm tra đường thẳng theo hàng
    private boolean checkX(int y1, int y2, int x) {
        int min = Math.min(y1, y2);
        int max = Math.max(y1, y2);

        for (int y = min + 1; y < max; y++) {
            if (matrix[x][y] != 0) {
                return false;
            }
        }
        return true;
    }

    //kiểm tra đường thẳng theo cột
    private boolean checkY(int x1, int x2, int y) {
        int min = Math.min(x1, x2);
        int max = Math.max(x1, x2);

        for (int x = min + 1; x < max; x++) {
            if (matrix[x][y] != 0) {
                return false;
            }
        }
        return true;
    }

    //kiểm tra đường nối 3 điểm theo hình chữ nhật theo chiều ngang sau đó đến dọc
    private boolean checkRectX(Point p1, Point p2) {
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        for (int y = pMinY.y; y <= pMaxY.y; y++) {
            if (y > pMinY.y && matrix[pMinY.x][y] != 0) {
                return false;
            }
            if ((matrix[pMaxY.x][y] == 0)
                    && checkY(pMinY.x, pMaxY.x, y)
                    && checkX(y, pMaxY.y, pMaxY.x)) {
                return true;
            }
        }
        return false;
    }

    //kiểm tra đường nối 3 điểm theo hình chữ nhật theo chiều dọc trước sau đó đến ngang
    private boolean checkRectY(Point p1, Point p2) {
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        for (int x = pMinX.x; x <= pMaxX.x; x++) {
            if (x > pMinX.x && matrix[x][pMinX.y] != 0) {
                return false;
            }
            if ((matrix[x][pMaxX.y] == 0)
                    && checkX(pMinX.y, pMaxX.y, x)
                    && checkY(x, pMaxX.x, pMaxX.y)) {
                return true;
            }
        }
        return false;
    }

    //kiểm tra 3 góc phức tạp theo chiều ngang
    private boolean checkMoreLineX(Point p1, Point p2, int type) {
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        int y = pMaxY.y + type;
        int row = pMinY.x;
        int colFinish = pMaxY.y;
        if (type == -1) {
            colFinish = pMinY.y;
            y = pMinY.y + type;
            row = pMaxY.x;
        }
        if ((matrix[row][colFinish] == 0 || pMinY.y == pMaxY.y)
                && checkX(pMinY.y, pMaxY.y, row)) {
            while (matrix[pMinY.x][y] == 0
                    && matrix[pMaxY.x][y] == 0) {
                if (checkY(pMinY.x, pMaxY.x, y)) {
                    return true;
                }
                y += type;
            }
        }
        return false;
    }

    //kiểm tra 3 góc phức tạp theo chiều dọc
    private boolean checkMoreLineY(Point p1, Point p2, int type) {
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        int x = pMaxX.x + type;
        int col = pMinX.y;
        int rowFinish = pMaxX.x;
        if (type == -1) {
            rowFinish = pMinX.x;
            x = pMinX.x + type;
            col = pMaxX.y;
        }
        if ((matrix[rowFinish][col] == 0 || pMinX.x == pMaxX.x)
                && checkY(pMinX.x, pMaxX.x, col)) {
            while (matrix[x][pMinX.y] == 0
                    && matrix[x][pMaxX.y] == 0) {
                if (checkX(pMinX.y, pMaxX.y, x)) {
                    return true;
                }
                x += type;
            }
        }
        return false;
    }

    //kiểm tra xem hai điểm có trùng nhau hay không
    public PointLine checkTwoPoint(Point p1, Point p2) {
        if (!p1.equals(p2) && matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
            if (p1.x == p2.x) {
                if (checkX(p1.y, p2.y, p1.x)) {
                    return new PointLine(p1, p2);
                }
            }
            if (p1.y == p2.y) {
                if (checkY(p1.x, p2.x, p1.y)) {
                    return new PointLine(p1, p2);
                }
            }

            if (checkRectX(p1, p2)) {
                return new PointLine(p1, p2);
            }
            if (checkRectY(p1, p2)) {
                return new PointLine(p1, p2);
            }
            if (checkMoreLineX(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            if (checkMoreLineX(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }

            if (checkMoreLineY(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            if (checkMoreLineY(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }
        }
        return null;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

}
