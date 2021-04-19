package ca.on.conec.iplan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Data;

@Entity
@Data
public class BucketList{

    @PrimaryKey(autoGenerate = true)
    public int bucketId;

    @ColumnInfo(name = "bucketTitle")
    public String bucketTitle;

    @ColumnInfo(name = "progressType")
    public String progressType;

    @ColumnInfo(name = "currentStatus")
    public double currentStatus;

    @ColumnInfo(name = "goal")
    public double goal;

    @ColumnInfo(name = "targetAge")
    public int targetAge;

    @ColumnInfo(name = "ORDER")
    public int order;
}
