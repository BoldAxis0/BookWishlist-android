package com.example.sdandeka_mybookwishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sdandeka_mybookwishlist.data.Book;
import com.example.sdandeka_mybookwishlist.databinding.ActivityDetailsBinding;

import java.util.ArrayList;



public class DetailsActivity extends AppCompatActivity {

    //declare binding object
    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize binding object
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //set up genre selection spinner
        Spinner spinner  = binding.genreSpinner;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bookGenres());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //get the book object and position from the intent
        Book book = (Book) getIntent().getSerializableExtra("book");
        int position = getIntent().getIntExtra("position", -1);

        //check if the book object is null
        if (book != null) {
            //if not, that means an existing book is being edited
            // set all attributes of the passed book object to the views
            binding.editTitle.setText(book.getTitle());
            binding.editAuthor.setText(book.getAuthor());
            spinner.setSelection(adapter.getPosition(book.getGenre()));
            binding.editYear.setText(String.valueOf(book.getYear()));
            binding.readornotCheckbox.setChecked(book.getIsRead());
        }

        //set up the save button
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked(spinner, position);
            }
        });
    }


    private void saveButtonClicked(Spinner spinner, int position) {
        if (Integer.parseInt(binding.editYear.getText().toString()) > 2024) {
            //toast to notify user that year is invalid
            Toast.makeText(DetailsActivity.this, "Publishing Year can't be in the future!", Toast.LENGTH_LONG).show();
        }
        else if(Integer.parseInt(binding.editYear.getText().toString()) < 0){
            //toast to notify user that year is invalid
            Toast.makeText(DetailsActivity.this, "Publishing Year can't be negative!", Toast.LENGTH_LONG).show();
        }

        //right now if negative book year is entered it breaks the app
        //TODO: handle negative book years to signify before christ books
        //also if any book before 1000 is entered, the int parser converts that to a 3 digit number
        // and so if you go to edit that book, you will have to append additional zeroes
        //to fulfil 4 digit requirement

        //the assignment page doesn't specify any minimum constrains for
        //any other fields, so not enforcing that. just max chars
        //which is handled by xml.

        else {
            //all correct inputs
            //make new book object based on current inputs
            Book updatedBook = new Book(
                    binding.editTitle.getText().toString(),
                    binding.editAuthor.getText().toString(),
                    spinner.getSelectedItem().toString(),
                Integer.parseInt(binding.editYear.getText().toString()),
//                            2024,
                    binding.readornotCheckbox.isChecked()
            );

            // Attach the updated Book object and position as extras to the Intent
            //and send back to MainActivity
            getIntent().putExtra("book", updatedBook);
            getIntent().putExtra("position", position);
            setResult(Activity.RESULT_OK, getIntent());

            finish();
        }
    }

    //a list of genres for the spinner
    private ArrayList<String> bookGenres(){
        ArrayList<String> mainHeaders = new ArrayList<>();

        // Adding main headers to the ArrayList
        mainHeaders.add("Fiction");
        mainHeaders.add("Non-Fiction");
        mainHeaders.add("Children's and Young Adult");
        mainHeaders.add("Poetry");
        mainHeaders.add("Drama/Play");
        mainHeaders.add("Science Fiction and Fantasy");
        mainHeaders.add("Mystery/Thriller");
        mainHeaders.add("Romance");
        mainHeaders.add("Historical Fiction");
        mainHeaders.add("Horror");
        mainHeaders.add("Humor/Satire");
        mainHeaders.add("Adventure");
        mainHeaders.add("Biography/Autobiography");
        mainHeaders.add("Business/Finance");
        mainHeaders.add("Religion/Spirituality");
        mainHeaders.add("Cookbooks/Food");
        mainHeaders.add("Graphic Novels/Comics");
        mainHeaders.add("Dystopian");
        mainHeaders.add("Self-Help");

        return mainHeaders;
    }
}