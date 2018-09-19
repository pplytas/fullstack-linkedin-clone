package server.endpoints.outputmodels;

import java.util.List;

public class ChatListOutputModel {

    private List<ChatOutputModel> chats;
    private LastChatOpenedOutputModel lastOpened;

    public List<ChatOutputModel> getChats() {
        return chats;
    }

    public void setChats(List<ChatOutputModel> chats) {
        this.chats = chats;
    }

    public LastChatOpenedOutputModel getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(LastChatOpenedOutputModel lastOpened) {
        this.lastOpened = lastOpened;
    }
}
