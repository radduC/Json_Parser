import jsonparser.EventCorrelation;

public class App {
    public static void main(String[] args) throws Exception {
        EventCorrelation eventCorrelation = new EventCorrelation();

        eventCorrelation.correlateEvents("resources/input/example.json", "resources/queries/query.txt", "resources/output/output.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_0.json", "resources/queries/query_0.txt", "resources/output/output_0.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_1.json", "resources/queries/query_1.txt", "resources/output/output_1.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_2.json", "resources/queries/query_2.txt", "resources/output/output_2.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_3.json", "resources/queries/query_3.txt", "resources/output/output_3.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_4.json", "resources/queries/query_4.txt", "resources/output/output_4.txt");
        eventCorrelation.correlateEvents("resources/input/traffic_5.json", "resources/queries/query_5.txt", "resources/output/output_5.txt");
    }
}
