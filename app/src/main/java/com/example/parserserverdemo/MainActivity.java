package com.example.parserserverdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
  Button actionBtn;
  TextView toggleBtn;
  EditText usernameField, passwordField;

  public void toggleButtons(View view) {
    String tag = actionBtn.getTag().toString();

    switch (tag) {
      case "signup":
        toggleBtn.setText("Or, Sign Up");
        actionBtn.setTag("login");
        actionBtn.setText("Login");
        break;
      case "login":
        toggleBtn.setText("Or, Login");
        actionBtn.setTag("signup");
        actionBtn.setText("Sign Up");
        break;
    }
  }

  public void registerUser(View view) {
    String username = usernameField.getText().toString().trim();
    String password = passwordField.getText().toString().trim();
    String tag = actionBtn.getTag().toString();

    if (username.isEmpty() || password.isEmpty() || username == "" || password == "")
      showToast("Please Fill All Fields");

    switch (tag) {
      case "login":
        LOGIN(username, password);
        break;
      case "signup":
        REGISTER(username, password);
        break;
    }
  }

  public void LOGIN(String un, String pw) {
    ParseUser.logInInBackground(un, pw, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (user != null) {
          showAllUsers();
        } else {
          showToast("Invalid username or password");
        }
      }
    });
  }

  public void REGISTER(final String un, final String pw) {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
    query.whereEqualTo("username", un);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null) {
          if (objects.size() == 0) {
            singup(un, pw);
          }
        }
      }
    });
  }

  public void singup(String un, String pw) {
    ParseUser user = new ParseUser();
    user.setUsername(un);
    user.setPassword(pw);

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          showToast("Signed Up Successfully!");
          showAllUsers();
        } else {
          showToast("Username Taken. Choose Another");
        }
      }
    });
  }

  public void showToast(String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
  }

  public void showAllUsers() {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (ParseUser.getCurrentUser() != null)
      showAllUsers();

    actionBtn = (Button) findViewById(R.id.actionButton);
    toggleBtn = (TextView) findViewById(R.id.toggleButton);
    usernameField = (EditText) findViewById(R.id.usernameTextView);
    passwordField = (EditText) findViewById(R.id.passwordTextView);

    //for hiding keyboard
    passwordField.setOnKeyListener(this);

    //attach events to these elems for hiding keyboard by clicking on screen
    ConstraintLayout background = (ConstraintLayout) findViewById(R.id.backgroundLayout);
    ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);

    background.setOnClickListener(this);
    logoImageView.setOnClickListener(this);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  //hiding keyboard on enter key
  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
      registerUser(v);      //v is view which doesn't matter but just  a param
    return false;
  }

  //for hiding keyboard on screen click
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.backgroundLayout || v.getId() == R.id.logoImageView) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }
}


//***********************BELOW WAS ALL PRACTICE********************
//    //Logging out User
//    ParseUser.logOut();
//    if (ParseUser.getCurrentUser() != null) {
//      System.out.println("CurrentUser: logged In" + ParseUser.getCurrentUser().getUsername());
//    } else {
//      System.out.println("User Not Logged In");
//    }

//check If User is logged in when app opens up
/*    if (ParseUser.getCurrentUser() != null) {
      System.out.println("CurrentUser: logged In" + ParseUser.getCurrentUser().getUsername());
    } else {
      System.out.println("User Not Logged In");
    }*/

//LOGIN CODE
/*    ParseUser.logInInBackground("JeffBezos", "jeffy^%65", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (user != null)
          System.out.println("Login: SUCCESS");
        else
          System.out.println("Login: FAILED");
      }
    });*/


//signup users simple isn't it
    /*
    ParseUser user = new ParseUser();
    user.setUsername("JeffBezos");
    user.setPassword("jeffy^%65");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null)
          System.out.println("SIGNUP success");
        else
          System.out.println("SIGNUP failed");
      }
    });*/


//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//
//    query.whereGreaterThan("score", 150);   //get all user whose score > 150
//
//    query.findInBackground(new FindCallback<ParseObject>() {
//      @Override
//      public void done(List<ParseObject> objects, ParseException e) {
//        if (e == null) {
//          System.out.println("FindInBg:Total: " + objects.size() + " objs");
//
//          if (objects.size() > 0) {
//            for (ParseObject object : objects) {
//              object.put("score", object.getInt("score") + 50);
//              object.saveInBackground();
//
//              System.out.println("Updated Score: " + object.getInt("score"));
//            }
//          }
//        }
//      }
//    });


//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//
//    query.whereEqualTo("username","malik");
//    query.setLimit(1);
//
//    query.findInBackground(new FindCallback<ParseObject>() {
//      @Override
//      public void done(List<ParseObject> objects, ParseException e) {
//        if (e == null) {
//          System.out.println("FindInBg:Total: " + objects.size() + " objs");
//
//          if (objects.size() > 0) {
//            for (ParseObject object : objects) {
//              System.out.println("Result: " + object.getInt("score"));
//            }
//          }
//        }
//      }
//    });

    /*
    ParseObject score = new ParseObject("Score");
    score.put("username", "JKA");
    score.put("score", 88);

    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null)
          System.out.println("SUCCESS SAVED");
        else
          System.out.println("ERROR SAVING" + e.toString());
      }
    });
*/

//retreiving data/objects from parse server
//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//    query.getInBackground("bZ1RqY8Sl2", new GetCallback<ParseObject>() {
//      @Override
//      public void done(ParseObject object, ParseException e) {
//        if (e == null && object != null) {
//
//          //updating values in parse server
//          object.put("score", 300);
//          object.saveInBackground();
//
//          System.out.println("VALUE FROM PARSE: " + object.getString("username"));
//          System.out.println("VALUE FROM PARSE: " + object.getInt("score"));
//        } else
//          System.out.println("ERROR BRUH");
//      }
//    });

//PRACTICE FOR MAKING TWEET WITH USERNAME
//    ParseObject object = new ParseObject("Tweet");
//    object.put("username", "MLKKK");
//    object.put("tweet", "Hello there! whats' up");
//    object.saveInBackground(new SaveCallback() {
//      @Override
//      public void done(ParseException e) {
//        if (e == null)
//          System.out.println("TWEET saved in Tweet class/object");
//        else
//          System.out.println("tweet Not Saved");
//      }
//    });

//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
//    query.getInBackground("bTdMT3uviL", new GetCallback<ParseObject>() {
//      @Override
//      public void done(ParseObject object, ParseException e) {
//        if(e==null && object != null){
//          object.put("tweet","I changed my tweet u know");
//          object.saveInBackground();
//          System.out.println("TWEET: "+object.getString("tweet"));
//        }
//      }
//    });