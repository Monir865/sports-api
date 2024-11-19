package org.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PlayerDetails {

    private static final String API_URL = "https://sportapi7.p.rapidapi.com/api/v1/player/";
    private static final String API_KEY = "e50a4582c3mshca839e3fd699ffdp1ccd5bjsn5be7f0a437fa";
    private static final String API_HOST = "sportapi7.p.rapidapi.com";

    public static void main(String[] args) {
        int playerId = 3; // Example player ID
        fetchAndDisplayPlayerDetails(playerId);
    }


    private static void fetchAndDisplayPlayerDetails(int playerId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + playerId))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                System.out.println("API Response: " + responseBody);  // Print the raw response
                parseAndDisplayPlayerDetails(responseBody);
            } else {
                System.out.println("Error: Received HTTP status code " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    private static void parseAndDisplayPlayerDetails(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject != null && jsonObject.has("player")) {
            JsonObject playerObject = jsonObject.getAsJsonObject("player");

            String name = getJsonValue(playerObject, "player", "name");
            String position = getJsonValue(playerObject, "player", "position");
            String nationality = getJsonValue(playerObject, "player", "nationality");
            String team = getJsonValue(playerObject, "player", "team");
            String age = getJsonValue(playerObject, "player", "age");

            System.out.printf(
                    "Player Details:%n" +
                            "Name: %s%n" +
                            "Position: %s%n" +
                            "Nationality: %s%n" +
                            "Team: %s%n" +
                            "Age: %s%n",
                    name, position, nationality, team, age
            );
        } else {
            System.out.println("No valid player data found in the response.");
        }
    }
    */
    private static void parseAndDisplayPlayerDetails(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        // Check if the player data is available in the response
        if (jsonObject != null && jsonObject.has("player")) {
            JsonObject playerObject = jsonObject.getAsJsonObject("player");

            // Correct the key accesses based on the actual structure of the response
            String name = getJsonValue(playerObject, "name");
            String position = getJsonValue(playerObject, "position");
            String nationality = getJsonValue(playerObject, "country", "name");
            String team = getJsonValue(playerObject, "team", "name");
            String age = getJsonValue(playerObject, "dateOfBirthTimestamp");

            System.out.printf(
                    "Player Details:%n" +
                            "Name: %s%n" +
                            "Position: %s%n" +
                            "Nationality: %s%n" +
                            "Team: %s%n" +
                            "Age: %s%n",
                    name, position, nationality, team, age
            );
        } else {
            System.out.println("No valid player data found in the response.");
        }
    }

    /*
    private static String getJsonValue(JsonObject jsonObject, String parentKey, String key) {
        if (jsonObject.has(parentKey)) {
            JsonObject parentObject = jsonObject.getAsJsonObject(parentKey);
            if (parentObject != null && parentObject.has(key) && !parentObject.get(key).isJsonNull()) {
                return parentObject.get(key).getAsString();
            }
        }
        return "N/A";
    }
    */
    private static String getJsonValue(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "N/A";
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
