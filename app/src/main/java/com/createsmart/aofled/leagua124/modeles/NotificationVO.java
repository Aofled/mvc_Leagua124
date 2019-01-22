package com.createsmart.aofled.leagua124.modeles;

public class NotificationVO {
    private String title; //заголовок
    private String message; //сообщение
    private String iconUrl; //URL-картинки
    private String action; //Действие
    private String actionDestination; //URL перехода или открытие Activity
    private String actionParameter;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDestination() {
        return actionDestination;
    }
    public void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }

    public String getActionParameter() {
        return actionParameter;
    }
    public void setActionParameter(String actionParameter) {
        this.actionParameter = actionParameter;
    }





}