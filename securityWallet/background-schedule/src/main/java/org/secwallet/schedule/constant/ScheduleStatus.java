package org.secwallet.schedule.constant;

/**
 * Scheduled task operation status
 * @date 2019-11-15
 */
public enum ScheduleStatus {
    START(1, "start"),
    PAUSE(2, "pause"),
    DELETE(3, "del");

    private final Integer value;
    private final String desc;

    ScheduleStatus(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getEnumName() {
        return name();
    }
}
