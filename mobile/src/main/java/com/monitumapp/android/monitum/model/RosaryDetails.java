package com.monitumapp.android.monitum.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Rosary Details.
 */
public class RosaryDetails {

    public void parseJson(JSONObject jsonObj) {
        if (jsonObj == null) return;
        nonTraditionalSeason = jsonObj.optString("nontraditionalseason");
        nonTraditionalNonSeason = jsonObj.optString("nontraditionalnonseason");
        traditionalSeason = jsonObj.optString("traditionalseason");
        traditionalNonSeason = jsonObj.optString("traditionalnonseason");
    }

    public String getCode(SettingsInfo settingsInfo) {
        boolean luminous;
        boolean timeCycle;
        if (settingsInfo == null) {
            return "-";
        } else {
            luminous = settingsInfo.luminous;
            timeCycle = settingsInfo.timeCycle;
        }

        String code;
        if (!luminous) {
            if (!timeCycle) {
                code = traditionalSeason;
            } else {
                code = traditionalNonSeason;
            }
        } else {
            if (!timeCycle) {
                code = nonTraditionalSeason;
            } else {
                code = nonTraditionalNonSeason;
            }
        }
        return TextUtils.isEmpty(code) ? "-" : code.substring(0, 1);
    }

    public String getReason(SettingsInfo settingsInfo) {
        boolean luminous;
        boolean timeCycle;
        if (settingsInfo == null) {
            return "-";
        } else {
            luminous = settingsInfo.luminous;
            timeCycle = settingsInfo.timeCycle;
        }

        String reason;
        if (!luminous) {
            if (!timeCycle) {
                reason = traditionalSeason;
            } else {
                reason = traditionalNonSeason;
            }
        } else {
            if (!timeCycle) {
                reason = nonTraditionalSeason;
            } else {
                reason = nonTraditionalNonSeason;
            }
        }
        return reason;

    }

    private enum RosaryInfo { Glorius, Joyful, Luminous, Sorrowful }

    @SerializedName("NonTraditionalSeason")
    private String nonTraditionalSeason;

    @SerializedName("NonTraditionalNonSeason")
    private String nonTraditionalNonSeason;

    @SerializedName("TraditionalSeason")
    private String traditionalSeason;

    @SerializedName("TraditionalNonSeason")
    private String traditionalNonSeason;

    public String getNonTraditionalSeason() {
        return nonTraditionalSeason;
    }

    public String getNonTraditionalNonSeason() {
        return nonTraditionalNonSeason;
    }

    public String getTraditionalSeason() {
        return traditionalSeason;
    }

    public String getTraditionalNonSeason() {
        return traditionalNonSeason;
    }
}
