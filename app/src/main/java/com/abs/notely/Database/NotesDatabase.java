package com.abs.notely.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abs.notely.Dao.NotesDAO;
import com.abs.notely.Model.Notes;
import com.abs.notely.Model.NotesFTS;

import org.jetbrains.annotations.Async;

@Database(entities = {Notes.class, NotesFTS.class},version = 2, exportSchema = false)
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
                        .allowMainThreadQueries().build();
            }

        }
        return INSTANCE;
    }

}
