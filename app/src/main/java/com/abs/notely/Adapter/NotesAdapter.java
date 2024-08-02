package com.abs.notely.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.notely.Model.Notes;
import com.abs.notely.databinding.NotesLayoutBinding;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context context;
    List<Notes> notesList;
    OnNoteClickListener onNoteClickListener;

    public NotesAdapter(Context context, List<Notes> notesList, OnNoteClickListener onNoteClickListener) {
        this.context = context;
        this.notesList=notesList;
        this.onNoteClickListener=onNoteClickListener;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotesLayoutBinding binding=NotesLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NotesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes notes= notesList.get(position);

        if (notes.getNoteTitle().isEmpty()) {
            holder.noteTitle.setVisibility(View.GONE);
            holder.noteContent.setText(notes.getNoteContent());
        } else if (notes.getNoteContent().isEmpty()) {
            holder.noteContent.setVisibility(View.GONE);
            holder.noteTitle.setText(notes.getNoteTitle());
        }else {
            holder.noteTitle.setText(notes.getNoteTitle());
            holder.noteContent.setText(notes.getNoteContent());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNoteClickListener.onNoteClick(notes);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder{

        private TextView noteTitle, noteContent;
        public NotesViewHolder(@NonNull NotesLayoutBinding itemBinding) {
            super(itemBinding.getRoot());

            noteTitle=itemBinding.noteTitle;
            noteContent=itemBinding.noteContent;


        }
    }

    public interface OnNoteClickListener{
        void onNoteClick(Notes note);
    }
}
