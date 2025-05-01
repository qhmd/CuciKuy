package com.example.cucikuy.Durasi;


import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;


public class DurasiItem implements Parcelable {
    private String nameDurasi;
    private String jamDurasi;
    private int editDurasi;
    private int deleteDurasi;

    public DurasiItem(String nameDurasi, String jamDurasi, int editDurasi, int deleteDurasi) {
        this.nameDurasi = nameDurasi;
        this.jamDurasi = jamDurasi;
        this.editDurasi = editDurasi;
        this.deleteDurasi = deleteDurasi;
    }

    protected DurasiItem(Parcel in) {
        nameDurasi = in.readString();
        jamDurasi = in.readString();
        editDurasi = in.readInt();
        deleteDurasi = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DurasiItem> CREATOR = new Creator<DurasiItem>() {
        @Override
        public DurasiItem createFromParcel(Parcel in) {
            return new DurasiItem(in);
        }

        @Override
        public DurasiItem[] newArray(int size) {
            return new DurasiItem[size];
        }
    };

    // Public getter dan setter
    public String getNameDurasi() {
        return nameDurasi;
    }

    public void setNameDurasi(String nameDurasi) {
        this.nameDurasi = nameDurasi;
    }

    public String getJamDurasi() {
        return jamDurasi;
    }

    public void setJamDurasi(String jamDurasi) {
        this.jamDurasi = jamDurasi;
    }


    public int getEditDurasi() {
        return editDurasi;
    }

    public void setEditDurasi(int editDurasi) {
        this.editDurasi = editDurasi;
    }

    public int getDeleteDurasi() {
        return deleteDurasi;
    }

    public void setDeleteDurasi(int deleteDurasi) {
        this.deleteDurasi = deleteDurasi;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nameDurasi);
        dest.writeString(jamDurasi);
        dest.writeInt(editDurasi);
        dest.writeInt(deleteDurasi);
    }

}
