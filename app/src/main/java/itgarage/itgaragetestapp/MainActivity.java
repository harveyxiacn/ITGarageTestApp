package itgarage.itgaragetestapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    List<Member> Members = new ArrayList<Member>();
    ListView memberListView;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view initialization
        nameTxt = (EditText) findViewById(R.id.nameText);
        phoneTxt = (EditText) findViewById(R.id.phoneText);
        emailTxt = (EditText) findViewById(R.id.emailText);
        addressTxt = (EditText) findViewById(R.id.addressText);
        memberListView = (ListView) findViewById(R.id.listView);
        dbHandler = new DBHandler(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.list);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.creator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.addButton);
        //add button listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member(dbHandler.getMembersCount(), String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()));
                dbHandler.createMember(member);
                Members.add(member);
                showList();
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " has been added!", Toast.LENGTH_SHORT).show();
                nameTxt.setText("");
                phoneTxt.setText("");
                emailTxt.setText("");
                addressTxt.setText("");
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(!String.valueOf(nameTxt.getText()).trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    String input = String.valueOf(s);
                    String numberOnly = checkNumberOnly(input);
                    String formatNumber = formatPhoneNumber(numberOnly);
                    phoneTxt.removeTextChangedListener(this);
                    phoneTxt.setText(formatNumber);
                    phoneTxt.setSelection(formatNumber.length());
                    phoneTxt.addTextChangedListener(this);
                }
            }

            private String checkNumberOnly(CharSequence s){
                return String.valueOf(s).replaceAll("[^0-9]", "");
            }

            private String formatPhoneNumber(CharSequence s){
                int index = 0;
                String temp = "";
                for (int i = 0; i < s.length(); i++){
                    if(index==0){
                        temp += "+";
                    }else if(index==1){
                        temp += "(";
                    }else if(index==4){
                        temp += ")";
                    }else if(index==7){
                        temp += "-";
                    }
                    temp += s.charAt(i);
                    index++;
                }
                return temp;
            }
        });

        if (dbHandler.getMembersCount() != 0)
            Members.addAll(dbHandler.getAllMembers());
        showList();

    }

    private  void showList(){
        ArrayAdapter<Member> adapter = new MemberListAdapter();
        memberListView.setAdapter(adapter);
    }

    private void addMember(int id, String name, String phone, String email, String address){
        Members.add(new Member(id, name, phone, email, address));
    }

    private class MemberListAdapter extends ArrayAdapter<Member>{
        public MemberListAdapter(){
            super(MainActivity.this, R.layout.listview_item, Members);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Member currentMember = Members.get(position);
            //set up member list item view
            TextView name = (TextView) view.findViewById(R.id.mName);
            name.setText(currentMember.get_name());
            TextView phone = (TextView) view.findViewById(R.id.mPhone);
            phone.setText(currentMember.get_phone());
            TextView email = (TextView) view.findViewById(R.id.mEmail);
            email.setText(currentMember.get_email());
            TextView address = (TextView) view.findViewById(R.id.mAddress);
            address.setText(currentMember.get_address());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
