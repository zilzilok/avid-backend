package ru.zilzilok.avid.boardgames.models.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeseraBoardGameDto extends BoardGameDto {

    private String description;
    private String descriptionShort;
    private String photoUrl;
    private String title;
    private String title2;
    private String title3;
    private String alias;
    private int playersMin;
    private int playersMax;
    private int playersMinRecommend;
    private int playersMaxRecommend;
    private int year;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getDescriptionShort() {
        return descriptionShort;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public List<String> getTitles() {
        return Arrays.asList(ArrayUtils.nullToEmpty(new String[]{title, title2, title2}));
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public int getPlayersMin() {
        return playersMin;
    }

    @Override
    public int getPlayersMax() {
        return playersMax;
    }

    @Override
    public int getPlayersMinRecommend() {
        return playersMinRecommend;
    }

    @Override
    public int getPlayersMaxRecommend() {
        return playersMaxRecommend;
    }

    @Override
    public int getYear() {
        return year;
    }
}
