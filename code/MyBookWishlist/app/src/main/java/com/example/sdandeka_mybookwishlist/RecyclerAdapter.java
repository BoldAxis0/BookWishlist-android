package com.example.sdandeka_mybookwishlist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sdandeka_mybookwishlist.data.Book;
import com.example.sdandeka_mybookwishlist.databinding.RecyclerItemBinding;

import java.util.List;

//this is the most confusing class, because recycler view is hella convoluted
//somehow, it works

//adapter class for the recyclerview
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<Book> books;
    private OnBookListener onBookListener;
    //need book removed listener to keep count of books and read books
    private OnBookRemovedListener onBookRemovedListener;

    //implement the constructer and 3 required methods of the abstract class
    public RecyclerAdapter(List<Book> books, OnBookListener onBookListener, OnBookRemovedListener onBookRemovedListener) {
        this.books = books;
        this.onBookListener = onBookListener;
        this.onBookRemovedListener= onBookRemovedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view, onBookListener, onBookRemovedListener );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //fill the views using data from each of the book objects
        Book currentBook = books.get(position);

        holder.title.setText(currentBook.getTitle());
        holder.author.setText(currentBook.getAuthor());
        holder.genre.setText(currentBook.getGenre());
        holder.year.setText(String.valueOf(currentBook.getYear()));
        holder.isRead.setChecked(currentBook.getIsRead());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //idk why viewbinding doesnt work here
//        public RecyclerItemBinding binding;
        public TextView title;
        public TextView author;
        public TextView genre;
        public TextView year;
        public CheckBox isRead;

        //click listener for editing the selected book
        OnBookListener onBookListener;
        //long click listener for deleting the selected book
        OnBookRemovedListener onBookRemovedListener;
        //TODO: implement left/right swiping animation for editing and deleting the book

        public ViewHolder(View itemView, OnBookListener onBookListener, OnBookRemovedListener onBookRemovedListener){
            super(itemView);
            //using findviewbyid instead of viewbinding
            //cos viewbinding throws fatal exception
            title = itemView.findViewById(R.id.title_text);
            author = itemView.findViewById(R.id.author_text);
            genre = itemView.findViewById(R.id.genre_text);
            year = itemView.findViewById(R.id.year_text);
            isRead = itemView.findViewById(R.id.read_checkbox);
            this.onBookListener = onBookListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            this.onBookRemovedListener = onBookRemovedListener;
        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            // Show a dialog to ask user if they want to delete the book
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this book?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User chose "Yes", delete the book from the list
                            int position = getAdapterPosition();
                            books.remove(position);
                            notifyItemRemoved(position);

                            onBookRemovedListener.onBookRemoved(position);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }

    }

    public interface OnBookListener {
        void onBookClick(int position);
    }

    //once again, this listener for keeping track of when books are deleted
    //there must be a more elegant solution for this
    //maybe by putting some kind of observable or livedata on the books arraylist
    //TODO: handle keeping track of book-count in a better way
    public interface OnBookRemovedListener {
        void onBookRemoved(int position);
    }
}
