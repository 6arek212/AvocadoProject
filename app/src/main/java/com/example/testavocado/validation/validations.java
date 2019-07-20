package com.example.testavocado.validation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testavocado.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validations {

    public static final String TAG="validations";
    private Context mcontext;
//ctor------------------------------------------------------------------------------------->
    public validations(Context mcontext) {
        this.mcontext = mcontext;
    }
    public validations()
    {}
    //------------------------------------------------------------------------------------->

    // check password validation func
    public  boolean PasswordValidation(final String password) {
        Log.d(TAG, "PasswordValidation: Password validation"+password);
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^" +
                "(?=.*[0-9])" + //at least 1 dgit
               // "(?=.*[a-z])" + //at least 1 lower case Letter
               // "(?=.*[A-Z])" + //at least 1 upper case Letter
                "(?=.*[A-Za-z])" + //any letter
               // "(?=.*[@#$%^&+=!])" + //at least 1 spcial Charcter
                "(?=\\S+$)" + // no white space
                ".{6,16}" + //at least 6 charcter and maximum 16 charcter
                "$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if(password.length()< mcontext.getResources().getInteger(R.integer.password_minimum_length))
            Toast.makeText(mcontext, mcontext.getString(R.string.password_atleast)+"", Toast.LENGTH_SHORT).show();

        if (password.length()>mcontext.getResources().getInteger(R.integer.password_maximum_length))
            Toast.makeText(mcontext, mcontext.getString(R.string.password_maximum)+"", Toast.LENGTH_SHORT).show();


        if(!matcher.matches())
            Toast.makeText(mcontext, mcontext.getString(R.string.password_conditions)+"", Toast.LENGTH_SHORT).show();

        return matcher.matches();

    }

    // check emil validation func
    public  boolean EMailValidation(String emailstring) {
        Log.d(TAG, "EMailValidation: Emilvalidation"+emailstring);
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);

        if(!emailMatcher.matches())
            Toast.makeText(mcontext, mcontext.getString(R.string.invalid_emiladdress), Toast.LENGTH_SHORT).show();

        return emailMatcher.matches();
    }


    // remove space function
   static public String space_remove(String text)
    {
        // -1 not has space
        if(text.indexOf(" ")!=-1) {
            String name1 = "";
            String[] splitedstring = text.split(" ");
            for (int i = 0; i < splitedstring.length; i++)
                name1 += splitedstring[i];
            text = name1;
            return text;
        }
        return text;
    }


    //validate FistName
    public  int Name_validate(String firstName)
    {
        //1 the name is good
        //-1 the name is wrong
        //remove spaceing

        if(firstName.indexOf(" ")!=-1) {
            String name1 = "";
            String[] splitedstring = firstName.split(" ");
            for (int i = 0; i < splitedstring.length; i++)
                name1 += splitedstring[i];
            firstName=name1;
        }

        if(firstName.length()<mcontext.getResources().getInteger(R.integer.first_last_minimum_length)
                || firstName.length()>mcontext.getResources().getInteger(R.integer.first_last_name_maximux_length)) {

            if(firstName.length()<mcontext.getResources().getInteger(R.integer.first_last_minimum_length))
            Toast.makeText(mcontext, mcontext.getString(R.string.short_name)+"", Toast.LENGTH_SHORT).show();
            if(firstName.length()>mcontext.getResources().getInteger(R.integer.first_last_name_maximux_length))
                Toast.makeText(mcontext, mcontext.getString(R.string.long_name)+"", Toast.LENGTH_SHORT).show();

            return -1;
        }
        if(firstName.matches( "[a-zA-Z]*" ))
            return 1;

        else {
            Toast.makeText(mcontext, mcontext.getString(R.string.first_last_charcter_numbers_only)+"", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    // validate last name
   // public  boolean validateLastName( String lastName)
   // {
     //   return lastName.matches( "[a-z]+[A-Z]+[0-9]*" );
   // }
}
