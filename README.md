
# TickerList

TickerList is a Java application that fetches the list of S&P 500 companies from Wikipedia, parses their ticker symbols and company names, and saves them to a formatted text file.

## Features

- **Web Scraping**: Automatically retrieves real-time S&P 500 data from Wikipedia using Jsoup.
- **Data Formatting**: Produces a clean pipe-separated (`|`) file containing `ticker|name`.
- **Sorted Output**: The resulting file is sorted alphabetically by ticker symbol.
- **Configurable Output**: Uses an environment variable to determine the output destination.
- **Debug Mode**: Includes a debug flag for detailed logging during execution.

## Prerequisites

- **Java Development Kit (JDK) 25** or higher.
- **Apache Maven** for building the project.

## Building the Project

To build the executable "fat" JAR, run the following command in the root directory:
```
mvn clean package
```

## Set the destination
Set the environment variable pointing to the output file.
```
export TICKER_LIST=/path/to/your/output_file.txt
```


## Output Format

The output file will follow this structure:

```
A|Agilent Technologies
AAPL|Apple Inc.
ABBV|AbbVie
ABNB|Airbnb
ABT|Abbott Laboratories
ACGL|Arch Capital Group
ACN|Accenture
```

