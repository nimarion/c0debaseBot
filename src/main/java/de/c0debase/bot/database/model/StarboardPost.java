package de.c0debase.bot.database.model;

public class StarboardPost {

    private String guildId;
    private String messageId;
    private String channelId;
    private String userId;
    private String starboardMessageId;
    private Integer starCount;

    /**
     * @param guildId
     * @param messageId
     * @param channelId
     * @param userId
     * @param starboardMessageId
     * @param starCount
     */
    public StarboardPost(String guildId, String messageId, String channelId, String userId, Integer starCount) {
        this.guildId = guildId;
        this.messageId = messageId;
        this.channelId = channelId;
        this.userId = userId;
        this.starCount = starCount;
    }

    /**
     * @return the guildId
     */
    public String getGuildId() {
        return guildId;
    }

    /**
     * @param guildId the guildId to set
     */
    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the channelId
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the starboardMessageId
     */
    public String getStarboardMessageId() {
        return starboardMessageId;
    }

    /**
     * @param starboardMessageId the starboardMessageId to set
     */
    public void setStarboardMessageId(String starboardMessageId) {
        this.starboardMessageId = starboardMessageId;
    }

    /**
     * @return the starCount
     */
    public Integer getStarCount() {
        return starCount;
    }

    /**
     * @param starCount the starCount to set
     */
    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }

}