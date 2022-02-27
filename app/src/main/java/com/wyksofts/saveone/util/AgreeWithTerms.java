package com.wyksofts.saveone.util;

import android.widget.CheckBox;

public class AgreeWithTerms {

    public boolean agreewithTerms(CheckBox agree_with_terms) {
        //agree with terms
        boolean status;
        if(agree_with_terms.isChecked()){
            status=true;
        }
        else {
            status=false;
        }
        return status;
    }
}

