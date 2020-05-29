package org.fao.StockMonitoringToolsLoadTester.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.cli.Option;
import org.fao.StockMonitoringToolsLoadTester.runners.CmsyRunner;
import org.fao.StockMonitoringToolsLoadTester.runners.ElefanGaRunner;
import org.fao.StockMonitoringToolsLoadTester.runners.ElefanRunner;
import org.fao.StockMonitoringToolsLoadTester.runners.ElefanSaRunner;
import org.fao.StockMonitoringToolsLoadTester.runners.SbprRunner;
import org.fao.StockMonitoringToolsLoadTester.runners.YprRunner;

public class Utils {

	public static void testUrl(String url) throws MalformedURLException {
		new URL(url);
	}
	
	public static Boolean hasQueryParams(String url) throws MalformedURLException {
		URL u = new URL(url);
		if (u.getQuery() == null) return false;
		return true;
	}
	
	public static Option createOption(String shortName, String description, Boolean hasArgs, Boolean required) {
    	return Option.builder().hasArg(hasArgs).required(required).desc(description).argName(shortName).longOpt(shortName).build();
    }
	
	public static List<Object> getCmsyRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new CmsyRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
	
	public static List<Object> getElefanGaRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new ElefanGaRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
	
	public static List<Object> getElefanSaRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new ElefanSaRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
	
	public static List<Object> getElefanRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new ElefanRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
	
	public static List<Object> getSbprRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new SbprRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
	
	public static List<Object> getYprRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, String testFile) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			list.add(new YprRunner(endPoint, shinyproxy, chromedriver, testFile));
		}
		return list;
	}
		
	public static List<Object> shuffleRunners(Integer number, String endPoint, Boolean shinyproxy, String chromedriver, HashMap<String, String> testFiles) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < number; i++) {
			Random rand = new Random(); 
			Integer randInt = rand.nextInt(6000);
			if (isBetween(randInt, 0, 999)) list.add(new CmsyRunner(endPoint, shinyproxy, chromedriver, testFiles.get("cmsy")));
			else if (isBetween(randInt, 1000, 1999)) list.add(new ElefanGaRunner(endPoint, shinyproxy, chromedriver, testFiles.get("elefan")));
			else if (isBetween(randInt, 2000, 2999)) list.add(new ElefanSaRunner(endPoint, shinyproxy, chromedriver, testFiles.get("elefan")));
			else if (isBetween(randInt, 3000, 3999)) list.add(new ElefanRunner(endPoint, shinyproxy, chromedriver, testFiles.get("elefan")));
			else if (isBetween(randInt, 4000, 4999)) list.add(new SbprRunner(endPoint, shinyproxy, chromedriver, testFiles.get("fishmethods")));
			else if (isBetween(randInt, 5000, 5999)) list.add(new YprRunner(endPoint, shinyproxy, chromedriver, testFiles.get("fishmethods")));
		}
		
		return list;
	}
	
	private static boolean isBetween(int x, int lower, int upper) {
		return lower <= x && x <= upper;
	} 
	
	public static File getFileFromResourcesFolder (String f) {

		File tmpFile;
		try {
			tmpFile = File.createTempFile("smtLoadTester", ".csv");
			
			InputStream in = Utils.class.getClassLoader().getResourceAsStream(f); 
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
			
			String l;
			while((l=br.readLine())!=null){

			    bw.write(l);
			    bw.newLine();

			}
			
			br.close();
			bw.close();
			
			return tmpFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		

		
		
	}
	
}
