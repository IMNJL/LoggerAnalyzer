package backend.academy.logs;

import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.util.List;
import static backend.academy.logs.Config.filePath;

@UtilityClass
public class Main {

    public static void main(String[] args) {
        LogParser logParser = new LogParser();
        LogReader logReader = new LogReader(logParser);

        try {
            List<LogEntry> logs = logReader.readLogFile(filePath);
            LogAnalyzer logAnalyzer = new LogAnalyzer(logs);
            String report = ReportGenerator.generateMarkdownReport(logAnalyzer);
            System.out.println(report);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
