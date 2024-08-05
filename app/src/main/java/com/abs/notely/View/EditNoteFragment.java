package com.abs.notely.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.abs.notely.Model.Notes;
import com.abs.notely.R;
import com.abs.notely.ViewModel.NotesViewModel;
import com.abs.notely.databinding.FragmentEditNoteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditNoteFragment extends Fragment {
    int id;
    String title, content, date;
    NotesViewModel notesViewModel;
    FragmentEditNoteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentEditNoteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.editNoteToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.editNoteToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.editNoteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        id=getArguments().getInt("id");
        title=getArguments().getString("title");
        content=getArguments().getString("content");
        date=getArguments().getString("dateTime");

        binding.editNoteTitle.append(title);
        binding.editNoteContent.append(content);
        binding.editNoteDateTime.setText(date);

        binding.editNoteTitle.setMovementMethod(LinkMovementMethod.getInstance());
        binding.editNoteContent.setMovementMethod(LinkMovementMethod.getInstance());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String updatedTitle, updatedContent, updatedDateTime;
                updatedTitle=binding.editNoteTitle.getText().toString();
                updatedContent=binding.editNoteContent.getText().toString();
                updatedDateTime=getDate();

                if (updatedTitle.isBlank() && updatedContent.isBlank()){
                    Snackbar.make(getView(),"Empty note discarded",Snackbar.LENGTH_SHORT).show();
                }else if(updatedTitle.equals(title) && updatedContent.equals(content)){

                }else {
                    Notes notes= new Notes(id,updatedTitle,updatedContent,updatedDateTime);
                    notesViewModel.updateNotes(notes);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int res_id= item.getItemId();

        if (res_id==R.id.action_delete){
            deleteNote(id);
        }
        return true;
    }

    private void deleteNote(int id) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Delete note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notesViewModel.deleteNotes(id);
                        Snackbar.make(getView(), "Note deleted", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_editNotes_to_homeFragment);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.create();
        builder.show();
    }
}