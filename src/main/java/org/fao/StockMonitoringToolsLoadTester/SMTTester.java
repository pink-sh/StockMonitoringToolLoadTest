package org.fao.StockMonitoringToolsLoadTester;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.EnumUtils;
import org.fao.StockMonitoringToolsLoadTester.utils.Utils;

public class SMTTester 
{
	
	private static Integer def_nThreads = 1;
	
	private static enum ValidMethods { all, cmsy, elefanga, elefansa, elefan, sbpr, ypr }; 
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Options options = new Options();
    	options.addOption(Utils.createOption("h", "Print this help", false, false));
    	options.addOption(Utils.createOption("endpoint", "Endpoint of the SMT to test", true, true));
    	options.addOption(Utils.createOption("threads", "The number of simultaneously threads min=0 max=30, default=5", true, false));
    	options.addOption(Utils.createOption("method", "The method to be tested, can be all|cmsy|elefanga|elefansa|elefan|sbpr|ypr", true, true));
    	options.addOption(Utils.createOption("shinyproxy", "Use this if the application you are testing is running under a ShinyProxy", false, false));
    	options.addOption(Utils.createOption("chromedriver", "Path to the chromedriver executable", true, true));
    	options.addOption(Utils.createOption("filecmsy", "Path to the cmsy dataset file, if not provided default dataset is used", true, false));
    	options.addOption(Utils.createOption("fileelefan", "Path to the elefan dataset file, if not provided default dataset is used", true, false));
    	options.addOption(Utils.createOption("filefishmethods", "Path to the fish methods dataset file, if not provided default dataset is used", true, false));
    	
    	CommandLineParser parser = new DefaultParser();
    	CommandLine cmd;
		try {
			cmd = parser.parse( options, args);
			if (cmd.hasOption("h")) {
				printHelp(options);
				System.exit(0);
			}
			
			File f = new File(cmd.getOptionValue("chromedriver"));
			if (!f.exists()) {
				System.out.println("Error reading chromedriver");
				System.out.println("");
				printHelp(options);
				System.exit(2);
			}
			String chromedriver = f.getAbsolutePath(); 
	    	
	    	Integer nThreads;
	    	try {
	    		nThreads = Integer.parseInt(cmd.getOptionValue("threads"));
	    	} catch (NumberFormatException ex) {
	    		nThreads = def_nThreads;
	    	}
	    	
	    	if (nThreads < 1 || nThreads > 30) {
	    		System.out.println("Threads must be between 1 and 30.");
	    		printHelp(options);
	    		System.exit(2	);
	    	}
	    	
	    	String endPoint = cmd.getOptionValue("endpoint");
	    	try {
	    		Utils.testUrl(endPoint);
	    	} catch (MalformedURLException ex) {
	    		ex.printStackTrace();
	    		System.exit(1);
	    	}
	    	
	    	Boolean shinyproxy = cmd.hasOption("shinyproxy") ? true : false;
	    	
	    	String method = cmd.getOptionValue("method").toLowerCase();
	    	
	    	if (!EnumUtils.isValidEnum(ValidMethods.class, method)) {
	    		printHelp(options);
	    		System.exit(2);
	    	}
	    	
	    	HashMap<String, String> sampleFiles = new HashMap<String, String>();
	    	sampleFiles.put("cmsy", Utils.getFileFromResourcesFolder("O_Stocks_Catch_15_Med_sample.csv").getAbsolutePath());
	    	sampleFiles.put("elefan", Utils.getFileFromResourcesFolder("elefanSampleFile.csv").getAbsolutePath());
	    	sampleFiles.put("fishmethods", Utils.getFileFromResourcesFolder("FishMethods_SampleDatafile.csv").getAbsolutePath());
	    	
	    	if (cmd.hasOption("filecmsy")) {
	    		File fcmsy = new File(cmd.getOptionValue("filecmsy"));
	    		if (!fcmsy.exists()) {
	    			System.out.println("Error reading file cmsy");
					System.out.println("");
					printHelp(options);
					System.exit(2);
	    		}
		    	sampleFiles.put("cmsy", cmd.getOptionValue("filecmsy"));
	    	}
	    	
	    	if (cmd.hasOption("fileelefan")) {
	    		File fcmsy = new File(cmd.getOptionValue("fileelefan"));
	    		if (!fcmsy.exists()) {
	    			System.out.println("Error reading file elefan");
					System.out.println("");
					printHelp(options);
					System.exit(2);
	    		}
		    	sampleFiles.put("elefan", cmd.getOptionValue("fileelefan"));
	    	}
	    	
	    	if (cmd.hasOption("filefishmethods")) {
	    		File fcmsy = new File(cmd.getOptionValue("filefishmethods"));
	    		if (!fcmsy.exists()) {
	    			System.out.println("Error reading file fish methods");
					System.out.println("");
					printHelp(options);
					System.exit(2);
	    		}
		    	sampleFiles.put("fishmethods", cmd.getOptionValue("filefishmethods"));
	    	}
	    	
	    	System.out.println("----------------------");
	    	System.out.println("Running with:");
	    	System.out.println(String.format("Endpoint: %s", endPoint));
	    	System.out.println(String.format("Method: %s", method));
	    	System.out.println(String.format("Threads: %s", nThreads));
	    	System.out.println(String.format("Chromedriver: %s", chromedriver));
	    	System.out.println(String.format("Shinyproxy: %s", shinyproxy));
	    	
	    	List<Object> runners = getRunners(method, nThreads, endPoint, shinyproxy, chromedriver, sampleFiles);
	    	
	    	
	    	ExecutorService es = Executors.newCachedThreadPool();
	    	for(Object runner : runners)
	    	    es.execute((Runnable)runner);
	    	es.shutdown();
	    	boolean finished = es.awaitTermination(10, TimeUnit.MINUTES);
	    	
	    	if (finished) System.exit(0);
	    	
	    	System.exit(0);
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printHelp(options);
		}  
    }
    
    private static List<Object> getRunners(String method, Integer threads, String endPoint, Boolean shinyproxy, String chromedriver, HashMap<String, String> files) {
    	switch (method) {
    		case "cmsy" : return Utils.getCmsyRunners(threads, endPoint, shinyproxy, chromedriver, files.get("cmsy"));
    		case "elefanga" : return Utils.getElefanGaRunners(threads, endPoint, shinyproxy, chromedriver, files.get("elefan"));
    		case "elefansa" : return Utils.getElefanSaRunners(threads, endPoint, shinyproxy, chromedriver, files.get("elefan"));
    		case "elefan" : return Utils.getElefanRunners(threads, endPoint, shinyproxy, chromedriver, files.get("elefan")); 
    		case "sbpr" : return Utils.getSbprRunners(threads, endPoint, shinyproxy, chromedriver, files.get("fishmethods")); 
    		case "ypr" : return Utils.getYprRunners(threads, endPoint, shinyproxy, chromedriver, files.get("fishmethods"));
    		default : return Utils.shuffleRunners(threads, endPoint, shinyproxy, chromedriver, files);  
    	}
    }
    
    private static void printHelp(Options options) {
    	HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Stock Monitoring Tools load tester", options);
		System.out.println("");
		System.out.println("---- Additional Information ----");
		System.out.println("The application relies on the chromedriver. You can download the appropriate driver");
		System.out.println("from: https://chromedriver.chromium.org/downloads");
		System.out.println("");
		System.out.println("Please note that images are not displayed during tests.");
		System.out.println("");
    }
}
