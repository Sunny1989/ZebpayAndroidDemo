package com.zebpay.demo.sumeet.chawla.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sumeet on 26-07-2017.
 */

public class ActivityFeed implements Parcelable {
    public String Name;
    public String RefNumber;
    public String Description;
    public String Time;
    public String AcitvityType;
    public String SourceImageUrl;
    public String PaymentTypeId;
    public String RefGuid;
    public String PaymentTypeGuid;
    public String Title;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.RefNumber);
        dest.writeString(this.Description);
        dest.writeString(this.Time);
        dest.writeString(this.AcitvityType);
        dest.writeString(this.SourceImageUrl);
        dest.writeString(this.PaymentTypeId);
        dest.writeString(this.RefGuid);
        dest.writeString(this.PaymentTypeGuid);
        dest.writeString(this.Title);
    }

    public ActivityFeed() {
    }

    protected ActivityFeed(Parcel in) {
        this.Name = in.readString();
        this.RefNumber = in.readString();
        this.Description = in.readString();
        this.Time = in.readString();
        this.AcitvityType = in.readString();
        this.SourceImageUrl = in.readString();
        this.PaymentTypeId = in.readString();
        this.RefGuid = in.readString();
        this.PaymentTypeGuid = in.readString();
        this.Title = in.readString();
    }

    public static final Creator<ActivityFeed> CREATOR = new Creator<ActivityFeed>() {
        @Override
        public ActivityFeed createFromParcel(Parcel source) {
            return new ActivityFeed(source);
        }

        @Override
        public ActivityFeed[] newArray(int size) {
            return new ActivityFeed[size];
        }
    };
}
