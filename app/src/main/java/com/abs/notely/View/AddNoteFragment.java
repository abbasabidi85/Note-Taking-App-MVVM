package com.abs.notely.View;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.abs.notely.Model.Notes;
import com.abs.notely.R;
import com.abs.notely.ViewModel.NotesViewModel;
import com.abs.notely.databinding.FragmentAddNoteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddNoteFragment extends Fragment {

    String title, content, date;
    FragmentAddNoteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddNoteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NotesViewModel notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.addNoteToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.addNoteToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.addNoteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.addNoteDateTime.setText(getDate());
        binding.addNoteContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.addNoteContent, InputMethodManager.SHOW_IMPLICIT);

        binding.addNoteTitle.setMovementMethod(LinkMovementMethod.getInstance());
        binding.addNoteContent.setMovementMethod(LinkMovementMethod.getInstance());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                title=binding.addNoteTitle.getText().toString();
                content=binding.addNoteContent.getText().toString();
                date=getDate();

                if (title.isEmpty() && content.isEmpty()){
                    Snackbar.make(getView(),"Empty note discarded",Snackbar.LENGTH_SHORT).show();
                }else{
                    int id=0;
                    Notes notes= new Notes(id,title,content,date);
                    notesViewModel.insertNotes(notes);
                }

                setEnabled(false); // Disable the callback after handling
                requireActivity().onBackPressed(); // Call the activity's back pressed method
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    public boolean isDarkModeEnabled() {
        int nightModeFlags = getActivity().getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    public String getDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");
            date= now.format(formatter);
        }
        return date;
    }
}