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

        double currentLat, currentLong;
        String currentLatDir, currentLongdir;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Latitude (12.34)");
        currentLat = Double.parseDouble(scanner.nextLine());

        System.out.println("Enter Latitude North or South (N/S)");
        currentLatDir = scanner.nextLine();

        System.out.println("Enter Longitude (12.34)");
        currentLong = Double.parseDouble(scanner.nextLine());

        System.out.println("Enter Longitude East or West (E/W)");
        currentLongdir = scanner.nextLine();

        System.out.println("Calculating ---");


        String fireballDB = "https://ssd-api.jpl.nasa.gov/fireball.api?req-loc=true";

        String dataHTML = getHTML(fireballDB);
        JSONObject dataJSON = new JSONObject(dataHTML);
        JSONArray fireballDataArr = dataJSON.getJSONArray("data");
        System.out.println(dataHTML);

        double currentFoundLat = 0, currentFoundLong = 0;
        String currentFoundLatDir = null, currentFoundLongDir = null;
        double currentFoundHigestEnergy = -1.0;
        boolean foundFireball = false;

        for (int i = 0; i < fireballDataArr.length(); i++) {
            JSONArray fireballNodeArr = fireballDataArr.getJSONArray(i);

            Double lat = Double.parseDouble((String) fireballNodeArr.get(3));
            String latDir = (String) fireballNodeArr.get(4);
            Double longT = Double.parseDouble((String) fireballNodeArr.get(5));
            String longDir = (String) fireballNodeArr.get(6);

            if (checkLat(lat, latDir, currentLat, currentLatDir, viewDeg) && checkLong(longT, longDir, currentLong, currentLongdir, viewDeg)) {
                double impactEnergy = Double.parseDouble((String) fireballNodeArr.get(1));
                if (impactEnergy > currentFoundHigestEnergy) {
                    foundFireball = true;
                    currentFoundLat = lat;
                    currentFoundLatDir = latDir;
                    currentFoundLong = longT;
                    currentFoundLongDir = longDir;
                    currentFoundHigestEnergy = impactEnergy;
                }
            }
        }

        if (foundFireball) {
            System.out.println("The nearest fireball to the given location with the highest energy is at: ");
            System.out.println("Latitude: " + currentFoundLat + " " + currentFoundLatDir);
            System.out.println("Longitude: " + currentFoundLong + " " + currentFoundLongDir);
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

    //S set to negative arbitrary
    public static boolean checkLat(Double checkLatt, String checkDir, Double currentLatt, String currentDir, int deg) {
        currentLatt = (currentDir.equals("S")) ? currentLatt * -1 : currentLatt;
        checkLatt = (checkDir.equals("S")) ? checkLatt * -1 : checkLatt;

        return checkLatt >= (currentLatt - deg) % 180 && checkLatt <= (currentLatt + deg) % 180;
    }

    //W set to negative arbitrary
    public static boolean checkLong(Double checkLongt, String checkDir, Double currentLongt, String currentDir, int deg) {
        currentLongt = (currentDir.equals("W")) ? currentLongt * -1 : currentLongt;
        checkLongt = (currentDir.equals("W")) ? checkLongt * -1 : checkLongt;

        return checkLongt >= (currentLongt - deg) % 180 && checkLongt <= (currentLongt + deg) % 180;
    }
}
