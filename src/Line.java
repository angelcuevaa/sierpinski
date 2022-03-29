public class Line {
    private Coordinate a;
    private Coordinate b;

    public Line(){
        a = new Coordinate(0,0);
        b = new Coordinate(0,0);
    }

    public Line(Coordinate a, Coordinate b){
        this.a = a;
        this.b = b;
    }
    public double getX0(){
        return a.getX();
    }
    public double getY0(){
        return a.getY();
    }
    public double getX1(){
        return b.getX();
    }
    public double getY1(){
        return b.getY();
    }



    @Override
    public String toString() {
        return "Line{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
