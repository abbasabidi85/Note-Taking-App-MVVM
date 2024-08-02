package com.abs.notely.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abs.notely.Dao.NotesDAO;
import com.abs.notely.Model.Notes;

@Database(entities = {Notes.class},version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDAO notesDAO();


    public static NotesDatabase INSTANCE;

    public static NotesDatabase getDatabaseInstance(Context context){
        if (INSTANCE==null){
            synchronized (NotesDatabase.class){
                INSTANCE= Room.databaseBuilder(
                                context.getApplicationContext(),
                                NotesDatabase.class,
                                "notesDatabase")
                        .build();
            }

        }
        return INSTANCE;
    }

}
