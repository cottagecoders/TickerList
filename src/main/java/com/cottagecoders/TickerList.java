package com.cottagecoders;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TickerList {

    private static class Args {
        @Parameter(names = {"-d", "--debug"}, description = "Enable debug mode")
        private boolean debug = false;
    }

    private record Company(String ticker, String name) {}

    public static void main(String[] args) throws IOException {
        Args arguments = new Args();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        if (arguments.debug) {
            System.out.println("Debug mode is enabled.");
        }

        String filePathEnv = System.getenv("TICKER_LIST");
        if (filePathEnv == null || filePathEnv.isEmpty()) {
            throw new IllegalStateException("Environment variable TICKER_LIST is not set.");
        }
        Path outputPath = Paths.get(filePathEnv);

        if (arguments.debug) {
            System.out.println("Fetching S&P 500 companies from Wikipedia...");
        }

        List<Company> companies = fetchSAndP500Companies();

        // Sort by ticker
        companies.sort(Comparator.comparing(Company::ticker));

        // Format: ticker|name
        String content = companies.stream()
                .map(c -> c.ticker() + "|" + c.name())
                .collect(Collectors.joining("\n"));

        Files.writeString(outputPath, content);

        if (arguments.debug) {
            System.out.println("Successfully wrote " + companies.size() + " companies to: " + outputPath.toAbsolutePath());
        }
    }

    private static List<Company> fetchSAndP500Companies() throws IOException {
        String url = "https://en.wikipedia.org/wiki/List_of_S&P_500_companies";
        Document doc = Jsoup.connect(url).get();
        // The S&P 500 table typically has the class 'wikitable'
        Elements rows = doc.select("table.wikitable tr");

        List<Company> companies = new ArrayList<>();

        // Skip header row (index 0)
        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            if (cols.size() >= 2) {
                String ticker = cols.get(0).text().trim();
                String name = cols.get(1).text().trim();

                if (!ticker.isEmpty() && !name.isEmpty()) {
                    companies.add(new Company(ticker, name));
                }
            }
        }
        return companies;
    }
}
