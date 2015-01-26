package com.hettinger.sean.quiethoursreredux;

import android.widget.Switch;

/**
 *
 * ListViewItem class to create custom listview objects to be use in conjunction with adapters
 *
 * Created by Sean on 1/26/2015.
 */
public class ListViewItem {

    public String title;
    public Switch OnOffSwitch;
    public int id;

    public ListViewItem(){
        super();
    }

    public ListViewItem(String title, Switch OnOffSwitch, int id) {
        super();
        this.title = title;
        this.OnOffSwitch = OnOffSwitch;
        this.id = id;
    }
}