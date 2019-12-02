package com.marksimonyi.android.cst2335finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import androidx.fragment.app.Fragment;

public class RecipeDetailFragment extends Fragment {
    /**
     * tracks whether the fragment is running on a tablet or not
     */
    private boolean isTablet;
    /**
     * the data passed to the fragment from the activity
     */
    private Bundle dataFromActivity;
    /**
     * the id of the element being displayed by the fragment
     */
    private long id;

    /**
     * sets whether the fragment is running on a tablet or not
     * @param tablet
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     * onCreateView inflates the layout, sets the values of the fields to display the selected recipe
     * and sets the onclick for the delete button.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the inflated layout as a View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(RecipeActivity.ID);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.view_recipe_fragment, container, false);

        //show the title
        TextView title = result.findViewById(R.id.recTxtFrgViewTitle);
        title.setText(dataFromActivity.getString(RecipeActivity.TITLE));

        //show the URL
        TextView url = result.findViewById(R.id.recTxtFrgViewUrl);
        String txt = dataFromActivity.getString(RecipeActivity.URL);
        url.setText( txt );
        Linkify.addLinks(url, Linkify.WEB_URLS);

        //show the id:
        TextView idView = result.findViewById(R.id.recTxtFrgViewId);
        idView.setText(""+dataFromActivity.getLong(RecipeActivity.ID));

        //show the Image:
        ImageView image = result.findViewById(R.id.recImgFrgImage);
        String imageUrl = dataFromActivity.getString(RecipeActivity.IMAGE);
        String imgUrl = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        FileInputStream fis = null;
                            try {    fis = getActivity().openFileInput(imgUrl);   }
                            catch (FileNotFoundException e) {    e.printStackTrace();  }
                            Bitmap bm = BitmapFactory.decodeStream(fis);
        image.setImageBitmap(bm);
        //image.setImageURI(Uri.parse(dataFromActivity.getString(RecipeActivity.IMAGE)));
        //image.setImageURI(Uri.parse("https://images.media-allrecipes.com/userphotos/720x405/3359675.jpg"));


        // get the delete button, and add a click listener:
        Button deleteButton = result.findViewById(R.id.recBtnFrgDelete);
        deleteButton.setOnClickListener( clk -> {


            if(isTablet) { //both the list and details are on the screen:
                RecipeActivity parent = (RecipeActivity) getActivity();
                parent.deleteRecipeId((int)id); //this deletes the item and updates the list

                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                ViewRecipe parent = (ViewRecipe) getActivity();
                Intent back = new Intent();
                back.putExtra(RecipeActivity.ID, id);

                parent.setResult(Activity.RESULT_OK, back); //send data back in onActivityResult()
                parent.finish(); //go back
            }

        });
        Button addButton = result.findViewById(R.id.recBtnFrgAdd);
        addButton.setOnClickListener( clk -> {
            if(isTablet) { //both the list and details are on the screen:
                RecipeActivity parent = (RecipeActivity) getActivity();
                parent.addFavRecipe(new Recipe(dataFromActivity.getString(RecipeActivity.TITLE), dataFromActivity.getString(RecipeActivity.IMAGE), dataFromActivity.getString(RecipeActivity.URL)));

                deleteButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                ViewRecipe parent = (ViewRecipe) getActivity();
                Intent back = new Intent();
                back.putExtra(RecipeActivity.ID, id);
                back.putExtra("add", true);

                parent.setResult(Activity.RESULT_OK, back); //send data back in onActivityResult()
                parent.finish(); //go back
            }
        });

        if (dataFromActivity.getBoolean(RecipeActivity.FAV)) {
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.VISIBLE);
        }
        return result;
    }

}
