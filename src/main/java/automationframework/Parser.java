package automationframework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class Parser extends SetUrl {

	public static void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sleepMore() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parser(ArrayList list){

		System.out.println("Started");
		/*
		 * For Chrome Driver
		 */
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			System.setProperty("webdriver.chrome.driver", "./src/main/resources/drivers/chromedriver.exe");
		} else {
			System.setProperty("webdriver.chrome.driver", "./src/main/resources/drivers/chromedriver");
		}
		//System.setProperty("webdriver.ie.driver", "D:\\SAYANTAN\\Software bank\\Selenium Drivers\\IEDriverServer.exe");
		WebDriver driver = new ChromeDriver();
		final long startTime = System.currentTimeMillis();
		/*
		 * For IEDriver
		 */
		//WebDriver driver = new InternetExplorerDriver();
		driver.get(baseUrl);
		driver.manage().window().maximize();
		System.out.println("Url opened");

		/* Start of Login Screen*/
		System.out.println("Entering login credentials");
		driver.findElement(By.id("unamebean")).clear();
		driver.findElement(By.id("unamebean")).sendKeys("piyaly_guha");		
		driver.findElement(By.id("pwdbean")).clear();
		driver.findElement(By.id("pwdbean")).sendKeys("welcome06");		
		driver.findElement(By.id("SubmitButton")).click();
		System.out.println("Logged in to the system.");
		/*End of Login Screen*/	
		/* Start of Main menu navigation*/
		System.out.println("Navigating MHE Change Control.");
		sleep();
		driver.findElement(By.linkText("MHE Change Control")).click();
		sleep();
		driver.findElement(By.linkText("Setup")).click();
		sleep();
		driver.findElement(By.id("Setup")).click();
		sleep();
		System.out.println("Navigating MHE Change Control finished.");
		int executedline = 1;
		int noofblankrow = 0;

		System.out.println("Start of Loop and Excel feeding...");
		for ( int count = 0 ; count < list.size(); count++){
			ExcelValueVO excelValueVO = (ExcelValueVO)list.get(count);			
			System.out.println("Line of Execution "+executedline);
			System.out.println("::::::::::START TEST DATA:::::::::");
			if(excelValueVO.getIcc()==null || excelValueVO.getIcc()==""){
				System.out.println("No data in ICC");				
				noofblankrow++;
			}else{
				System.out.println("Entering Item Catalog Category...."+excelValueVO.getIcc());
				driver.findElement(By.id("EgoItemTypeListSearchValue")).clear();
				driver.findElement(By.id("EgoItemTypeListSearchValue")).sendKeys(excelValueVO.getIcc());
				System.out.println("Finished Item Catalog Category....");
				System.out.println("Clicking on Go...");
				driver.findElement(By.id("EgoItemTypeListSearchGoBut")).click();
				System.out.println("Clicking on Go...");
				System.out.println("Clicking...");
				sleep();
				driver.findElement(By.id("N5:EgoCatalogGroupName:0")).click();
				System.out.println("Clicking on Attribute Groups...");
				sleep();
				driver.findElement(By.id("EgoAttributeGroupsLink")).click();
				sleep();
				/*
				 * Start of Selecting Dropdown
				 */
				WebElement dropdown = driver.findElement(By.id ("DataLevelChoice"));
				Select se=new Select(dropdown);
				se.selectByVisibleText(excelValueVO.getViewAttrGroup());
				sleep();
				System.out.println(excelValueVO.getViewAttrGroup()+" selected...");	
				/*
				 * End of Selecting Dropdown
				 */
				System.out.println("Entering data search field ....");
				driver.findElement(By.id("searchInput")).sendKeys(excelValueVO.getDisplayName());
				System.out.println("Clicked on Search ....");
				driver.findElement(By.id("searchGoButton")).click();
				sleep();
				Boolean isPresent= driver.findElements(By.id("N4:AttrGroupDispName:0")).size()>0;
				System.out.println("If no result then add attribute ....");
				if (isPresent == false){
					System.out.println("No result found...");
					driver.findElement(By.id("FndAdd")).click();
					sleep();
					
					System.out.println("Entering data in Internal name..."+excelValueVO.getInternalName());
					driver.findElement(By.id("Value_2")).clear();
					driver.findElement(By.id("Value_2")).sendKeys(excelValueVO.getInternalName());sleep();
					driver.findElement(By.xpath("(//button[@type='submit'])[1]")).click();sleep();
					System.out.println("Clicking on Select All");
					driver.findElement(By.xpath("//*[@id='EGOEDITASSOCIATIONDSL_NestedIt.AttrGroupsTable']/table[1]/tbody/tr[2]/td/table/tbody/tr/td/a[1]")).click();
					driver.findElement(By.id("applyButton")).click();sleep();
					System.out.println("::::::::::END TEST DATA:::::::::");
				}  else{

					System.out.println("Results found");
					driver.findElement(By.id("EGO_ITEMS_TAB_MENU_ENTRIES")).click();sleep();
					System.out.println("Returning back");
					System.out.println("::::::::::END TEST DATA:::::::::");
				
				}  		
				executedline ++;
				noofblankrow = 0;
			}
			if(noofblankrow >2){
				executedline --;
				driver.close();
				driver.quit();
				
			}
		
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("Executed "+executedline+" lines");
		System.out.println("Total execution time: " + (endTime - startTime)/1000 + " sec" );
		System.exit(0);
	}
	
	public static void datafeed(){
		
		ReadingExcel re = new ReadingExcel();
		try {		
 			ArrayList data = re.readExcel();
			/*for ( int count = 0; count< data.size();count ++){
				
				ExcelValueVO excelValueVO = (ExcelValueVO)data.get(count);
				System.out.println(excelValueVO.getInternalName());
			}*/
			parser(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		datafeed();
	}

}
