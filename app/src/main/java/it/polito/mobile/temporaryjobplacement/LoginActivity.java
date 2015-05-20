package it.polito.mobile.temporaryjobplacement;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;



import it.polito.mobile.temporaryjobplacement.commons.utils.Connectivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.ClearableEditText;
import it.polito.mobile.temporaryjobplacement.model.Student;
import it.polito.mobile.temporaryjobplacement.pcompany.activities.CompanyMainActivity;
import it.polito.mobile.temporaryjobplacement.pstudent.activities.StudentMainActivity;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.AsyncTaskWithProgressBar;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;


public class LoginActivity extends ActionBarActivity {

    private EditText userText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        if(AccountManager.checkIfLoggedIn()) startNextActivity();






        userText = ((ClearableEditText) findViewById(R.id.editTextUser)).editText();
        passText = ((ClearableEditText) findViewById(R.id.editTextPass)).editText();


    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }






    public void startNextActivity(){
        try {
            String userType=AccountManager.getCurrentUserType();
            if(userType.equals("student")) {
                startActivity(new Intent(this, StudentMainActivity.class));
            } else {
                startActivity(new Intent(this,CompanyMainActivity.class));
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void signUp(View v) {

          Intent intent=new Intent(this,SignUpActivity.class);
          startActivity(intent);


    }


    public void login(View v) {

        if(!Connectivity.hasNetworkConnection(getApplicationContext())){
            DialogManager.setDialog(getResources().getString(R.string.no_connectivity_messagge), this);
            return;
        }

        if(userText.getText().toString().trim().equals("") || passText.getText().toString().trim().equals("")){
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(userText.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(passText.getWindowToken(), 0);
            DialogManager.toastMessage(getResources().getString(R.string.empty_field_message), this);
            return;
        }


        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = null;

                try {
                    AccountManager.login(userText.getText().toString(), passText.getText().toString());
                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }
                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
                try {
                    startNextActivity();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }.execute();
    }




    public void loginStudent(View v) {

        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = null;

                try {
                    AccountManager.login("s", "s");
                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }
                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
                try {
                    startNextActivity();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }.execute();
    }


    public void loginCompany(View v) {

        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = null;

                try {
                    AccountManager.login("c", "c");
                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }
                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
                try {
                    startNextActivity();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }.execute();
    }


    /*
    public void newProfile(View v) {



        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = OK_VALUE;



                try {
                    if (!AccountManager.checkIfLoggedIn()) return "Error occurred:\nNo user logged";

                    String profileType = "StudentProfile";
                    if (AccountManager.getCurrentUser().get("userType").equals("company"))
                        profileType = "CompanyProfile";
                    ParseObject profile = new ParseObject(profileType);
                    profile.put("userId", AccountManager.getCurrentUser().getObjectId());
                    profile.put("name", "Nicolo Fodera");
                    AccountManager.setPrivateUserWriteAccess(profile);
                    profile.save();


                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }
                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
            }


        }.execute();

    }


    public void editProfile(View v) {

        new AsyncTaskWithProgressBar(this) {
            @Override
            protected String doInBackground(Void... params) {
                String resultMessage = OK_VALUE;

                try {
                    if (!AccountManager.checkIfLoggedIn()) return "Error occurred:\nNo user logged";

                    String profileType = "StudentProfile";
                    if (AccountManager.getCurrentUser().get("userType").equals("company"))
                        profileType = "CompanyProfile";
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(profileType);
                    ParseQuery<ParseObject> outputQuery = query.whereEqualTo("userId", AccountManager.getCurrentUser().getObjectId());
                    ParseObject studentProfile = outputQuery.getFirst();
                    studentProfile.put("name", System.currentTimeMillis() + "");
                    studentProfile.save();

                } catch (Exception e) {
                    resultMessage = "Error occurred:\n" + e.getMessage();
                    e.printStackTrace();
                }
                return resultMessage;
            }

            @Override
            protected void onPostExecute(String resultMessage) {
                super.onPostExecute(resultMessage);
            }


        }.execute();

    }


*/





}

