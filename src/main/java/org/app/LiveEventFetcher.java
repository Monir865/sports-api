package org.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LiveEventFetcher {

    public static void main(String[] args) {
        String apiUrl = "https://sportapi7.p.rapidapi.com/api/v1/sport/football/events/live";
        String apiKey = "e50a4582c3mshca839e3fd699ffdp1ccd5bjsn5be7f0a437fa";
        String apiHost = "sportapi7.p.rapidapi.com";

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", apiHost)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                parseAndDisplayEvents(responseBody);
            } else {
                System.out.println("Error: Received HTTP status code " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseAndDisplayEvents(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("events") && jsonObject.get("events").isJsonArray()) {
            JsonArray events = jsonObject.getAsJsonArray("events");
            if (events.size() > 0) {
                System.out.println("Live Football Events:");
                for (int i = 0; i < events.size(); i++) {
                    JsonObject event = events.get(i).getAsJsonObject();

                    String tournamentName = getJsonValue(event, "tournament", "name");
                    String seasonName = getJsonValue(event, "season", "name");
                    String homeTeam = getJsonValue(event, "homeTeam", "name");
                    String awayTeam = getJsonValue(event, "awayTeam", "name");
                    String homeScore = getJsonValue(event, "homeScore", "current");
                    String awayScore = getJsonValue(event, "awayScore", "current");
                    String status = getJsonValue(event, "status", "description");

                    System.out.printf(
                            "Tournament: %s | Season: %s%n" +
                                    "Match: %s vs %s%n" +
                                    "Score: %s - %s | Status: %s%n%n",
                            tournamentName, seasonName, homeTeam, awayTeam, homeScore, awayScore, status
                    );
                }
            } else {
                System.out.println("No live events currently available.");
            }
        } else {
            System.out.println("No valid 'events' data found in the response.");
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
