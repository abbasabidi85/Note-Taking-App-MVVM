package com.abs.notely.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abs.notely.Model.Notes;

import java.util.List;

@Dao
public interface NotesDAO {

    @Query("SELECT* FROM notesDatabase" )
    LiveData<List<Notes>> getAllNotes();

    @Insert
    void insertNotes(Notes... notes);

    @Query("DELETE FROM notesDatabase WHERE id=:id")
    void deleteNotes(int id);

    @Update
    void updateNotes(Notes... notes);

}
