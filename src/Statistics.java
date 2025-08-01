import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private int totalCallsWithKnownOS = 0;
    private final HashSet<String> sitePages = new HashSet<>();
    private final HashMap<OperationSystemType, Integer> operatingSystems = new HashMap<>();
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
            sitePages.add(logEntry.getPath());
        }

        OperationSystemType key = logEntry.getUserAgent().getOperationSystemType();
        if (key != OperationSystemType.NOT_DEFINED) {
            totalCallsWithKnownOS++;
            operatingSystems.merge(key, 1, Integer::sum);
        }
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(maxTime, minTime);

        return (double) totalTraffic / duration.toHours();
    }

    public HashSet<String> getSitePages() {
        return sitePages;
    }

    public HashMap<OperationSystemType, Double> calculateOSShare() {
        HashMap<OperationSystemType, Double> calculateShare = new HashMap<>();
        for (var entry : operatingSystems.entrySet()) {
            calculateShare.put(entry.getKey(), (double) entry.getValue() / totalCallsWithKnownOS);
        }
        return calculateShare;
    }
}
