package com.abs.notely.View;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abs.notely.Adapter.NotesAdapter;
import com.abs.notely.Model.Notes;
import com.abs.notely.R;
import com.abs.notely.ViewModel.NotesViewModel;
import com.abs.notely.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private FragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            if (isDarkModeEnabled()) { // check if dark mode is enabled
                actionBar.setLogo(R.drawable.notely_logo_dark);
            } else {
                actionBar.setLogo(R.drawable.notely_logo_light);
            }
        }

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addNoteFragment);
            }
        });


        notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);
        recyclerView=binding.recyclerView;
        notesViewModel.getAllNotes.observe(getViewLifecycleOwner(), notes -> {
            staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            notesAdapter=new NotesAdapter(this.getContext(), notes, this);

            if (notesAdapter.getItemCount() == 0){
                binding.noNotes.setVisibility(View.VISIBLE);
                binding.noNotesImg.setVisibility(View.VISIBLE);
            }else {
                binding.noNotes.setVisibility(View.GONE);
                binding.noNotesImg.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(notesAdapter);
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    public boolean isDarkModeEnabled() {
        int nightModeFlags = getContext().getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onNoteClick(Notes note) {
        Bundle bundle= new Bundle();
        bundle.putInt("id",note.getId());
        bundle.putString("title",note.getNoteTitle());
        bundle.putString("content",note.getNoteContent());
        bundle.putString("dateTime",note.getNoteDate());
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_editNotes,bundle);
    }
}