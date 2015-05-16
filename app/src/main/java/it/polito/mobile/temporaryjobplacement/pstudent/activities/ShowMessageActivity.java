package it.polito.mobile.temporaryjobplacement.pstudent.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.TemporaryJobPlacementApp;
import it.polito.mobile.temporaryjobplacement.commons.utils.AccountManager;
import it.polito.mobile.temporaryjobplacement.commons.utils.TimeManager;
import it.polito.mobile.temporaryjobplacement.model.Application;
import it.polito.mobile.temporaryjobplacement.model.JobOffer;
import it.polito.mobile.temporaryjobplacement.model.Message;
import it.polito.mobile.temporaryjobplacement.model.Student;

public class ShowMessageActivity extends ActionBarActivity {
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        //Set the custom toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String messageId=getIntent().getStringExtra("SELECTED_MESSAGE");
        final Message[] message = {null};
        final RelativeLayout loadingOverlay =(RelativeLayout) findViewById(R.id.loadingOverlay);
        loadingOverlay.setVisibility(View.VISIBLE);
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    message[0] = Message.getQuery().include("company").include("originalMessage").get(messageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                loadingOverlay.setVisibility(View.GONE);
                initializeView(message[0]);
            }
        }.execute();


    }

    private void initializeView(final Message message) {

        this.message=message;

        getSupportActionBar().setTitle(message.getSubject());
        getSupportActionBar().setSubtitle(message.getCompany().getName());

        //Position stands for job name
        TextView subjectTextView=(TextView) findViewById(R.id.subjectTextView);
        subjectTextView.setText(message.getSubject());

        TextView fromSentTextView=(TextView) findViewById(R.id.fromSentTextView);


        TextView companyStudentTextView=(TextView) findViewById(R.id.companyStudentTextView);
        companyStudentTextView.setText(message.getCompany().getName());

        companyStudentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(ShowMessageActivity.this, StudentDetailActivity.class);
                detailIntent.putExtra("SELECTED_COMPANY", message.getCompany().getObjectId());
                startActivityForResult(detailIntent, 0);

            }
        });

        TextView timestampTextView=(TextView) findViewById(R.id.timestampTextView);
        Button replyButton = (Button) findViewById(R.id.replyButton);

        if(message.getSender().equals("student")) {
            fromSentTextView.setText("To:");
            timestampTextView.setText(TimeManager.getFormattedTimestamp(message.getCreatedAt(), "Sent"));
            replyButton.setVisibility(View.GONE);

        } else {
            fromSentTextView.setText("From:");
            timestampTextView.setText(TimeManager.getFormattedTimestamp(message.getCreatedAt(), "Received"));
            replyButton.setVisibility(View.VISIBLE);

        }



        TextView messageBodyTextView=(TextView) findViewById(R.id.messageBodyTextView);
        messageBodyTextView.setText(message.getMessage());

        if(message.getSender().equals("company")) {
            message.setRead(true);
            message.saveInBackground();
        }

        if(message.getOriginalMessage()!=null) {
            final TextView originalMessageButton =(TextView) findViewById(R.id.originalMessageButton);
            originalMessageButton.setVisibility(View.VISIBLE);
            originalMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    originalMessageButton.setVisibility(View.GONE);

                    TextView originalMessageBodyTextView=(TextView) findViewById(R.id.originalMessageBodyTextView);
                    originalMessageBodyTextView.setText(message.getOriginalMessage().getMessage());

                    LinearLayout originalMessageLayout=(LinearLayout) findViewById(R.id.originalMessageLayout);
                    originalMessageLayout.setVisibility(View.VISIBLE);

                    final TextView originalFullMessageButton =(TextView) findViewById(R.id.originalFullMessageButton);
                    originalFullMessageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ShowMessageActivity.this, ShowMessageActivity.class);
                            i.putExtra("SELECTED_MESSAGE", message.getOriginalMessage().getObjectId());
                            startActivityForResult(i, 0);
                        }
                    });


                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            this.onBackPressed();
            return true;
        }if(id==R.id.action_HOME){
            //setResult(TemporaryJobPlacementApp.exitCode);
            //finish();
            Intent i = new Intent(this, StudentMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== TemporaryJobPlacementApp.exitCode){
            setResult(TemporaryJobPlacementApp.exitCode);
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void reply(View v) {
        try {

            Intent intent = new Intent(this, SendMessageActivity.class);
            intent.putExtra("SELECTED_COMPANY", message.getCompany().getObjectId());
            intent.putExtra("ORIGINAL_MESSAGE", message.getObjectId());
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
