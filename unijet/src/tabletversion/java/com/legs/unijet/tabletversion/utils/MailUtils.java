package com.legs.unijet.tabletversion.utils;

import android.content.Context;

import com.legs.unijet.smartphone.R;

public class MailUtils {

    public static Context context;


    public MailUtils(Context context) {
        MailUtils.context = context;
    }

    public static boolean checkDomainStudents (String url){
        String[] domains_array = context.getResources().getStringArray(R.array.students_domains);
        for(String site : domains_array){
            if(url.contains(site)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkDomainTeachers (String url){
        String[] domains_array = context.getResources().getStringArray(R.array.teaching_domains);
        for(String site : domains_array){
            if(url.contains(site)) {
                return true;
            }
        }
        return false;
    }

}
