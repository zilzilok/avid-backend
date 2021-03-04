package ru.zilzilok.avid.boardgames.models.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        return Arrays.asList(ArrayUtils.nullToEmpty(new String[]{title, title2, title3}));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TeseraBoardGameDto that = (TeseraBoardGameDto) o;

        if(that.getAlias().equals(alias)) {
            return true;
        }

        return new EqualsBuilder().append(playersMin, that.playersMin).append(playersMax, that.playersMax).append(playersMinRecommend, that.playersMinRecommend).append(playersMaxRecommend, that.playersMaxRecommend).append(year, that.year).append(description, that.description).append(descriptionShort, that.descriptionShort).append(photoUrl, that.photoUrl).append(title, that.title).append(title2, that.title2).append(title3, that.title3).append(alias, that.alias).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(description).append(descriptionShort).append(photoUrl).append(title).append(title2).append(title3).append(alias).append(playersMin).append(playersMax).append(playersMinRecommend).append(playersMaxRecommend).append(year).toHashCode();
    }
}
