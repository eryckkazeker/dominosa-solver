public class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Point)) {
      return false;
    }

    var other = (Point)obj;
    return other.getX() == this.x && other.getY() == this.y;

  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31*result+x;
    result = 31*result+y;
    return result;
  }

}
