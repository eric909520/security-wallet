package org.secwallet.core.util.scriptEngine;

public enum RoundingEnum {
    ceil("Rounded up"),
    floor("round down"),
    round_down("never increment the value of the number"),
    round_up("never decrease the value of a number"),
    round_ceiling("round towards positive infinity"),
    round_floor("round towards negative infinity"),
    round_half_up("Standard rounding"),
    round_half_down("round to the \"nearest\" number"),
    round_half_even("Banker's Algorithm");



    private String text;

    public String getText(){
        return text;
    }

    RoundingEnum(String text){
        this.text=text;
    }
}
