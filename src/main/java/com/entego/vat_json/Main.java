package com.entego.vat_json;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Main {
    public static final String VAT_URL = "https://euvatrates.com/rates.json";

    public static void main(String[] args) throws IOException {
        URL url = new URL(VAT_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty ( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0" );
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String data = new String();
        String line;
        while((line = reader.readLine()) != null) {
            data += line;
        }

        List<Country> countries = new ArrayList<>();
        JSONObject json = new JSONObject(data);
        JSONObject rates = (JSONObject) json.get("rates");
        for (String key : rates.keySet() ) {
            System.out.println("json = " + key);
            JSONObject detail = rates.getJSONObject(key);
            countries.add(new Country(key, detail.getString("country"), detail.getDouble("standard_rate"), detail.get("reduced_rate") instanceof Boolean ? -1 : detail.getDouble("reduced_rate") ));
        }

        //System.out.println("countries = " + countries);
        countries.sort(new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                int fullDiff = (int)(o2.getFullVat() - o1.getFullVat());
                if (fullDiff != 0) {
                    return fullDiff;
                }
                if (o2.getLowerVat() == -1) {
                    return fullDiff;
                }
                return (int)(o2.getLowerVat() - o1.getLowerVat());
            }
        });

        System.out.println("3 země s nejvyššími sazbami");
        for (int i = 0; i < 3; i++) {
            Country country = countries.get(i);
            System.out.printf("%s (%s): %G %%\n", country.getName(), country.getAbbreviation(), country.getFullVat());
        }

        System.out.println("3 země s nejnižšími  sazbami");
        for (int i = countries.size() - 1; i > countries.size() - 4; i--) {
            Country country = countries.get(i);
            System.out.printf("%s (%s): %G %%\n", country.getName(), country.getAbbreviation(), country.getFullVat());
        }

        System.out.println("Chcete zapsat výsledek do souboru y/N?");
        Scanner scanner = new Scanner(System.in);
        if (scanner.next().equals("y")) {
            File file = new File("./countries_vat.txt");
            PrintWriter pw = new PrintWriter(file);
            pw.println("3 země s nejvyššími sazbami");
            for (int i = 0; i < 3; i++) {
                Country country = countries.get(i);
                pw.printf("%s (%s): %G %%\n", country.getName(), country.getAbbreviation(), country.getFullVat());
            }
            pw.println();

            pw.println("3 země s nejnižšími  sazbami");
            for (int i = countries.size() - 1; i > countries.size() - 4; i--) {
                Country country = countries.get(i);
                pw.printf("%s (%s): %G %%\n", country.getName(), country.getAbbreviation(), country.getFullVat());
            }
            pw.println();

            pw.println("Seznam zemí");
            for (Country country : countries) {
                pw.printf("%s (%s): %G %%\n", country.getName(), country.getAbbreviation(), country.getFullVat());
            }
            pw.close();
        }

    }
}
