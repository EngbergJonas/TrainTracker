package com.example.jonasengberg.traintracker;

import java.util.ArrayList;
import java.util.HashMap;

class GetTrainlist {

    ArrayList<String> list;

    GetTrainlist(ArrayList<String> list) {
        this.list = list;
    }

    public String[] getNoteRows()
    {
        String[] noteRows = new String[list.size()];

        for(int i = 0; i < noteRows.length; i++)
        {
            noteRows[i] = list.get(i);
        }
        return noteRows;
    }

}
