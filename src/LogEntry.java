import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    public static final Pattern PATTERN = Pattern.compile(
            "(?<ip>.*?) .*\\[(?<time>.*)] \"(?<method>.*) /(?<path>.*) .*\" (?<responseCode>\\d+) " +
                    "(?<size>\\d+) \"(?<referer>.*)\" \"(?<userAgent>.*)\"");
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "d/LLL/yyyy:HH:mm:ss X", Locale.US);
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new RuntimeException("Can't parse log entry");
        }
        this.ipAddr = matcher.group("ip");
        this.time = LocalDateTime.parse(matcher.group("time"), FORMATTER);
        this.method = HttpMethod.valueOf(matcher.group("method"));
        this.path = matcher.group("path");
        this.responseCode = Integer.parseInt(matcher.group("responseCode"));
        this.responseSize = Integer.parseInt(matcher.group("size"));
        this.referer = matcher.group("referer");
        this.userAgent = new UserAgent(matcher.group("userAgent"));
    }


    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", time=" + time +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                ", referer='" + referer + '\'' +
                ", userAgent=" + userAgent +
                '}';
    }
}
