package com.example.douin.util.network;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * 单个视频信息类，与数据库对接
 *
 * @author YDJSIR
 */
public class VideoItem {
    @SerializedName("feedurl")
    public String feedURL;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("description")
    public String description;
    @SerializedName("likecount")
    public int likecount;
    @SerializedName("avatar")
    public String avatarURI;
    @SerializedName("thumbnails")
    public String thumbnailURI;
    @SerializedName("_id")
    private String id;

    public VideoItem() {

    }

    public VideoItem(String _id, String feedURL, String nickname, String description, int likecount, String avatarURI, String thumbnailURI) {
        this.id = _id;
        this.feedURL = feedURL;
        this.nickname = nickname;
        this.description = description;
        this.likecount = likecount;
        this.avatarURI = avatarURI;
        this.thumbnailURI = thumbnailURI;
    }

    @NotNull
    @Override
    public String toString() {
        return "VideoItem{" +
                "_id='" + id + "' \n" +
                ", feedURL='" + feedURL + "' \n" +
                ", nickname='" + nickname + "' \n" +
                ", description='" + description + "' \n" +
                ", likecount=" + likecount +
                ", avatarURI='" + avatarURI + "' \n" +
                ", thumbnailURI='" + thumbnailURI + "' \n" +
                '}';
    }
}

