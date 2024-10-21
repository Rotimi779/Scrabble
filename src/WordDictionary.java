import java.io.*;
import java.net.*;
import java.util.*;

public class WordDictionary {
    private List<String> words;

    // Constructor to initialize and load words from the online source
    public WordDictionary(String url) {
        words = new ArrayList<>();
        loadWordsFromUrl(url);
    }

    // Fetch the words from the URL and store them in the list
    private void loadWordsFromUrl(String url) {
        try {
            URL link = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(link.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim()); // Add each word to the list, trimming whitespace
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading words: " + e.getMessage());
        }
    }

    // Get all the words as a list
    public List<String> getWords() {
        return words;
    }

    // Check if a specific word is in the dictionary
    public boolean containsWord(String word) {
        return words.contains(word);
    }

    // Get the total number of words in the dictionary
    public int getWordCount() {
        return words.size();
    }
}