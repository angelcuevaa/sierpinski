//Angel Cueva

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File fileName = new File("input.txt");
        int target = 0;
        ArrayList<Double> fileNums = new ArrayList<>();
        try {
            Scanner file = new Scanner(fileName);
            target = file.nextInt();
            while (file.hasNext()) {
                fileNums.add(file.nextDouble());
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found..");
        }
        ArrayList<Line> lineSegments = new ArrayList<>();
        for (int i = 0; i < fileNums.size(); i += 4) {
            Line tempLine = new Line(new Coordinate(fileNums.get(i), fileNums.get(i + 1)),
                    new Coordinate(fileNums.get(i + 2), fileNums.get(i + 3)));
            lineSegments.add(tempLine);
        }
        //System.out.println(lineSegments.size());

        ArrayList<Line> sierpinskiLines = sierpinskiDrawer(target);
        //ArrayList<Boolean> intersectionLinesBools = new ArrayList<>();


        try {
            FileWriter output = new FileWriter("output.txt");
            for (int i = 0; i < lineSegments.size(); i++) {
                int temp = 0;
                Line lineSegmentTemp = lineSegments.get(i);
                for (int j = 0; j < sierpinskiLines.size(); j++) {
                    if (intersection(lineSegmentTemp, sierpinskiLines.get(j))) {
                        temp = 1;
                        break;
                    }
                }
                output.write(temp + "\n");
            }
            output.close();
        }
        catch (IOException e){
            System.out.println("File already exists");
        }
        //System.out.println(intersectionLinesBools);
        //System.out.println(intersectionLinesBools.size());

    }

    //pass coordinates of 2 lines (4 points, 8 xy), use cross product
    public static boolean intersection(Line line1, Line line2) {
        boolean intersection = false;

        double L1x1 = line1.getX0();
        double L1y1 = line1.getY0();
        double L1x2 = line1.getX1();
        double L1y2 = line1.getY1();


        double L2x1 = line2.getX0();
        double L2y1 = line2.getY0();
        double L2x2 = line2.getX1();
        double L2y2 = line2.getY1();

        double temp = (L1x2 - L2x1) * (L1y1 - L2y1) - (L1x1 - L2x1) * (L1y2 - L2y1);
        double temp2 = (L1x2 - L2x2) * (L1y1 - L2y2) - (L1x1 - L2x2) * (L1y2 - L2y2);

        double temp3 = (L2x1 - L1x1) * (L2y2 - L1y1) - (L2x2 - L1x1) * (L2y1 - L1y1);
        double temp4 = (L2x1 - L1x2) * (L2y2 - L1y2) - (L2x2 - L1x2) * (L2y1 - L1y2);


        //System.out.println(temp + " " + temp2 + " " + temp3 + " " + temp4);


        if (((temp < 0 && temp2 > 0) || (temp > 0 && temp2 < 0)) &&
                ((temp3 < 0 && temp4 > 0) || (temp3 > 0 && temp4 < 0))) {
            intersection = true;
        }


        return intersection;
    }

    public static ArrayList<Line> sierpinskiDrawer(int target) {
        //youre assuming shape 0 is reflected across x axis
        Coordinate origin = new Coordinate(0, 0);
        Coordinate a1 = new Coordinate(0.5, Math.sqrt(1 - 0.25));
        Coordinate a2 = new Coordinate(1.5, Math.sqrt(1 - 0.25));
        Coordinate a3 = new Coordinate(2.0, 0);

        ArrayList<Coordinate> shape0 = new ArrayList<>();
        ArrayList<Coordinate> shape1 = new ArrayList<>();
        ArrayList<Coordinate> shape2 = new ArrayList<>();
        ArrayList<Coordinate> shape3 = new ArrayList<>();


        shape0.add(origin);
        shape0.add(a1);
        shape0.add(a2);
        shape0.add(a3);
        //nested loop for target number and inner loop for drawing shape
        //get WHOLE shape from previous coors of shape 2
        //plot shape 2 from end of shape 1, add y's and x's
        //start plotting shape 3 at end of shape 2
        for (int i = 1; i < target; i++) {
            shape1.clear();
            shape2.clear();
            shape3.clear();
            xAxisReflect(shape0);
            for (int j = 0; j < 3; j++) {
                ArrayList<Coordinate> temp = new ArrayList<>();
                if (j == 0) {
                    for (int k = 0; k < shape0.size(); k++) {
                        shape1.add(coorsMethod(shape0.get(k)));
                    }
                }
                if (j == 1) {
                    double xDiff = shape1.get(shape1.size() - 1).getX();
                    double yDiff = shape1.get(shape1.size() - 1).getY();
                    //System.out.println(xDiff);
                    //System.out.println(yDiff);
                    for (int k = 1; k < shape0.size(); k++) {
                        double newX = shape0.get(k).getX() + xDiff;
                        double newY = shape0.get(k).getY() * -1 + yDiff;
                        Coordinate tempCoor = new Coordinate(newX, newY);
                        temp.add(k - 1, tempCoor);
                    }
                    shape2 = temp;
                }
                if (j == 2) {
                    double xDiff = shape2.get(shape2.size() - 1).getX();
                    double yDiff = shape2.get(shape2.size() - 1).getY();
                    for (int k = 1; k < shape0.size(); k++) {
                        Coordinate coor = coorsMethodPrime(shape0.get(k));
                        Coordinate newCoor = new Coordinate(coor.getX() + xDiff, coor.getY() + yDiff);
                        shape3.add(newCoor);
                    }
                }
            }
            shape0.clear();
            shape0.addAll(shape1);
            shape0.addAll(shape2);
            shape0.addAll(shape3);
        }
        ArrayList<Line> lineList = new ArrayList<>();
        //simplify loop
        for (int first = 0; first < shape0.size() - 1; first++) {
            Line newLine = new Line(shape0.get(first), shape0.get(first + 1));
            lineList.add(newLine);
        }
        return lineList;
    }

    public static double xprime(double x0, double y0) {
        return x0 * Math.cos(Math.toRadians(60)) - y0 * Math.sin(Math.toRadians(60));
    }

    public static double yprime(double x0, double y0) {
        return x0 * Math.sin(Math.toRadians(60)) + y0 * Math.cos(Math.toRadians(60));
    }

    public static double xDoublePrime(double x0, double y0) {
        return x0 * Math.cos(Math.toRadians(300)) - y0 * Math.sin(Math.toRadians(300));
    }

    public static double yDoublePrime(double x0, double y0) {
        return x0 * Math.sin(Math.toRadians(300)) + y0 * Math.cos(Math.toRadians(300));
    }

    //gives you updated coordinate
    public static Coordinate coorsMethod(Coordinate c) {
        return new Coordinate(xprime(c.getX(), c.getY()), yprime(c.getX(), c.getY()));
    }

    public static Coordinate coorsMethodPrime(Coordinate c) {
        return new Coordinate(xDoublePrime(c.getX(), c.getY()), yDoublePrime(c.getX(), c.getY()));
    }

    public static ArrayList<Coordinate> xAxisReflect(ArrayList<Coordinate> coors) {
        for (Coordinate c : coors) {
            c.xAxisFlip();
        }
        return coors;
    }
}

