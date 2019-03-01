package edu.monash.iforme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** Class to handle the welcome screens and assign to Adapter
 * Created by ykha0002 on 8/6/18.
 */

public class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {

    //array to store the references for the layouts of screens
    private int[] layouts;
    //to inflate the inflater service
    private LayoutInflater inflater;
    //context of application
    private Context context;

    /**
     * constructor to instantiate the array and context and inflate the layout
     * @param layouts
     * @param context
     */
    public ViewPagerAdapter(int[] layouts, Context context){
        this.layouts = layouts;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * To get the count of layouts
     * @return number of layouts for welcome screen
     */
    @Override
    public int getCount() {
        return layouts.length;
    }

    /**
     * to check if the current object is the same as the view of layout
     * @param view layout
     * @param object
     * @return true if view and object are the same
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * inflate the layout based on the current posiiton
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }

    /**
     * To finish the view and remove it from the container
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
