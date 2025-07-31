public class UserAgent {
    private final Browser browser;
    private final OperationSystemType operationSystemType;

    public UserAgent(String userAgent) {
        this.operationSystemType = getOperationSystemType(userAgent);
        this.browser = getBrowser(userAgent);
    }

    private Browser getBrowser(String userAgent) {
        if (userAgent.contains("OPR/") || userAgent.contains("presto")) {
            return Browser.OPERA;
        }
        if (userAgent.contains("Firefox")) {
            return Browser.FIEFOX;
        }
        if (userAgent.contains("Edg/") || userAgent.contains("Edge")) {
            return Browser.EDGE;
        }
        if (userAgent.contains("Safari") && userAgent.contains("like Gecko") && userAgent.contains("KHTML")) {
            return Browser.CHROME;
        }
        if (userAgent.contains("Safari")) {
            return Browser.SAFARI;
        }
        return Browser.NOT_DEFINED;
    }

    private OperationSystemType getOperationSystemType(String userAgent) {
        if (userAgent.contains("Windows")){
            return OperationSystemType.WINDOWS;
        }
        if (userAgent.contains("Linux")){
            return OperationSystemType.LINUX;
        }
        if (userAgent.contains("Mac OS")){
            return OperationSystemType.MAC_OS;
        }
        return OperationSystemType.NOT_DEFINED;
    }

    public Browser getBrowser() {
        return browser;
    }

    public OperationSystemType getOperationSystemType() {
        return operationSystemType;
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "browser='" + browser + '\'' +
                ", operationSystemType='" + operationSystemType + '\'' +
                '}';
    }
}
