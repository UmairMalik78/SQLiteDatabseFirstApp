package haqnawaz.org.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final String CUSTOMER_AGE = "CustomerAge";
    public static final String ACTIVE_CUSTOMER = "ActiveCustomer";
    public static final String CUSTOMER_ID = "CustomerID";
    public static final String CUST_TABLE = "CustTable";

    public DBHelper(@Nullable Context context) {
        super(context, "MyDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("ALC","Database created");
        //String createTableSTatementOne = "CREATE TABLE CustTable(CustomerID Integer PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME_FIRST + " Text, CustomerAge Int, ActiveCustomer BOOL) ";
        String createTableSTatement = "CREATE TABLE " + CUST_TABLE + "(" + CUSTOMER_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME + " Text, " + CUSTOMER_AGE + " Int, " + ACTIVE_CUSTOMER + " BOOL) ";
        db.execSQL(createTableSTatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addCustomer(CustomerModel customerModel){
        SQLiteDatabase db = this.getWritableDatabase();
        //Hash map, as we did in bundles
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME,customerModel.getName());
        cv.put(CUSTOMER_AGE, customerModel.getAge());
        cv.put(ACTIVE_CUSTOMER, customerModel.isActive());
        //NullCoumnHack
        long insert = db.insert(CUST_TABLE, null, cv);
        if (insert == -1) { return false; }
        else{return true;}
    }

    public boolean removeCustomer(int id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = CUSTOMER_ID+"=?";
        String whereArgs[] = {String.valueOf(id)};
        int status=db.delete(CUST_TABLE,whereClause,whereArgs);
        db.close();
        return status>0;
    }
    public ArrayList<CustomerModel> GetAllRecords(){
        ArrayList<CustomerModel> myList=new ArrayList<>();
        String query="Select * from " +CUST_TABLE;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                CustomerModel tempCustomerModel=new CustomerModel("",-1,false);
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                int age=cursor.getInt(2);
                boolean isActive=cursor.getInt(3)==1?true:false;
                tempCustomerModel.setId(id);
                tempCustomerModel.setName(name);
                tempCustomerModel.setAge(age);
                tempCustomerModel.setActive(isActive);
                myList.add(tempCustomerModel);
            }while(cursor.moveToNext()!=false);
        }
        cursor.close();
        sqLiteDatabase.close();
        return myList;
    }
}
