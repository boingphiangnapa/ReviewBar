package th.ac.kku.cis.lab.reviewbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class HomePage extends Fragment {
    View view;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton signout, post_btn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_page,container,false);
        materialDesignFAM = (FloatingActionMenu) view.findViewById
                (R.id.material_design_android_floating_action_menu);
        signout = (FloatingActionButton) view.findViewById(R.id.btn_signout);
        post_btn = (FloatingActionButton) view.findViewById(R.id.btn_add_post);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_store);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                3));


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        materialDesignFAM.setVisibility(View.INVISIBLE);
        if(user != null){
            materialDesignFAM.setVisibility(View.VISIBLE);
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());

                // set title
                alertDialogBuilder.setTitle("Logout ?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("SignOut")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mAuth.signOut();
                                Intent intent = new Intent(getActivity(),LoginPage.class);
                                startActivity(intent);
                                Toast.makeText(getActivity(),"Sign Out already !!",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No",null);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PostPage.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference("Bar");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Bar> options = new FirebaseRecyclerOptions.Builder<Bar>()
                .setQuery(mData,Bar.class)
                .build();
        FirebaseRecyclerAdapter<Bar,Card_View> adapter = new FirebaseRecyclerAdapter<Bar, Card_View>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Card_View holder, int position, @NonNull final Bar model) {
                final String post_key = getRef(position).getKey();
                holder.setName(model.getName());
                holder.setImage(getActivity(),model.getImageUrl());

                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(user == null) {
                               Intent intent = new Intent(getActivity(), DetailPage.class);
                               intent.putExtra("PostKey", post_key);
                               intent.putExtra("NameBar", model.getName());
                               startActivity(intent);

                           }else if(user != null){
                               PopupMenu popupMenu = new PopupMenu(getActivity(),holder.imageView);
                               popupMenu.getMenuInflater().inflate(R.menu.popup_card_store,popupMenu.getMenu());
                               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                   @Override
                                   public boolean onMenuItemClick(MenuItem item) {
                                       switch (item.getItemId()){
                                           case R.id.View:
                                               Intent intent = new Intent(getActivity(), DetailPage.class);
                                               intent.putExtra("PostKey", post_key);
                                               intent.putExtra("NameBar", model.getName());
                                               startActivity(intent);
                                               break;
                                           case R.id.Edit:
                                               Intent intent1 = new Intent(getActivity(), EditPage.class);
                                               intent1.putExtra("PostKey", post_key);
                                               startActivity(intent1);
                                               break;
                                           case R.id.delete:
                                               FirebaseDatabase.getInstance().getReference("Bar").child(post_key).removeValue();
                                               break;
                                       }
                                       return true;
                                   }
                               });
                               popupMenu.show();
                           }
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
        public void setImage(Context context,String image){
            Picasso.with(context).load(image).into(imageView);
        }
    }

}
