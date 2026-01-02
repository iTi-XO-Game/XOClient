package com.mycompany.clientside.serverhandler.message;
/*
should has
1) enum has messageType
2) enum has actionType
*/



public class Header
{
    public enum MessageType {
        REQUEST,
        RESPONSE,
        ERROR,
        EVENT
    }

    public enum ActionType {
        MOVE,
        LOGIN
    }


    public MessageType messageType;
    public ActionType actionType;
    // now we have to create Constrctor and sitters , gitters


    public Header() {

    }

    public Header(MessageType messageType, ActionType actionType) {
        this.messageType = messageType;
        this.actionType = actionType;
    }

    // ===== Getters & Setters =====
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

}
