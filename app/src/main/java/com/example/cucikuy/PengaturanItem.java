package com.example.cucikuy;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PengaturanItem implements Parcelable {
        private int iconResId;
        private String judul;
        private String deskripsi;

        public PengaturanItem(int iconResId, String judul, String deskripsi) {
            this.iconResId = iconResId;
            this.judul = judul;
            this.deskripsi = deskripsi;
        }

    protected PengaturanItem(Parcel in) {
        iconResId = in.readInt();
        judul = in.readString();
        deskripsi = in.readString();
    }

    public static final Creator<PengaturanItem> CREATOR = new Creator<PengaturanItem>() {
        @Override
        public PengaturanItem createFromParcel(Parcel in) {
            return new PengaturanItem(in);
        }

        @Override
        public PengaturanItem[] newArray(int size) {
            return new PengaturanItem[size];
        }
    };

    public int getIconResId() {
            return iconResId;
        }

        public String getJudul() {
            return judul;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(iconResId);
        dest.writeString(judul);
        dest.writeString(deskripsi);
    }
}
