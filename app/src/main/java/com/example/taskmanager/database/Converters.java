package com.example.taskmanager.database;

import androidx.room.TypeConverter;

import com.example.taskmanager.model.State;

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



}
