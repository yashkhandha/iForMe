package edu.monash.iforme;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple Game Fragment class
 */
public class GameFragment extends Fragment {

    //Capture view
    View view;
    //UI references
    Button startGameButton;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * on create method to load layout on startup
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Play PacMan");

        //Get reference from UI for button
        startGameButton = view.findViewById(R.id.startGameButton);
        //start game activity on click of button
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),GameActivity.class));
            }
        });

        return view;
    }
}
