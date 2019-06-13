package com.cgy.colorfulnews.event;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 13:50
 */
public class ChannelItemMoveEvent {
    private int fromPosition;
    private int toPosition;

    public ChannelItemMoveEvent(int fromPosition, int toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }
}
