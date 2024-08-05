package com.abs.notely.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "notesDatabaseFTS")
@Fts4(contentEntity = Notes.class)
public class NotesFTS{

    @ColumnInfo(name = "noteTitle")
    String title;

    @ColumnInfo(name = "noteContent")
    String content;

    public NotesFTS(String content, String title) {
        this.content = content;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
