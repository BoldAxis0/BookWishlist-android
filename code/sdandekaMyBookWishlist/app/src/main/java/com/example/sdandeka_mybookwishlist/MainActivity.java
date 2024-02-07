package com.example.sdandeka_mybookwishlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.sdandeka_mybookwishlist.data.Book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sdandeka_mybookwishlist.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.List;

//pending issue: sometimes when both the title and author is very long,
//the checkbox is not visible in the recycler view

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnBookListener, RecyclerAdapter.OnBookRemovedListener {

    private ActivityMainBinding binding;

    private List<Book> books;

    private int num_books = 0;
    private int read_books = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup viewbinding object
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //some dummy data for recycler view
//        books = getDummyBooks();
        books = new ArrayList<>();
        //set up recycler view
        RecyclerAdapter adapter = new RecyclerAdapter(books, this, this);
        binding.bookList.setAdapter(adapter);
        binding.bookList.setLayoutManager(new LinearLayoutManager(this));


        //start detailsactivity if floating action button is clicked
        binding.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start DetailsActivity without passing any Book object or position
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

                //startActivityForResult is depricated, change to Activity Results API in future
                startActivityForResult(intent, 1);
            }
        });
    }

    // dummy testing data for long list
    @NonNull
    private static List<Book> getDummyBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, true));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, true));
        books.add(new Book("1984", "George Orwell", "Fiction", 1949, true));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, true));
        books.add(new Book("The Grapes of Wrath", "John Steinbeck", "Fiction", 1939, true));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, true));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, true));
        books.add(new Book("1984", "George Orwell", "Fiction", 1949, true));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, true));
        books.add(new Book("The Grapes of Wrath", "John Steinbeck", "Fiction", 1939, true));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, true));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, true));
        books.add(new Book("1984", "George Orwell", "Fiction", 1949, true));
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, true));
        books.add(new Book("The Grapes of Wrath", "John Steinbeck", "Fiction", 1939, true));
        return books;
    }


    @Override
    public void onBookClick(int position) {

        //get the clicked book from the list
        Book clickedBook = books.get(position);

        //create new intent and attach the clicked book and its position
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("book", clickedBook);
        intent.putExtra("position", position);

        // Start DetailsActivity with the ActivityResultLauncher
        //startActivityForResult is depricated however
        //I tried the Activity Results API and it complicated things beyond my understanding
        //TODO for future: use the Activity Results API
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if request valid, retrieve the updated book object and position from the intent

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                // Retrieve the updated Book object and position from the Intent
                Book updatedBook = (Book) data.getSerializableExtra("book");
                int position = data.getIntExtra("position",-1);

                // Update the item in the RecyclerView
                // If the position is not -1, an existing book is updated
                if (position != -1) {
                    if(books.get(position).getIsRead() && !updatedBook.getIsRead() && read_books>0){
                        read_books--;
                    }
                    else if(!books.get(position).getIsRead() && updatedBook.getIsRead()){
                        read_books++;
                    }

                    books.set(position, updatedBook);
                    binding.bookList.getAdapter().notifyItemChanged(position);
                    binding.countText.setText("Books: " + num_books + " Read: " + read_books);

                }
                //otherwise, a new book was added to the end of the list
                else{
                    books.add(updatedBook);
                    binding.bookList.getAdapter().notifyItemInserted(books.size() - 1);

                    num_books++;
                    if (updatedBook.getIsRead()) {
                        read_books++;
                    }
                    binding.countText.setText("Books: " + num_books + " Read: " + read_books);
                }
            }
        }
    }

    @Override
    public void onBookRemoved(int position) {
        //update the number of books and read books
        num_books--;
        if (read_books > 0) {
            read_books--;
        }
        binding.countText.setText("Books: " + num_books + " Read: " + read_books);
    }
}