package com.abs.notely.View;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.abs.notely.Adapter.NotesAdapter;
import com.abs.notely.Model.Notes;
import com.abs.notely.R;
import com.abs.notely.ViewModel.NotesViewModel;
import com.abs.notely.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private FragmentHomeBinding binding;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView, searchRecyclerView;
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

        if (isDarkModeEnabled()) { // check if dark mode is enabled
            binding.fabAdd.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.purple_700));
        } else {
            binding.fabAdd.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.purple_500));
        }
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addNoteFragment);
            }
        });

        notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);

        setupRecyclerView();
        setupSearchResults();
        setupBackNavigation();

    }

    private void setupSearchResults() {
        searchRecyclerView=binding.searchRecyclerView;
        binding.searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query=binding.searchView.getText().toString();
                notesViewModel.searchNotes(query).observe(getViewLifecycleOwner(), notes -> {
                    searchRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                    notesAdapter=new NotesAdapter(getContext(), notes, HomeFragment.this);
                    searchRecyclerView.setAdapter(notesAdapter);
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.searchView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (binding.searchView.getEditText().getText().toString().isBlank()){

                }else{
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.searchView.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    private void setupRecyclerView() {
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

    private void setupBackNavigation() {

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.searchView.getEditText().hasFocus()) {
                    binding.searchView.setText(null);
                    binding.searchView.hide();
                } else if(!binding.searchView.getText().toString().isBlank()){
                    binding.searchView.setText(null);
                    binding.searchView.hide();
                }else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        setupBackNavigation();
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