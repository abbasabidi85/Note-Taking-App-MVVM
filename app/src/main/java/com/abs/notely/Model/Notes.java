package com.abs.notely.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Entity(tableName = "notesDatabase")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "noteTitle")
    private String noteTitle;

    @ColumnInfo(name = "noteContent")
    private String noteContent;

    @ColumnInfo(name = "noteDate")
    private String noteDate;

    public Notes(int id, String noteTitle, String noteContent, String noteDate) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }
}

