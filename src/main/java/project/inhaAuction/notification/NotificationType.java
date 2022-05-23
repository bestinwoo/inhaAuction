package project.inhaAuction.notification;

public enum NotificationType {
    SALES("sales"),
    PURCHASE("purchase"),
    MODIFY_STATE("modifyState"),
    BAN("ban"),
    OUT("out");

    String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }
}
