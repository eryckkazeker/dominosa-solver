public class Domino {
  private int value1;
  private int value2;

  public Domino(int value1, int value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public int getValue1() {
    return value1;
  }

  public int getValue2() {
    return value2;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Domino)) {
      return false;
    }

    var domino = (Domino)obj;

    return (domino.getValue1() == this.value1 && domino.getValue2() == this.value2)
      || (domino.getValue1() == this.value2 && domino.getValue2() == this.value1);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31*result+value1+value2;
    return result;
  }

  @Override
  public String toString() {
    return String.format("[%d, %d]", value1, value2);
  }
}
