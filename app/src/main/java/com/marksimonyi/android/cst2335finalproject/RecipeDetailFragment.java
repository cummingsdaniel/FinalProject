package com.marksimonyi.android.cst2335finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
     * @return
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

        //show the message position
        //TextView position = result.findViewById(R.id.tvFrgDirection);
        //position.setText(dataFromActivity.getString(ChatRoomActivity.MESSAGE_POSITION));

        //show the id:
        TextView idView = result.findViewById(R.id.recTxtFrgViewId);
        idView.setText(""+dataFromActivity.getLong(RecipeActivity.ID));

        // get the delete button, and add a click listener:
        Button deleteButton = result.findViewById(R.id.recBtnFrgDelete);
        deleteButton.setOnClickListener( clk -> {

            /*
            if(isTablet) { //both the list and details are on the screen:
                RecipeActivity parent = (RecipeActivity) getActivity();
                parent.deleteMessageId((int)id); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent back = new Intent();
                back.putExtra(ChatRoomActivity.MESSAGE_LIST_ID, id);

                parent.setResult(Activity.RESULT_OK, back); //send data back in onActivityResult()
                parent.finish(); //go back
            }

             */
        });
        return result;
    }

}
