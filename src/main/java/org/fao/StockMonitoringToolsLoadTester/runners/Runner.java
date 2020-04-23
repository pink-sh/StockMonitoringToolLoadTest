package org.fao.StockMonitoringToolsLoadTester.runners;

public class Runner {
	public String BASE_URL;
	public Boolean shinyproxy;
	public String chromedriver;
	public String testFile;

	public Runner(String baseUrl, Boolean shinyproxy, String chromedriver, String testFile) {
		this.BASE_URL = baseUrl;
		this.shinyproxy = shinyproxy;
		this.chromedriver = chromedriver;
		this.testFile = testFile;
	}
}
