package edu.monash.iforme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/** Medication adapter class to manage the listView for MedicalFragment class
 * Created by yashkhandha on 13/05/2018.
 */

public class MedicationAdapter extends BaseAdapter implements Filterable {

    private Context mCurrentContext;
    //for storing all medications
    private ArrayList<Medication> mMedicationList = new ArrayList<>();
    //for storing filtered list of medications
    private ArrayList<Medication> mFilteredList;
    private MedicationFilter mFilter;

    //Constructor to initialise fields and lists
    public MedicationAdapter(Context con, ArrayList<Medication> medications){
        mCurrentContext = con;
        mFilteredList = new ArrayList<>(100);
        mMedicationList = medications;
        mFilteredList = mMedicationList;
    }

    /**
     * Method to get the count of filtered list items
     * @return size of list
     */
    @Override
    public int getCount() { return mFilteredList.size(); }

    /**
     * Method to fetch selected item in list
     * @param i
     * @return object
     */
    @Override
    public Object getItem(int i) { return mFilteredList.get(i); }

    /**
     * Method to fetch selected item index
     * @param i
     * @return i
     */
    @Override
    public long getItemId(int i) { return i; }

    /**
     * Method to load the list medication item for ListView
     * @param i position
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Check if view exists, if not then inflate it
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mCurrentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Create a list item based off layout definition
            view = inflater.inflate(R.layout.list_medication_item,null);
        }

        //Assign values to the text views using Book object
        TextView nameView = (TextView) view.findViewById(R.id.nameTextView);
        nameView.setText(mFilteredList.get(i).medicationName);

        return view;
    }

    /**
     * Method to check filter value
     * @return mFilter
     */
    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new MedicationFilter();
        }
        return mFilter;
    }

    /**
     * Class to manage the display list based on entered text in search
     */
    private class MedicationFilter extends Filter {

        /**
         * Method to filter values
         * @param constraint
         * @return
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                ArrayList<Medication> tempList = new ArrayList<>();
                //check in the list if the entered text in search amtches with it and save in a temporary list
                for(Medication medication : mMedicationList) {
                    if (medication.medicationName.toLowerCase().
                            contains(constraint.toString().
                                    toLowerCase()))
                        tempList.add(medication);
                }
                results.count = tempList.size();
                results.values = tempList;
            }
            else {
                results.count = mMedicationList.size();
                results.values = mMedicationList;
            }
            return results;
        }

        /**
         * Return searched results
         * @param constraint
         * @param results
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mFilteredList = (ArrayList<Medication>) results.values;
            notifyDataSetChanged();
        }
    }
}
