package reserveNewPageByMobileEmulator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebConnector {
//    private final static long DEFAULT_TIMEOUT = 5000;

    // ブラウザ名
    private final String BROWSER_EDGE = "Edge";
    private final String BROWSER_FF = "FireFox";
    private final String BROWSER_OPERA = "Opera";
    private final String BROWSER_CR = "Chrome";
    private final String BROWSER_Default = "Browser";
    // ブラウザタイプ
    private final int BROWSER_TYPE_IE = 0;
    private final int BROWSER_TYPE_EDGE = 1;
    private final int BROWSER_TYPE_FF = 2;
    private final int BROWSER_TYPE_OPERA = 3;
    private final int BROWSER_TYPE_CR = 4;
    private final int BROWSER_TYPE_DF = 5;

    /** WebDriverクラス */
    public static WebDriver webDriver;

    /** 実行中のWebDriverタイプを保持する */
    private int DriverType;
    /** Screen Shot FolderName */
    private String screenShotPath = null;
    private Actions builder = null;
    private ChromeOptions options = new ChromeOptions();

    private boolean acceptNextAlert = true;

    /**
     * コンストラクタ
     */
//    public WebConnector() {}

	public static Date dt;

//	public String testDate;
//	public int weekEnd;
	public String dateFrom;
	public String dateTo;
//	public int priceData;
//	public int termValue;
//	public int termValueWeekEnd;

	public int headCountValue;
	public String breakFastValue;
	public String planAvalue;
	public String planBvalue;
	public String guestValue;

    public String username;
    public String contact;
    public String email;
    public String tel;


    public static WebDriverWait wait;
    private String window1;
    private String window2;

/**
 * Mobile Emulator設定
 */
        public void setMobileEmulator(String mobile, int width, int height, double pixel) {
        	Map<String, Object> mobileEmulation = new HashMap<>();
        	Map<String, Object> deviceMetrics = new HashMap<>();

//       		mobileEmulation.put("deviceName", mobile);
        	mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
        	options.addArguments("--enable-web-app-frame");

        	deviceMetrics.put("width", width);
        	deviceMetrics.put("height", height);
        	deviceMetrics.put("pixelRatio", pixel);
        	mobileEmulation.put("deviceMetrics", deviceMetrics);

        	options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }

    /**
     * WebDriverの選択
     * @param browser ブラウザ名
     * @throws InterruptedException
     * @throws MalformedURLException
     */
    public void selectWebDriver(String browser) throws InterruptedException, MalformedURLException {
        this.DriverType = BROWSER_TYPE_DF;
        if (browser.equals(BROWSER_EDGE)) {
            this.DriverType = BROWSER_TYPE_EDGE;
        } else if (browser.equals(BROWSER_FF)) {
            this.DriverType = BROWSER_TYPE_FF;
        } else if (browser.equals(BROWSER_OPERA)) {
            this.DriverType = BROWSER_TYPE_OPERA;
        } else if (browser.equals(BROWSER_CR)) {
        	this.DriverType = BROWSER_TYPE_CR;
        }
        //WebDriverのセット
        this.setWebDriver();
    }

/**
 * WebDriverインスタンスを生成する
 * @throws InterruptedException
 * @throws MalformedURLException
 */
    private void setWebDriver() throws InterruptedException, MalformedURLException {
        switch (this.DriverType) {
        case BROWSER_TYPE_IE: // IE
            //Windows10では、 64bit 版だと動かないので32bitを使う
            System.setProperty("webdriver.ie.driver", "./exe32bit/IEDriverServer.exe");

            webDriver = new InternetExplorerDriver();
            break;

        case BROWSER_TYPE_EDGE: // Edge
            System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\edgedriver_win64\\msedgedriver.exe");

            webDriver = new EdgeDriver();
            break;

        case BROWSER_TYPE_FF: // FireFox
            System.setProperty("webdriver.gecko.driver", "C:\\WebDrivers\\geckodriver_win64\\geckodriver.exe");
            webDriver = new FirefoxDriver();
            break;

        case BROWSER_TYPE_OPERA: // Opera
            System.setProperty("webdriver.opera.driver", "./exe64bit/operadriver.exe");
            webDriver = new OperaDriver();
            break;

        default: // Chrome
            System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver_win32\\chromedriver.exe");
//            driver = new ChromeDriver();
            webDriver = new ChromeDriver(options);
            break;
        }

        Thread.sleep(3000);
        //暗黙wait
        //this.webDriver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);

//		dt = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		testDate = sdf.format(dt);

        wait = new WebDriverWait(webDriver, 10);

    }

/**
 * 指定されたURLでブラウザを開く
 * @param location 表示するURL
 * @throws InterruptedException
 */
    public void openAndWait(String location) throws InterruptedException {
        //location(URL)のタイプミスとかでもエラーにはならない
        webDriver.navigate().to(location);
        Thread.sleep(5000);
    }

/**
 * 画面下方に移動する
 * 微妙なところにクリックしたいエレメントが配置されることへの対応
 */
    public void moveToElement(String selector) {
        try {
            WebElement element = webDriver.findElement(By.id(selector));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(element);
    		actions.perform();
    		Thread.sleep(500);
        } catch(Exception e) {
        }
        try {
            WebElement element = webDriver.findElement(By.xpath(selector));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(element);
    		actions.perform();
    		Thread.sleep(500);
        } catch(Exception e) {
//            webDriver.quit();
        }

    }

/**
 * Windowを最大化する
 * ただし、ChromeDriver以外
 * @throws InterruptedException
 */
    public void setWindowMax() throws InterruptedException {
        //ただし、Chromeではorg.openqa.selenium.NoSuchSessionExceptionで動かない）
            try {
                webDriver.manage().window().maximize();
            } catch (Exception e) {
//                webDriver.quit();
            }
        Thread.sleep(2000);
    }

/**
 * テスト失敗時のブラウザ再起動
 */
    public void rebootBrowser(String mobileBrowserType, String mobileUrl) throws InterruptedException, MalformedURLException {

    	selectWebDriver(mobileBrowserType);
    	openAndWait(mobileUrl);

    }

    public void rebootBrowserMB(String mobileMode, int mobileWidth, int mobileHeight, double mobilePixel, String mobileBrowserType, String mobileUrl) throws MalformedURLException, InterruptedException {
    	setMobileEmulator(mobileMode, mobileWidth, mobileHeight, mobilePixel);
    	selectWebDriver(mobileBrowserType);
    	openAndWait(mobileUrl);

    }

/**
 * 使用言語対応
 */
    public void setLangEnglish() {
        options.addArguments("--lang=en-GB");
    }

    public void setlangJapanese() {
        options.addArguments("--lang=ja-JP");
    }

/**
 * 指定時間待つ
 * @param sec waitする秒数
 */
    public void sleep(int sec) {
        long msec = sec * 1000;
        try {
            Thread.sleep(msec);
        } catch (Exception e) {
        }
    }

/**
 * iFrame を切り替える
 * @param sec waitする秒数
 */
    public WebDriver switchFrame(String selector) {
        return webDriver.switchTo().frame(selector);
    }

    public WebDriver switchDefaultFrame() {
    	return webDriver.switchTo().defaultContent();
    }

    /**
     * フレームが表示されるまで待つ
     * DEFAULT_TIMEOUTで表示されなかったら Exceptionをスローする
     * @param iFrameのIDセレクタ
     * @param フレームが表示された後で、フレーム内で探すセレクタ(ID->name->class順）
     * 　　　　　targetClass iFrame内の class名
     * FireFoxだとダメみたいです。

    public void sleepFrame(String selector, String targetClass) {
        String parentHandle = this.webDriver.getWindowHandle();
        logger.log_info(this, "WindowHandle["+ parentHandle+"]");
        //iFrame エレメントを取得
        WebElement iframe = this.webDriver.findElement(By.id(selector));
        logger.log_info(this, "iframe["+ iframe.getLocation()+"]");
        //iFrame Switch
        this.webDriver.switchTo().frame(iframe);
        WebElement element =this.webDriver.findElement(By.className(targetClass));
        //親画面に戻る
        this.webDriver.switchTo().window(parentHandle);
    }
    */

    public void parentWindow() {
        String parentHandle = webDriver.getWindowHandle();
        //親画面に戻る
        webDriver.switchTo().window(parentHandle);
    }

    public void setWindow() {
        Set<String> set = webDriver.getWindowHandles();
        java.util.Iterator<String> it = set.iterator();
        window1 = it.next();
        window2 = it.next();
        webDriver.switchTo().window(window1);
    }

    public void setChild() {
    	webDriver.switchTo().window(window2);
    }

    public void setParent() {
    	webDriver.switchTo().window(window1);
    }

    public void closeChild() {
    	webDriver.close();
    }

    public void refresh() throws InterruptedException {
    	webDriver.navigate().refresh();
    	Thread.sleep(5000);
    }
    public void eraseCalendar() {

    }
    /**
     * スクリーンショット画像格納フォルダを指定する
     * @param path フォルダ名
     */
    public void setScreenShotPath(String path) {
        this.screenShotPath = path;
    }

    public void initSelenium() throws Exception {
    }

    /**
     * 終了処理
     */
    public void destroySelenium() {
        //WebDriver プロセスを終了し、ブラウザを閉じる
        webDriver.quit();
	}

    /**
     * ブラウザのスクリーンショットを実行する
     * @param filename 保存するファイル名（パスは付けない）
     */
//    public void getScreenShot(String filename) {
//        String path = this.screenShotPath + "/" + filename;
        /**
         * Chromeではスクショ　できません、2.9では
         * 色々やってみたけど、Dhromeでは Exceptionで動作しないため、無効化
         */
//        if(this.DriverType != BROWSER_TYPE_CR) {
//            TakesScreenshot screen = (TakesScreenshot)this.driver;
//            Path capture = Paths.get(path);
//            try {
//                Files.write(capture, screen.getScreenshotAs(OutputType.BYTES));
//            } catch(Exception e) {
//                this.webDriver.quit();
//            }
//        }
//    }

/**
 * 画面から値取得系
 * @throws InterruptedException
 */
        public String getText(String commandLocater3) throws InterruptedException {
    		String getText;
    		WebElement text = webDriver.findElement(By.id(commandLocater3));
            wait.until(ExpectedConditions.visibilityOf(text));
            getText = text.getAttribute("value");
            Thread.sleep(500);

    		return getText;
    	}

        public String getString(String selector) throws InterruptedException {
        	String getText = null;
        	WebElement text = webDriver.findElement(By.id(selector));
        	wait.until(ExpectedConditions.visibilityOf(text));
        	getText = text.getText();
        	Thread.sleep(500);

			return getText;

        }

/**
 * テキストボックス入力系
 */

        /**
         * セレクタに値を送信する
         * @param selector id or name属性セレクタ
         * @param value 送信する値
         */
        public void inputAndWait(String selector, String value) {
            try {
                WebElement element = webDriver.findElement(By.id(selector));
        		Actions actions = new Actions(webDriver);
        		actions.moveToElement(element);
        		actions.perform();
        		Thread.sleep(500);

                wait.until(ExpectedConditions.elementToBeClickable(element));

                element.clear();
                Thread.sleep(500);
                element.sendKeys(value);
                return;
            } catch(Exception e) {
            }
            try {
                WebElement element = webDriver.findElement(By.name(selector));
        		Actions actions = new Actions(webDriver);
        		actions.moveToElement(element);
        		actions.perform();
        		Thread.sleep(500);

                wait.until(ExpectedConditions.elementToBeClickable(element));

                element.clear();
                Thread.sleep(500);
                element.sendKeys(value);
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * セレクタにEnterを送信する
         * @param selector id or name属性セレクタ
         * @param value 送信する値
         */
        public void inputEnterAndWait(String selector) {
            try {
                WebElement element = webDriver.findElement(By.id(selector));
                element.sendKeys(Keys.RETURN);
                return;
            } catch(Exception e) {
            }
            try {
                WebElement element = webDriver.findElement(By.name(selector));
                element.sendKeys(Keys.RETURN);
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * ボタンセレクタをクリックする
         * @param selector [input type]セレクタ
         */
        public void btnClickAndWait(String value) {
            try {
                WebElement element = webDriver.findElement(
                    By.xpath("//input[@type=\"submit\" and @value=\"" + value + "\"]"));
                element.click();
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * 複数のボタンの内、value属性がlabelのボタンをクリックする
         * @param type [input type] "button"  or "submit"
         * @param label value属性値
         */
        public void btnClickAndWait(String type, String label) {
            try {
                List <WebElement>element = webDriver.findElements(By.xpath("//input[@type=\"" + type + "\"]"));
                for(WebElement entity : element) {
                    if(entity.getAttribute("value").equals(label)) {
                        entity.click();
                        break;
                    }
                }
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * 規則的に並ぶ複数のボタンの内、value属性がvalueのindexで指定された順番のボタンをクリックする
         * 複数のボタン配列の特定のvalueのボタンをクリックする
         * @param selector [input type]セレクタ
         * @param value value属性値
         * @param index 配列の順番(0オリジン)
         */
        public void btnByblockClickAndWait(String selector, String value, int index) {
            int i = 0;
            try {
                List <WebElement>element = webDriver.findElements(
                        By.xpath("//input[@type=\"" + selector + "\" and @value=\"" + value + "\"]"));
                for(WebElement entity : element) {
                    if(i == index) {
                        entity.click();
                        break;
                    }
                    i++;
                }
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * 複数のボタンの内 selector名の name属性のボタンをクリックする
         * @param selector
         */
        public void btnBynameClickAndWait(String selector) {
            try {
                WebElement element = webDriver.findElement(By.name(selector));
                element.click();
            } catch(Exception e) {
                webDriver.quit();
            }
        }

        /*
         * Locater名が、id属性のボタンをクリックする
         */
        public void btnClickAndWait_ID(String commandLocater) throws InterruptedException {
    		WebElement elementPos = webDriver.findElement(By.id(commandLocater));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(elementPos);
    		actions.perform();
    		Thread.sleep(500);
    		WebElement submitButton = webDriver.findElement(By.id(commandLocater));
    		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
    		submitButton.click();

    		Thread.sleep(1000);
        }

        /*
         * Locater名が、xpath属性のエレメントをクリックする
         */
        public void btnClickAndWait_X(String commandLocater) throws InterruptedException {
    		WebElement elementPos = webDriver.findElement(By.xpath(commandLocater));
    		wait.until(ExpectedConditions.elementToBeClickable(elementPos));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(elementPos);
    		actions.perform();
    		Thread.sleep(500);
//    		WebElement element = webDriver.findElement(By.xpath(commandLocater));
    		elementPos.click();

    		Thread.sleep(500);
        }

        /*
         * href リンクをクリックする
         * @param text 検索したいリンクテキスト
         */
        public void clickAndWait(String text) {
            try {
                //エレメントから、a href のリンク文字列を探す
                WebElement element = webDriver.findElement(By.linkText(text));
                element.click();
            } catch(Exception e) {
//                webDriver.quit();
            }
        }

        /**
         * href リンクをクリックする
         * @param href リンクテキスト
         */
        public void clickHrefAndWait(String href) {
            try {
                //エレメントから、a href のリンクを探す
                WebElement element = webDriver.findElement(By.xpath("//a[@href='"+href+"']"));
                wait.until(ExpectedConditions.elementToBeClickable(element));
        		Actions actions = new Actions(webDriver);
        		actions.moveToElement(element);
        		actions.perform();
        		Thread.sleep(1000);

                element.click();    //見つかったらクリック
            } catch(Exception e) {
            }
        }

        /**
         * spanタグをクリックする
         * @param tagname "span"を期待
         * @param text タグから検索したい文字列
         */
        public void spanClickAndWait(String tagname, String text) {
            int i = 0;
            try {
                //tagnameの名前のエレメントを探す
                List <WebElement>element = webDriver.findElements(By.tagName(tagname));
                for(WebElement entity : element) {
                    //エレメントのテキストが検索したい文字列と合致
                    if(entity.getText().equals(text)) {
                        entity.click(); //エレメントをクリックする
                        break;
                    }
                }
            } catch(Exception e) {
                webDriver.quit();
            }
        }

        /**
         * aタグをクリックする
         * @param text a タグのテキスト
         * @throws InterruptedException
         */
        public void linkClickAndWait(String text) throws InterruptedException {
    		WebElement elementPos = webDriver.findElement(By.partialLinkText(text));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(elementPos);
    		actions.perform();
    		Thread.sleep(500);

    		WebElement element = webDriver.findElement(By.partialLinkText(text));
    		wait.until(ExpectedConditions.elementToBeClickable(element));
    		element.click();
            Thread.sleep(500);
        }

    /**  チェックボックスをOnまたはOffにする
     * @throws InterruptedException */
        public void checkBoxClick(String commandLocater, String state) throws InterruptedException {
    		WebElement elementPos = webDriver.findElement(By.id(commandLocater));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(elementPos);
    		actions.perform();
    		Thread.sleep(500);

    		WebElement element = webDriver.findElement(By.id(commandLocater));
    		wait.until(ExpectedConditions.elementToBeClickable(element));
    		if(state.equals("on")) {
    			if(element.isSelected() == false) {
    				element.click();
    			}
    		}
    		if(state.equals("off")) {
    			if(element.isSelected() == true) {
    				element.click();
    			}
    		}

            Thread.sleep(500);
        }

    /**  ドロップダウンメニューから選択する
     * @throws InterruptedException */
        public void dropDownSelect(String commandLocater, String selText) throws InterruptedException {
    		WebElement element = webDriver.findElement(By.id(commandLocater));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(element);
    		actions.perform();
    		Thread.sleep(1000);
            Select output_Select = new Select(webDriver.findElement(By.id(commandLocater)));
            output_Select.selectByVisibleText(selText);
            Thread.sleep(500);

        }

    	public void btnClickAndWait_CSS(String commandLocater) throws InterruptedException {
    		WebElement elementPos = webDriver.findElement(By.cssSelector(commandLocater));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(elementPos);
    		actions.perform();
    		Thread.sleep(500);

    		WebElement element = webDriver.findElement(By.cssSelector(commandLocater));
    		wait.until(ExpectedConditions.elementToBeClickable(element));
    		element.click();
            Thread.sleep(1000);
    	}

    	public void cssButtonClickAndPopUp(String commandLocater) throws InterruptedException {
    		acceptNextAlert = true;

    		WebElement exitButton = webDriver.findElement(By.cssSelector(commandLocater));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(exitButton);
    		actions.perform();
    		Thread.sleep(500);
    		exitButton = webDriver.findElement(By.cssSelector(commandLocater));
    		wait.until(ExpectedConditions.elementToBeClickable(exitButton));
    		exitButton.click();
    		Thread.sleep(500);
    	}

/**
 * 入力系
 */
    	public void birthdayInput(String commandLocater, String birthday) throws InterruptedException {
    		String[] inputText = {"//"};
    		if(birthday.length() != 0) {
    			inputText = birthday.split("/", -1);
    			WebElement inputBox = webDriver.findElement(By.id(commandLocater));
    			wait.until(ExpectedConditions.elementToBeClickable(inputBox));
    			inputBox.clear();
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[0]);
    			Thread.sleep(500);
    			inputBox.sendKeys(Keys.RIGHT);
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[1]);
    			Thread.sleep(500);
    			inputBox.sendKeys(Keys.RIGHT);
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[2]);
    			Thread.sleep(500);
    		}
    	}

    	public void birthdayInputEN(String commandLocater, String birthday) throws InterruptedException {
    		String[] inputText = {"//"};
    		if(birthday.length() != 0) {
    			inputText = birthday.split("/", -1);
    			WebElement inputBox = webDriver.findElement(By.id(commandLocater));
    			wait.until(ExpectedConditions.elementToBeClickable(inputBox));
    			inputBox.clear();
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[0]);
    			Thread.sleep(500);
    			inputBox.sendKeys(Keys.ENTER);
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[1]);
    			Thread.sleep(500);
    			inputBox.sendKeys(Keys.ENTER);
    			Thread.sleep(500);
    			inputBox.sendKeys(inputText[2]);
    			Thread.sleep(500);
    		}
    	}

    	public void sunday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
//    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void sundayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
//    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void dateFromSet() {
//    		dt = new Date();
    		String reserveFrom;
    		int reserveYear;
    		int reserveMonth;
    		int reserveDay;

    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		reserveFrom = sdf.format(dt);
    		reserveYear = Integer.valueOf(reserveFrom.substring(0, 4));
    		reserveMonth = Integer.valueOf(reserveFrom.substring(5, 7));
    		reserveDay = Integer.valueOf(reserveFrom.substring(8, 10));
    		dateFrom = String.valueOf(reserveYear) + "年" + String.valueOf(reserveMonth) + "月" + String.valueOf(reserveDay) + "日";
    	}

    	public void dateFromSetEN() {
    		String reserveFrom;
    		int reserveYear;
    		int reserveMonth;
    		String reserveMonthChr = null;
    		int reserveDay;

    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		reserveFrom = sdf.format(dt);
    		reserveYear = Integer.valueOf(reserveFrom.substring(0, 4));
    		reserveMonth = Integer.valueOf(reserveFrom.substring(5, 7));
    		reserveDay = Integer.valueOf(reserveFrom.substring(8, 10));

    		switch(reserveMonth) {
    		case(1):
    			reserveMonthChr = "January";
    			break;
    		case(2):
    			reserveMonthChr = "Feburary";
    			break;
    		case(3):
    			reserveMonthChr = "March";
    			break;
    		case(4):
    			reserveMonthChr = "April";
    			break;
    		case(5):
    			reserveMonthChr = "May";
    			break;
    		case(6):
    			reserveMonthChr = "June";
    			break;
    		case(7):
    			reserveMonthChr = "July";
    			break;
    		case(8):
    			reserveMonthChr = "August";
    			break;
    		case(9):
    			reserveMonthChr = "September";
    			break;
    		case(10):
    			reserveMonthChr = "October";
    			break;
    		case(11):
    			reserveMonthChr = "Novenber";
    			break;
    		case(12):
    			reserveMonthChr = "December";
    			break;

    		}
    		dateFrom = reserveMonthChr + " "  + String.valueOf(reserveDay) + ", " + String.valueOf(reserveYear);
    	}

    	public void monday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.MONDAY:
//    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void mondayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.MONDAY:
//    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void tuesday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.TUESDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void tuesdayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.TUESDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void wednesday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.WEDNESDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void wednesdayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.WEDNESDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void thursday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.THURSDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void thursdayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.THURSDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void friday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.FRIDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void fridayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.FRIDAY:
//    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.SATURDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void saturday(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.SATURDAY:
//    			calendar.add(Calendar.DATE, 1);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void saturdayEN(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, 1);
    		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
    		case Calendar.SUNDAY:
    			calendar.add(Calendar.DATE, 6);
    			break;
    		case Calendar.MONDAY:
    			calendar.add(Calendar.DATE, 5);
    			break;
    		case Calendar.TUESDAY:
    			calendar.add(Calendar.DATE, 4);
    			break;
    		case Calendar.WEDNESDAY:
    			calendar.add(Calendar.DATE, 3);
    			break;
    		case Calendar.THURSDAY:
    			calendar.add(Calendar.DATE, 2);
    			break;
    		case Calendar.FRIDAY:
    			calendar.add(Calendar.DATE, 1);
    			break;
    		case Calendar.SATURDAY:
//    			calendar.add(Calendar.DATE, 1);
    			break;
    		default:
    		}

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(500);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);
    	}

    	public void termSet(int term) {
//    		Date dt = new Date();
    		Date reserveEnd;
    		String reserveTo;
    		int reserveToYear;
    		int reserveToMonth;
    		int reserveToDay;

    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, term);
    		reserveEnd = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		reserveTo = sdf.format(reserveEnd);
    		reserveToYear = Integer.valueOf(reserveTo.substring(0, 4));
    		reserveToMonth = Integer.valueOf(reserveTo.substring(5, 7));
    		reserveToDay = Integer.valueOf(reserveTo.substring(8, 10));
    		dateTo = String.valueOf(reserveToYear) + "年" + String.valueOf(reserveToMonth) + "月" + String.valueOf(reserveToDay) + "日";
    	}

    	public void termSetEN(int term) {
    		Date reserveEnd;
    		String reserveTo;
    		int reserveToYear;
    		int reserveToMonth;
    		String reserveToMonthChr = null;
    		int reserveToDay;

    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.DATE, term);
    		reserveEnd = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		reserveTo = sdf.format(reserveEnd);
    		reserveToYear = Integer.valueOf(reserveTo.substring(0, 4));
    		reserveToMonth = Integer.valueOf(reserveTo.substring(5, 7));
    		reserveToDay = Integer.valueOf(reserveTo.substring(8, 10));

    		switch(reserveToMonth) {
    		case(1):
    			reserveToMonthChr = "January";
    			break;
    		case(2):
    			reserveToMonthChr = "Feburary";
    			break;
    		case(3):
    			reserveToMonthChr = "March";
    			break;
    		case(4):
    			reserveToMonthChr = "April";
    			break;
    		case(5):
    			reserveToMonthChr = "May";
    			break;
    		case(6):
    			reserveToMonthChr = "June";
    			break;
    		case(7):
    			reserveToMonthChr = "July";
    			break;
    		case(8):
    			reserveToMonthChr = "August";
    			break;
    		case(9):
    			reserveToMonthChr = "September";
    			break;
    		case(10):
    			reserveToMonthChr = "October";
    			break;
    		case(11):
    			reserveToMonthChr = "Novenber";
    			break;
    		case(12):
    			reserveToMonthChr = "December";
    			break;

    		}

    		dateTo = reserveToMonthChr + " "  + String.valueOf(reserveToDay) + ", " + String.valueOf(reserveToYear);
    	}

    	public void fourMonthAgo(String commandLocater) throws InterruptedException {
    		dt = new Date();
    		Date reserveDate;
    		String testReserveDate;
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(dt);
    		calendar.add(Calendar.MONTH, 4);

    		reserveDate = calendar.getTime();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		testReserveDate = sdf.format(reserveDate);
    		dt = reserveDate;

    		String inputText = testReserveDate.substring(0, 10);

            WebElement inputDate = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.elementToBeClickable(inputDate));
            inputDate.sendKeys(Keys.ENTER);
            inputDate.clear();
            Thread.sleep(1000);
//            reserveYear = reserveYear + "\n";
            inputDate.sendKeys(inputText);
            Thread.sleep(500);

    	}

/**
 * 検証系
 */
        /**
         * 宿泊料金の検証
         */
        public boolean testPrice(String commandLocater1, int price) {
        	boolean testResult = false;

    		String priceText;
    		int priceData;

//    		int up25Price = 8750;
//    		int normalPrice = 7000;
    		int calcPrice;

            WebElement element = webDriver.findElement(By.id(commandLocater1));
            wait.until(ExpectedConditions.visibilityOf(element));
            priceText = element.getText();
            priceText = priceText.replace("円", "");
            priceText = priceText.replace("（税込み）", "");
            priceText = priceText.replace("合計", "");
            priceText = priceText.replace(",", "").trim();

            priceData = Integer.valueOf(priceText);

            calcPrice = price;
            if(priceData == calcPrice) {
    			testResult = true;
    		}else {
    			testResult = false;
    		}
    		return testResult;
    	}

        public boolean testPriceEN(String commandLocater1, double price) {
        	boolean testResult = false;

    		String priceText;
    		double priceData;
//    		int up25Price = 8750;
//    		int normalPrice = 7000;
    		double calcPrice;

            WebElement element = webDriver.findElement(By.id(commandLocater1));
            wait.until(ExpectedConditions.visibilityOf(element));
            priceText = element.getText();
            priceText = priceText.replace("$", "");
            priceText = priceText.replace(" (included taxes)", "");
            priceText = priceText.replace("Total ", "");
            priceText = priceText.replace(",", "").trim();

            priceData = Double.valueOf(priceText);

            calcPrice = price;
            if(priceData == calcPrice) {
    			testResult = true;
    		}else {
    			testResult = false;
    		}
    		return testResult;
    	}

        /**
         * 宿泊期間の検証
         * @throws InterruptedException
         */
        public boolean testTerm(String commandLocater, int term) throws InterruptedException {
        	boolean testResult = false;

    		String termText;
    		String stay = String.valueOf(term) + "泊";

            WebElement elementTerm = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.visibilityOf(elementTerm));
            termText = elementTerm.getText();

            Pattern pFrom = Pattern.compile(dateFrom);
            Matcher mFrom = pFrom.matcher(termText);
            if(mFrom.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }

            Pattern pTo = Pattern.compile(dateTo);
            Matcher mTo = pTo.matcher(termText);
            if(mTo.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }

            Pattern pStay = Pattern.compile(stay);
            Matcher mStay = pStay.matcher(termText);
            if(mStay.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }
            Thread.sleep(500);

        	return testResult;
        }

        public boolean testTermEN(String commandLocater, int term) throws InterruptedException {
        	boolean testResult = false;

    		String termText;
    		String stay = String.valueOf(term) + " night";

            WebElement elementTerm = webDriver.findElement(By.id(commandLocater));
            wait.until(ExpectedConditions.visibilityOf(elementTerm));
            termText = elementTerm.getText();

            Pattern pFrom = Pattern.compile(dateFrom);
            Matcher mFrom = pFrom.matcher(termText);
            if(mFrom.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }

            Pattern pTo = Pattern.compile(dateTo);
            Matcher mTo = pTo.matcher(termText);
            if(mTo.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }

            Pattern pStay = Pattern.compile(stay);
            Matcher mStay = pStay.matcher(termText);
            if(mStay.find()) {
            	testResult = true;
            }else {
            	testResult = false;
            }
            Thread.sleep(500);

        	return testResult;
        }

        /**
         * 指定したエレメントのテキストが期待値通りかチェックする
         * @throws InterruptedException
         */
        public boolean testText(String selector, String text) throws InterruptedException {
        	String resultText;
        	boolean res;

        	WebElement element = webDriver.findElement(By.id(selector));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(element);
    		actions.perform();
    		Thread.sleep(500);

        	wait.until(ExpectedConditions.visibilityOf(element));
        	resultText = element.getText();
        	if(resultText.equalsIgnoreCase(text)) {
        		res = true;
        	}else {
        		res = false;
        	}

        	return res;
        }

        public boolean testTextX(String selector, String text) throws InterruptedException {
        	String resultText;
        	boolean res;

        	WebElement element = webDriver.findElement(By.xpath(selector));
        	wait.until(ExpectedConditions.visibilityOf(element));
    		Actions actions = new Actions(webDriver);
    		actions.moveToElement(element);
    		actions.perform();
    		Thread.sleep(500);

        	resultText = element.getText();
        	if(resultText.equalsIgnoreCase(text)) {
        		res = true;
        	}else {
        		res = false;
        	}

        	return res;
        }

        /**
         * 指定されたテキストが、現在のページ内にあるかチェックする
         * @param text 検索対象のテキスト
         * @return テキストが見つかれば true, 見つからなければ false を返す
         */
        public boolean isTextPresent(String text) {
            WebElement content = webDriver.findElement(By.tagName("body"));
            boolean res = content.getText().contains(text);
            return res;
        }

    	public boolean isPopUpPresent(String text) throws InterruptedException {
    	    try {
    		        Alert alert = webDriver.switchTo().alert();
    		        String alertText = alert.getText();
    		        if (acceptNextAlert) {
    		            alert.accept();
    		        } else {
    		            alert.dismiss();
    		        }
    		        boolean res = alertText.contains(text);
    		        Thread.sleep(1000);
    			    acceptNextAlert = true;
    		        return res;

    		    } finally {
    		      acceptNextAlert = true;
    		    }
    	}

    	public boolean checkContensList(String commandLocater, String planName, String hyouji) throws InterruptedException {
    		String targetContent = null;
    		List<WebElement> contentsList = webDriver.findElements(By.className(commandLocater));
    		boolean res = false;
    		Actions actions = new Actions(webDriver);

    		for(WebElement content : contentsList) {
    			if(targetContent.equals(planName)) {
            		actions.moveToElement(content);
            		actions.perform();
            		Thread.sleep(500);
    				res = hyouji.contains("yes");
    			}else {
    				res= hyouji.contains("no");
    			}
    			if(targetContent.equals(planName)) {
    				break;
    			}
    		}
    		Thread.sleep(500);
    		return res;
    	}

        public void assertTable(String className, List expect) {

            WebElement table = webDriver.findElement(By.className(className));

            List <WebElement>rows = table.findElements(By.tagName("tr"));
            int columnCount = ((SearchContext) rows.get(0)).findElements(By.xpath("./*")).size();
            assertThat(rows.size() * columnCount, is(expect.size()));

            int ix = 0;
            for (WebElement row : rows) {

                List <WebElement>cells = row.findElements(By.xpath("./*"));
                for (WebElement cell : cells) {
                    assertThat(cell.getText(), is(expect.get(ix)));
                    ix++;
                }
            }
        }


        /**
    	*ページタイトルの検証
    	*/
        public boolean isTitlePresent(String title) {
        	boolean res = WebConnector.webDriver.getTitle().contains(title);
        	return res;
        }

        public String getReserveUser(String commandLocater1) throws InterruptedException {
    		String contactText = null;

    		WebElement name = webDriver.findElement(By.id(commandLocater1));
            wait.until(ExpectedConditions.visibilityOf(name));
            username = name.getAttribute("value");

            Thread.sleep(500);
    		return contactText;
        }
}
