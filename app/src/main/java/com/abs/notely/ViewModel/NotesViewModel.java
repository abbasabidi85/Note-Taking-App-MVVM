package com.abs.notely.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abs.notely.Model.Notes;
import com.abs.notely.Repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    public NotesRepository notesRepository;
    public LiveData<List<Notes>> getAllNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);

        notesRepository = new NotesRepository(application);
        getAllNotes = notesRepository.getAllNotes;

    }

    public void insertNotes(Notes notes){
        notesRepository.insertNote(notes);
    }

    public void updateNotes(Notes notes){
        notesRepository.updateNote(notes);
    }

    public void deleteNotes(int id){
        notesRepository.deleteNote(id);
    }

}
