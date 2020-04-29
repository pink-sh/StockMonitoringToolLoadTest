# StockMonitoringToolLoadTest

This tool provides an easy way to load test the StockMonitoringTools Shiny application using a simple command line interface.

The application is built on top of *Java8+Selenium.*

*Selenium* needs a driver to simulate webpage interactions and this tool uses the *ChromeDriver* that is not included in this repository because the application does not run in headless mode. Hence the *Chromedriver* needs to match your Chrome/Chromium version. Chromedriver can be downloaded [here](https://chromedriver.chromium.org/downloads).

### What it does

This tool can test the StockMonitoringTool application by firing up multiple browser windows that interact and run a particular or a random method simultaneously.

### What it does not

This tool does not test slow bandwidth connections. To test such scenarios you might want to have a proxy upfront that shrinks your bandwidth, one option is to use [BrowserMob](http://bmp.lightbody.net/).

### Parameters

| Parameter (bold are mandatory) | Explanation                                                  |
| ------------------------------ | ------------------------------------------------------------ |
| h                              | Prints the help.                                             |
| **endpoint**                   | Set the endpoint to test.                                    |
| **threads**                    | Set how many simultaneously threads you want to test min=0 max=30 |
| **method**                     | Can be one of all\|cmsy\|elefan\|elefanga\|elefansa\|sbpr\|ypr - note that with the *all* value the tool shuffles the algorithms for the number of threads set. |
| shinyproxy                     | No value needed. If set the tool knows that the application about to load test runs under a shinyproxy and it looks for the iFrame shinyproxy renders. |
| **chromedriver**               | Path to your chromedriver executable file.                   |
| filecmsy                       | The cmsy dataset you want to use for the tests. If not set the tool uses the sample dataset. |
| fileelefan                     | The elefan dataset you want to use for the tests. If not set the tool uses the sample dataset. |
| filefishmethods                | The fish methods dataset used for SBPR/YPR methods. If not set the tool uses the sample dataset. |

### Examples

Run the cmsy 5 times simultaneously:

```
java -jar StockMonitoringToolsLoadTester-1.0.jar --endpoint <your endpoint> --threads 5 --method cmsy --chromedriver </path/to/your/chromedriver>
```

Run shuffle methods 15 times simultaneously:

```
java -jar StockMonitoringToolsLoadTester-1.0.jar --endpoint <your endpoint> --threads 15 --method all --chromedriver </path/to/your/chromedriver>
```

Run shuffle methods 20 times simultaneously but uses a particular elefan dataset:

```
java -jar StockMonitoringToolsLoadTester-1.0.jar --endpoint <your endpoint> --threads 20 --method all --chromedriver </path/to/your/chromedriver> --fileelefan </path/to/your/elefan/dataset>
```

