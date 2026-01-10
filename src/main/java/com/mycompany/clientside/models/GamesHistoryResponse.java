package com.mycompany.clientside.models;

import java.util.ArrayList;

public class GamesHistoryResponse
{
    private ArrayList<GameHistory> gameModels;

    public GamesHistoryResponse(){}

    public GamesHistoryResponse(ArrayList<GameHistory> data)
    {
        gameModels = data;
    }

    public ArrayList<GameHistory> getGameModels() {
        return gameModels;
    }

    public void setGameModels(ArrayList<GameHistory> data)
    {
        gameModels = data;
    }
}
