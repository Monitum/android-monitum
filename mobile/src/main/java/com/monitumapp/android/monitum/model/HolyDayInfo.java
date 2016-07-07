package com.monitumapp.android.monitum.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.monitumapp.android.monitum.R;
import com.monitumapp.android.monitum.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Represents fasting, rosary info.
 */
public class HolyDayInfo {

    private static final String TAG = HolyDayInfo.class.getSimpleName();
    @SerializedName("Date")
    private Date date;

    // Not used
    @SerializedName("Season")
    private String season;

    @SerializedName("HDO")
    private boolean hdo;

    @SerializedName("HDO_Name")
    private String hdoName;

    @SerializedName("SDP")
    private boolean sdp;

    @SerializedName("SDP_Name")
    private String sdpName;

    public int getHdoImageId() {
        return isHdo() ? R.drawable.ic_hdo_red : R.drawable.ic_hdo_grey;
    }

    public int getSdpImageId() {
        return isSdp() ? R.drawable.ic_sdp_red : R.drawable.ic_sdp_grey;
    }

    public enum DietaryRestrictionImageType { Fast, Fasting, Abstinance, None }

    @SerializedName("Dietary_Restrictions")
    private String dietaryRestrictions;

    private String dietaryRestrictionsName;

    public enum DaySquareImageColor {
        Square_White,
        Square_Green_Red,
        Square_Green_White,
        Square_White_Red,
        Square_Green,
        Square_Red,
        Square_Purple,
        Square_Violet,
        Square_Violet_White }
    @SerializedName("Color1")
    private String color1;

    //@SerializedName("Color2")
    //private String color2;

    private RosaryDetails rosaryDetails;

    public void parseJson(JSONObject jsonObj) {
        if (jsonObj == null) return;
        try {
            String dateStr = jsonObj.getString("date");
            date = DateUtils.parseDate(dateStr);
            season = jsonObj.optString("season");

            hdo = jsonObj.optBoolean("holyDayOfObligation");
            hdoName = jsonObj.optString("hdoreason");

            sdp = jsonObj.optBoolean("specialDayOfPrayer");
            sdpName = jsonObj.optString("sdpreason");

            dietaryRestrictions = jsonObj.optString("dietaryRestrictions");
            dietaryRestrictionsName = jsonObj.optString("dietaryreason");

            color1 = jsonObj.optString("color");
            //color2 = jsonObj.optString("color2");
            Log.d(TAG, "color1 = " + color1);
            rosaryDetails = new RosaryDetails();
            rosaryDetails.parseJson(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isToday() {
        return (date != null && DateUtils.isToday(date));
    }

    public Date getDate() {
        return date;
    }

    public String getSeason() {
        return season;
    }

    public boolean isHdo() {
        return hdo;
    }

    public String getHdoName() {
        return hdoName;
    }

    public boolean isSdp() {
        return sdp;
    }

    public String getSdpName() {
        return sdpName;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public String getColor1() {
        return color1;
    }

//    public String getColor2() {
//        return color2;
//    }


    public String getDietaryRestrictionsName() {
        return dietaryRestrictionsName;
    }

    public int getDietaryRestrictionImageId() {
        int imageResId = R.drawable.ic_fish;
        if (DietaryRestrictionImageType.Fast.toString().equals(dietaryRestrictions)) {
            imageResId = R.drawable.ic_fast;
        } else if (DietaryRestrictionImageType.Fasting.toString().equals(dietaryRestrictions)) {
            imageResId = R.drawable.ic_fast;
        } else if (DietaryRestrictionImageType.Abstinance.toString().equals(dietaryRestrictions)) {
            imageResId = R.drawable.ic_abstinence;
        }
        return imageResId;
    }

    public RosaryDetails getRosaryDetails() {
        return rosaryDetails;
    }

    public int getDaySquareImageId() {
        // FIXME add logic here
        int colorSquareDrawable = R.drawable.ic_square_violet_white;
        if (DaySquareImageColor.Square_Green.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_green;
        } else if (DaySquareImageColor.Square_White.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_white;
        } else if (DaySquareImageColor.Square_Green_Red.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_green_red;
        } else if (DaySquareImageColor.Square_Green_White.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_green_white;
        } else if (DaySquareImageColor.Square_White_Red.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_white_red;
        } else if (DaySquareImageColor.Square_Red.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_red;
        } else if (DaySquareImageColor.Square_Purple.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_rose;
        } else if (DaySquareImageColor.Square_Violet.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_violet;
        } else if (DaySquareImageColor.Square_Violet_White.toString().equals(color1)) {
            colorSquareDrawable = R.drawable.ic_square_violet_white;
        }
        return colorSquareDrawable;
    }

}
