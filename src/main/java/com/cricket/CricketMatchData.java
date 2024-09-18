package com.cricket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CricketMatchData {

    public static void main(String[] args) {
        try {
            // Call the API and fetch the data
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.cuvora.com/car/partner/cricket-data"))
                    .header("apiKey", "test-creds@2320")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Parse the JSON response using Gson
            JsonArray matches = JsonParser.parseString(responseBody).getAsJsonArray();

            int highestScore = 0;
            String highestScoringTeam = "";
            int matchCount300Plus = 0;

            // Iterate through matches and calculate results
            for (int i = 0; i < matches.size(); i++) {
                JsonObject match = matches.get(i).getAsJsonObject();

                // Get team names and their scores
                String team1 = match.get("t1").getAsString();
                String team2 = match.get("t2").getAsString();

                // Extract scores, ensuring they are valid
                String team1ScoreStr = match.get("t1s").getAsString().split("/")[0]; // Score before the "/"
                String team2ScoreStr = match.get("t2s").getAsString().split("/")[0];

                int team1Score = Integer.parseInt(team1ScoreStr.isEmpty() ? "0" : team1ScoreStr);
                int team2Score = Integer.parseInt(team2ScoreStr.isEmpty() ? "0" : team2ScoreStr);

                // Calculate total score of both teams
                int totalScore = team1Score + team2Score;

                // Check for the highest individual team score
                if (team1Score > highestScore) {
                    highestScore = team1Score;
                    highestScoringTeam = team1;
                }
                if (team2Score > highestScore) {
                    highestScore = team2Score;
                    highestScoringTeam = team2;
                }

                // Check if the total score of both teams exceeds 300
                if (totalScore > 300) {
                    matchCount300Plus++;
                }
            }

            // Print results
            System.out.println("Highest Score: " + highestScore + " and Team Name is: " + highestScoringTeam);
            System.out.println("Number of Matches with total 300 Plus Score: " + matchCount300Plus);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
