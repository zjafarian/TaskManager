package com.example.taskmanager.database;

import android.net.Uri;

import androidx.room.TypeConverter;

import com.example.taskmanager.model.State;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class Converters {

    @TypeConverter
    public static Date timestampToDate(long timeStamp) {
        return new Date(timeStamp);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static String uuidToString(UUID uuid){
        return uuid.toString();
    }

    @TypeConverter
    public static UUID stringToUUID (String uuid){
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public static String stateToString (State state){
        return state.toString();
    }

    @TypeConverter
    public static State stringToState(String state){
        return State.valueOf(state);
    }

    @TypeConverter
    public static File stringToFile (String fileName){
        return new File(fileName);
    }

    @TypeConverter
    public static String fileToString (File file){
        return file.toString();
    }

    @TypeConverter
    public static String uriToString (Uri uri){
        return uri.toString();
    }

    @TypeConverter
    public static Uri stringToUri (String uri){
        return Uri.parse(uri);

    }



}
