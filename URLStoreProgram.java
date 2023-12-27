package com.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class URLStoreProgram {
    private static Map<String, URLData> urlDatabase = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.print("Enter command: ");
            String command = scanner.next();

            switch (command.toLowerCase()) {
                case "storeurl":
                    String urlToStore = scanner.next();
                    storeURL(urlToStore);
                    break;

                case "get":
                    String urlToGet = scanner.next();
                    String shortKey = getShortKey(urlToGet);
                    System.out.println("Short Key: " + shortKey);
                    break;

                case "count":
                    String urlToCount = scanner.next();
                    int usageCount = getUsageCount(urlToCount);
                    System.out.println("Usage Count: " + usageCount);
                    break;

                case "list":
                    String jsonResult = listURLs();
                    System.out.println("URLs and Counts: " + jsonResult);
                    break;

                case "exit":
                    isRunning = false;
                    System.out.println("Exiting program.");
                    break;

                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

    private static void storeURL(String url) {
        String shortKey = generateShortKey();
        urlDatabase.put(shortKey, new URLData(url, 0));
        System.out.println("URL stored with short key: " + shortKey);
    }

    private static String getShortKey(String url) {
        for (Map.Entry<String, URLData> entry : urlDatabase.entrySet()) {
            if (entry.getValue().getUrl().equals(url)) {
                entry.getValue().incrementUsageCount();
                return entry.getKey();
            }
        }
        return null; 
    }

    private static int getUsageCount(String url) {
        for (URLData urlData : urlDatabase.values()) {
            if (urlData.getUrl().equals(url)) {
                return urlData.getUsageCount();
            }
        }
        return -1;
    }

    private static String listURLs() {
        StringBuilder result = new StringBuilder("{ ");
        for (Map.Entry<String, URLData> entry : urlDatabase.entrySet()) {
            result.append("\"").append(entry.getValue().getUrl()).append("\": ").append(entry.getValue().getUsageCount()).append(", ");
        }
        result.delete(result.length() - 2, result.length()); 
        result.append(" }");
        return result.toString();
    }

    private static String generateShortKey() {
           	return "shortKey" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    private static class URLData {
        private String url;
        private int usageCount;

        public URLData(String url, int usageCount) {
            this.url = url;
            this.usageCount = usageCount;
        }

        public String getUrl() {
            return url;
        }

        public int getUsageCount() {
            return usageCount;
        }

        public void incrementUsageCount() {
            usageCount++;
        }
    }
}
