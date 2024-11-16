//package backend.academy.logs;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class LogRecord {
//    private String ipAddress;
//    private Date requestTime;
//    private String httpMethod;
//    private String resourcePath;
//    private int responseCode;
//    private int responseSize;
//    private String referrer;
//    private String userAgent;
//
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
//
//    public LogRecord(String ipAddress, Date requestTime, String httpMethod, String resourcePath,
//        int responseCode, int responseSize, String referrer, String userAgent) {
//        this.ipAddress = ipAddress;
//        this.requestTime = requestTime;
//        this.httpMethod = httpMethod;
//        this.resourcePath = resourcePath;
//        this.responseCode = responseCode;
//        this.responseSize = responseSize;
//        this.referrer = referrer;
//        this.userAgent = userAgent;
//    }
//
//    public static LogRecord parseLog(String logLine) throws ParseException {
//        String[] parts = logLine.split(" ");
//        String ipAddress = parts[0];
//        Date requestTime = dateFormat.parse(parts[3].substring(1) + " " + parts[4].substring(0, parts[4].length() - 1));
//        String methodAndResource = parts[5] + " " + parts[6] + " " + parts[7];
//        String httpMethod = methodAndResource.split(" ")[0];
//        String resourcePath = methodAndResource.split(" ")[1];
//        int responseCode = Integer.parseInt(parts[8]);
//        int responseSize = Integer.parseInt(parts[9]);
//        String referrer = parts[10].replace("\"", "");
//        String userAgent = parts[11].replace("\"", "");
//
//        return new LogRecord(ipAddress, requestTime, httpMethod, resourcePath, responseCode, responseSize, referrer, userAgent);
//    }
//
//    public String getResourcePath() {
//        return resourcePath;
//    }
//
//    public int getResponseCode() {
//        return responseCode;
//    }
//
//    public int getResponseSize() {
//        return responseSize;
//    }
//}
