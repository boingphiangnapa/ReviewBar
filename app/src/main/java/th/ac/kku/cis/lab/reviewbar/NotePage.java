package th.ac.kku.cis.lab.reviewbar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NotePage extends Fragment {

    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mNote;
    RecyclerView recyclerView;
    DatabaseReference mData;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_note_page,container,
                false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mNote = firebaseDatabase.getReference("Note");


        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_note);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                2));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Bar> options = new FirebaseRecyclerOptions.Builder<Bar>()
                .setQuery(mNote,Bar.class)
                .build();
        FirebaseRecyclerAdapter<Bar,Card_View> adapter = new FirebaseRecyclerAdapter<Bar, Card_View>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Card_View holder, int position,
                                            @NonNull final Bar model) {
                final String post_key = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setImage(getActivity(),model.getImageUrl());
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            PopupMenu popupMenu = new PopupMenu(getActivity(),holder.imageView);
                            popupMenu.getMenuInflater().inflate(R.menu.popup_note,popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case R.id.delete_view:
                                            Intent intent = new Intent(getActivity(), DetailPage.class);
                                            intent.putExtra("PostKey", post_key);
                                            intent.putExtra("NameBar", model.getName());
                                            startActivity(intent);
                                            break;
                                        case R.id.delete_card:
                                            FirebaseDatabase.getInstance().getReference("Note").child(post_key).removeValue();
                                            break;
                                    }
                                    return true;
                                }
                            });
                            popupMenu.show();
                        }
                    
                });
            }

            @NonNull
            @Override
            public Card_View onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View Sview = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.card_store,viewGroup,false);
                return new Card_View(Sview);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class Card_View extends RecyclerView.ViewHolder{
        View mView;
        ImageView imageView;
        //CardView cardView;
        public Card_View(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imageView = (ImageView) mView.findViewById(R.id.card_image);

        }
        public void setName(String name){
            TextView nameTe = (TextView) mView.findViewById(R.id.text_name_view);
            nameTe.setText(name);
        }
        public void setImage(Context context, String image){
            Picasso.with(context).load(image).into(imageView);
        }
    }
}
