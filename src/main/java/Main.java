import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        int viewDeg = 10;

        Scanner scanner = new Scanner(System.in);



        String fireballDB = "https://ssd-api.jpl.nasa.gov/fireball.api?req-loc=true";

        String dataHTML = getHTML(fireballDB);
        JSONObject dataJSON = new JSONObject(dataHTML);
        JSONArray fireballDataArr = dataJSON.getJSONArray("data");

        int currentFoundLat, currentFoundLong;
        int currentFoundHigestEnergy = -1;

        for (int i = 0; i < fireballDataArr.length(); i++) {
            JSONArray fireballNodeArr = fireballDataArr.getJSONArray(i);
            System.out.println(fireballNodeArr);

            Double lat = Double.parseDouble((String) fireballNodeArr.get(3));

            System.out.println(lat);



        }



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


    public static boolean checkLat(Double latt, String dir, int deg) {

    }

    public static boolean checkLong(Double longt, String dir, int deg) {
        
    }

}
