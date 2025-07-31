import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic = 0;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;

    public Statistics() {
    }

    public void addEntry (LogEntry logEntry){
        totalTraffic += logEntry.getResponseSize();

        LocalDateTime requestTime = logEntry.getTime();
        if (requestTime.isAfter(maxTime)){
            maxTime = requestTime;
        }
        if (requestTime.isBefore(minTime)){
            minTime = requestTime;
        }
    }

    public double getTrafficRate(){
        Duration duration = Duration.between(maxTime, minTime);

        return (double) totalTraffic / duration.toHours();
    }
}
