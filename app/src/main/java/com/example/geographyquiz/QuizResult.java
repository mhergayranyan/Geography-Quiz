package com.example.geographyquiz;

public class QuizResult {
    private int score;
    private long timestamp;

    public QuizResult(int score, long timestamp) {
        this.score = score;
        this.timestamp = timestamp;
    }

    public int getScore() {
        return score;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

