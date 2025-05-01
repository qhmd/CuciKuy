package com.example.cucikuy.Kontak;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class KontakItem implements Parcelable {
    private int iconKontak;
    private String nama;
    private String noTelpon;
    private String alamat;

    public KontakItem(int iconKontak, String nama, String noTelpon, String alamat) {
        this.iconKontak = iconKontak;
        this.nama = nama;
        this.noTelpon = noTelpon;
        this.alamat = alamat;
    }

    protected KontakItem(Parcel in) {
        iconKontak = in.readInt();
        nama = in.readString();
        noTelpon = in.readString();
        alamat = in.readString();
    }

    public static final Creator<KontakItem> CREATOR = new Creator<KontakItem>() {
        @Override
        public KontakItem createFromParcel(Parcel in) {
            return new KontakItem(in);
        }

        @Override
        public KontakItem[] newArray(int size) {
            return new KontakItem[size];
        }
    };

    public int getIconKontak() {
        return iconKontak;
    }

    public String getNama() {
        return nama;
    }

    public String getNoTelpon() {
        return noTelpon;
    }
    public String getAlamat() {return alamat;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(iconKontak);
        dest.writeString(nama);
        dest.writeString(noTelpon);
        dest.writeString(alamat);
    }
}
