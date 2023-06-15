package hu.rivalsnetwork.rivalsapi.nms;

import hu.rivalsnetwork.rivalsapi.users.User;

public interface PacketEntity {

    void spawn(User user);

    void startTicking(int tickDuration);

    void onClick(ClickAction action);

    interface ClickAction {
        void accept(User user);
    }
}
