import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        String fireballDB = "https://ssd-api.jpl.nasa.gov/fireball.api?req-loc=true";
        String dataHTML = getHTML(fireballDB);
        System.out.println(dataHTML);

        JSONObject dataJSON = new JSONObject(dataHTML);
        System.out.println(dataJSON.getString("fields"));
        System.out.println(dataJSON.getString("data"));



    }
    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (var reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
