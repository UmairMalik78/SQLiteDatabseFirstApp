package haqnawaz.org.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button buttonAdd, buttonViewAll;
    EditText editName, editAge;
    Switch switchIsActive;
    ListView listViewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonViewAll = findViewById(R.id.buttonViewAll);
        editName = findViewById(R.id.editTextName);
        editAge = findViewById(R.id.editTextAge);
        switchIsActive = findViewById(R.id.switchCustomer);
        listViewCustomer = findViewById(R.id.listViewCustomer);
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        ArrayList<String> allCustomerList = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, allCustomerList);
        listViewCustomer.setAdapter(arrayAdapter);
        listViewCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dbHelper.removeCustomer(dbHelper.GetAllRecords().get(position).getId())) {
                    allCustomerList.remove(position);
                    RefreshData();
                }
            }
            public void RefreshData(){
                listViewCustomer.setAdapter(arrayAdapter);
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            CustomerModel customerModel;
            @Override
            public void onClick(View v) {
                try {
                    customerModel = new CustomerModel(editName.getText().toString(), Integer.parseInt(editAge.getText().toString()), switchIsActive.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                if (editName.getText().toString().equals("") || editAge.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill all the fields!!!", Toast.LENGTH_SHORT).show();
                } else {

                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    boolean b = dbHelper.addCustomer(customerModel);
                }
            }
        });

        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCustomerList.clear();
                ArrayList<CustomerModel> customersList=dbHelper.GetAllRecords();
                if(customersList.isEmpty()){
                    Toast.makeText(MainActivity.this,"No Records Added yet" , Toast.LENGTH_SHORT).show();
                }else {
                    allCustomerList.addAll(formatCustomerList(customersList));
                    listViewCustomer.setAdapter(arrayAdapter);
                }
            }
            ArrayList<String> formatCustomerList(ArrayList<CustomerModel> customerList){
                ArrayList<String> formattedList=new ArrayList<String>();
                for(int i=0;i<customerList.size();i++){
                    String customerInfo="ID: "+customerList.get(i).getId()+" Name: "+customerList.get(i).getName()+" Age: "+customerList.get(i).getAge();
                    formattedList.add(customerInfo);
                }
                return formattedList;
            }
        });
    }
}