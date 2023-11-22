
public class Pair {
  
  private Point p1;
  private Point p2;
  
  public Pair(Point p1, Point p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public static Pair ofCoordinates(int x1, int y1, int x2, int y2) {
    Point p1 = new Point(x1,y1);
    Point p2 = new Point(x2, y2);
    return new Pair(p1, p2);
  }

  public Point getP1() {
    return p1;
  }

  public Point getP2() {
    return p2;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Pair)) {
      return false;
    }

    var otherPair = (Pair)obj;
    return otherPair.getP1().equals(this.p1) && otherPair.getP2().equals(this.p2) ||
        otherPair.getP2().equals(this.p1) && otherPair.getP1().equals(this.p2);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31*result+p1.hashCode();
    result = 31*result+p2.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("[%d,%d] [%d,%d]",
        this.p1.getX(),
        this.p1.getY(),
        this.p2.getX(),
        this.p2.getY());
  }

  public Boolean collidesWith(Pair other) {

    if (this.equals(other)) return false;

    return this.p1.equals(other.getP1()) ||
        this.p1.equals(other.getP2()) ||
        this.p2.equals(other.getP1()) ||
        this.p2.equals(other.getP2());
  }

  public Boolean contains(Point p) {
    return this.p1.equals(p) || this.p2.equals(p);
  }

}
