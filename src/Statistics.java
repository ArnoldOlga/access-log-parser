import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private int totalCallsWithKnownOS = 0;
    private int totalCallsWithKnownBrowser = 0;
    private final HashSet<String> existingSitePages = new HashSet<>();
    private final HashSet<String> notExistingSitePages = new HashSet<>();
    private final HashMap<OperationSystemType, Integer> operatingSystems = new HashMap<>();
    private final HashMap<Browser, Integer> browser = new HashMap<>();
    private int totalTraffic = 0;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getResponseSize();

        LocalDateTime requestTime = logEntry.getTime();
        if (requestTime.isAfter(maxTime)) {
            maxTime = requestTime;
        }
        if (requestTime.isBefore(minTime)) {
            minTime = requestTime;
        }

        if (logEntry.getResponseCode() == 200) {
            existingSitePages.add(logEntry.getPath());
        }

        if (logEntry.getResponseCode() == 404) {
            notExistingSitePages.add(logEntry.getPath());
        }

        OperationSystemType key = logEntry.getUserAgent().getOperationSystemType();
        if (key != OperationSystemType.NOT_DEFINED) {
            totalCallsWithKnownOS++;
            operatingSystems.merge(key, 1, Integer::sum);
        }

        Browser browserKey = logEntry.getUserAgent().getBrowser();
        if (browserKey != Browser.NOT_DEFINED) {
            totalCallsWithKnownBrowser++;
            browser.merge(browserKey, 1, Integer::sum);
        }
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(maxTime, minTime);

        return (double) totalTraffic / duration.toHours();
    }

    public HashSet<String> getExistingSitePages() {
        return existingSitePages;
    }

    public HashSet<String> getNotExistingSitePages() {
        return notExistingSitePages;
    }

    public HashMap<OperationSystemType, Double> calculateOSShare() {
        HashMap<OperationSystemType, Double> calculateShare = new HashMap<>();
        for (var entry : operatingSystems.entrySet()) {
            calculateShare.put(entry.getKey(), (double) entry.getValue() / totalCallsWithKnownOS);
        }
        return calculateShare;
    }

    public HashMap<Browser, Double> calculateBrowserShare() {
        HashMap<Browser, Double> calculateShare = new HashMap<>();
        for (var entry : browser.entrySet()) {
            calculateShare.put(entry.getKey(), (double) entry.getValue() / totalCallsWithKnownBrowser);
        }
        return calculateShare;
    }
}
