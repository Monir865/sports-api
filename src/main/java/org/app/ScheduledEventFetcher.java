package org.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduledEventFetcher {

    public static void main(String[] args) {
        String apiUrl = "https://sportapi7.p.rapidapi.com/api/v1/sport/football/scheduled-events/2022-02-11";
        String apiKey = "e50a4582c3mshca839e3fd699ffdp1ccd5bjsn5be7f0a437fa";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "sportapi7.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                String responseBody = response.body();
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

                JsonArray events = jsonResponse.getAsJsonArray("events");
                System.out.println("Scheduled Events:");
                int count = 1;

                while (count <= events.size()) {
                    JsonObject event = events.get(count - 1).getAsJsonObject();

                    String tournamentName = getJsonValue(event, "tournament", "name");
                    String homeTeam = getJsonValue(event, "homeTeam", "name");
                    String awayTeam = getJsonValue(event, "awayTeam", "name");
                    String eventStatus = getJsonValue(event, "status", "description");
                    String eventDate = getJsonValue(event, "date", null);
                    String eventTime = getJsonValue(event, "time", null);

                    System.out.println("Event " + count + ":");
                    System.out.println("Tournament: " + tournamentName);
                    System.out.println("Date: " + eventDate + " Time: " + eventTime);
                    System.out.println("Home Team: " + homeTeam);
                    System.out.println("Away Team: " + awayTeam);
                    System.out.println("Status: " + eventStatus);
                    System.out.println("----------------------------");

                    count++;
                }
            } else {
                System.out.println("Failed to fetch data. HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger(ScheduledEventFetcher.class.getName()).log(Level.SEVERE, "Request failed", e);
        }
    }

    private static String getJsonValue(JsonObject jsonObject, String parentKey, String key) {
        if (jsonObject.has(parentKey)) {
            JsonObject parentObject = jsonObject.getAsJsonObject(parentKey);
            if (parentObject != null && parentObject.has(key) && !parentObject.get(key).isJsonNull()) {
                return parentObject.get(key).getAsString();
            }
        }
        return "N/A";
    }
}
