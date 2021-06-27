package com.cutehub;

import com.cutehub.utils.CallBack;

import java.util.ArrayList;

public class ScoreManager {

    private static ScoreManager scoreManager;
    private ArrayList<CallBack> listOfCallBack = new ArrayList<>();
    private int currentScore;
    private int prevScore;

    public static ScoreManager getInstance() {
        if (scoreManager == null) {
            scoreManager = new ScoreManager();
        }
        return scoreManager;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void resetCurrentScore() {
        currentScore = 0;
    }


    public void addCallBack(CallBack callBack) {
        listOfCallBack.add(callBack);
    }

    public void addScoreByOne() {
        updateScore(currentScore + 1);
    }

    public void updateScore(int score) {
        if (currentScore == score) return;
        currentScore = score;
        for (CallBack callBack : listOfCallBack) {
            callBack.call(this);
        }
    }
}
