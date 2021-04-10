package ru.zilzilok.avid.boardgames.models.dto;

import java.util.List;

public abstract class BoardGameDto {

    public abstract String getDescription();

    public abstract String getDescriptionShort();

    public abstract String getPhotoUrl();

    public abstract List<String> getTitles();

    public abstract String getAlias();

    public abstract int getPlayersMin();

    public abstract int getPlayersMax();

    public abstract int getPlayersMinRecommend();

    public abstract int getPlayersMaxRecommend();

    public abstract int getPlaytimeMin();

    public abstract int getPlaytimeMax();

    public abstract int getPlayersAgeMin();

    public abstract int getYear();
}
