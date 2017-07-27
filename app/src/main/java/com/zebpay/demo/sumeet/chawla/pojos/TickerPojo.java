package com.zebpay.demo.sumeet.chawla.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sumeet on 26-07-2017.
 */

public class TickerPojo implements Parcelable {
    public String market;
    public String sell;
    public String buy;
    public String volume;
    public String currency;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.market);
        dest.writeString(this.sell);
        dest.writeString(this.buy);
        dest.writeString(this.volume);
        dest.writeString(this.currency);
    }

    public TickerPojo() {
    }

    protected TickerPojo(Parcel in) {
        this.market = in.readString();
        this.sell = in.readString();
        this.buy = in.readString();
        this.volume = in.readString();
        this.currency = in.readString();
    }

    public static final Parcelable.Creator<TickerPojo> CREATOR = new Parcelable.Creator<TickerPojo>() {
        @Override
        public TickerPojo createFromParcel(Parcel source) {
            return new TickerPojo(source);
        }

        @Override
        public TickerPojo[] newArray(int size) {
            return new TickerPojo[size];
        }
    };
}
