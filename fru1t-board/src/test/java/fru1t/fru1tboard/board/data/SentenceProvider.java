package fru1t.fru1tboard.board.data;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SentenceProvider {
    private static final String RESOURCE_NAME = "/mock/korean-sentences.txt";

    public List<String> getSentences() {
        List<String> sentences = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(RESOURCE_NAME);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    sentences.add(trimmed);
                }
            }
            return sentences;
        } catch (Exception e) {
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    @Test
    public void sentenceProvider() {
        try {
            List<String> sentences = getSentences();
            System.out.println("sentences = " + sentences.size());
            for (String sentence : sentences) {
                System.out.println("sentence = " + sentence);
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }
}
