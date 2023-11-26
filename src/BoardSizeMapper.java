public class BoardSizeMapper {
  public static int map(int size) {
    switch (size) {
      case 3:
        return 0;
      case 4:
        return 6;
      case 5:
        return 7;
      case 6:
        return 1;
      case 7:
        return 8;
      case 8:
        return 9;
      case 15:
        return 3;
      case 20:
        return 4;
      default:
        throw new RuntimeException("Invalid board size");
    }
  }
}
