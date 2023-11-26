import java.io.IOException;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class WebClient {

  private static String CONTENT_BEGIN = "var task = '";
  private static String CONTENT_END = "'; var loadedId";
  
  private WebDriver webDriver;

  public WebClient() {
    var options = new ChromeOptions().addArguments("--headless");
    webDriver = new ChromeDriver(options);
  }

  public String getBoard(int size, String id) throws IOException, InterruptedException, URISyntaxException {

    webDriver.get("https://www.puzzle-dominosa.com/specific.php");

    var sizeSelector = webDriver.findElement(By.id("size"));
    Select select = new Select(sizeSelector);
    select.selectByValue(String.valueOf(size));

    var idInput = webDriver.findElement(By.id("specid"));
    idInput.sendKeys(String.valueOf(id));

    var chooseButton = webDriver.findElement(By.xpath("//input[@type='submit']"));

    chooseButton.click();

    var page = webDriver.getPageSource();

    var document = Jsoup.parse(page);

    var scriptTag = document.getElementsByTag("script").get(8);

    var scriptContent = scriptTag.childNode(0).toString();

    var boardData = scriptContent.substring(scriptContent.indexOf(CONTENT_BEGIN)+CONTENT_BEGIN.length(), scriptContent.indexOf(CONTENT_END));

    for (int i = boardData.length()-1; i > 0; i--) {
      if (boardData.charAt(i) == ']') {
        i-=2;
        boardData = boardData.substring(0, i) + "," + boardData.substring(i);
        i--;
        continue;
      }
      boardData = boardData.substring(0, i) + "," + boardData.substring(i);
    }

    boardData = boardData.replace("[", "").replace("]", "");

    return boardData;
  }

  
}
