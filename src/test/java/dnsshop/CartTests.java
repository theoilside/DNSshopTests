package dnsshop;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartTests
{
    static WebDriver driver;

    /**
     * Запуск драйвера с браузером Chrome 112 перед тестами
     */
    @BeforeAll
    public static void setupDriver()
    {
        WebDriverManager.chromedriver().browserVersion("112").setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1025, 1025));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    /**
     * Тест на добавление товара в корзину
     */
    @Test
    public void testAddItem()
    {
        // Открыть страницу товара
        driver.get("https://www.dns-shop.ru/product/af81f10af3a7ed20/bluetooth-garnitura-sony-wh-1000xm4-cernyj/");

        // Добавить в корзину
        try
        {
            driver.findElement(By.xpath("//div[8]/div/button[2]")).click();
        }
        catch (NoSuchElementException e)
        {
            Assertions.fail("Кнопка добавления в корзину не найдена на странице :(");
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-link-counter__badge")));

        // Перейти в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        // Проверки
        WebElement element = driver.findElement(By.cssSelector(".cart-products-count"));
        String textElement = element.getText();
        String message = String.format("В корзине неверное количество товаров. Ожидалось: «1 товар». Получили: «%s»", textElement);
        Assertions.assertEquals("1 товар", textElement, message);

        // Очистка корзины
        try
        {
            driver.findElement(By.cssSelector(".remove-button__title")).click();
        }
        catch (NoSuchElementException e)
        {
            Assertions.fail("Кнопка удаления товара из корзины не найдена на странице :(");
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty-message__title-empty-cart")));
    }

    /**
     * Тест на удаление товара из корзины
     */
    @Test
    public void removeItem()
    {
        // Перейти в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        // Поиск заголовка пустой корзины
        WebElement element = null;
        try
        {
            element = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        }
        catch (NoSuchElementException e)
        {
            Assertions.fail("Заголовок пустой корзины не найден :(");
        }

        // Проверки
        String textElement = element.getText();
        String message = "В корзине еще остались товары";
        Assertions.assertEquals("Корзина пуста", textElement, message);
    }

    /**
     * Выход из драйвера с браузером Chrome 112 после тестов
     */
    @AfterAll
    public static void closeDriver()
    {
        driver.quit();
    }
}
