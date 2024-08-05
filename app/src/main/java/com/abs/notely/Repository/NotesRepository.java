package com.abs.notely.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.abs.notely.Dao.NotesDAO;
import com.abs.notely.Database.NotesDatabase;
import com.abs.notely.Model.Notes;
import com.abs.notely.Model.NotesFTS;

import java.util.List;

public class NotesRepository {

    public NotesDAO notesDAO;

    public LiveData<List<Notes>> getAllNotes;

    public NotesRepository(Application application){
        NotesDatabase notesDatabase=NotesDatabase.getDatabaseInstance(application);
        notesDAO=notesDatabase.notesDAO();
        getAllNotes= notesDAO.getAllNotes();
    }

    public void insertNote(Notes notes){
        notesDAO.insertNotes(notes);

    }

    public void deleteNote(int id){
        notesDAO.deleteNotes(id);
    }

    public void updateNote(Notes notes){
        notesDAO.updateNotes(notes);
    }
    public LiveData<List<Notes>> searchNote(String query){
        return notesDAO.searchNotes(query);
    }
}
