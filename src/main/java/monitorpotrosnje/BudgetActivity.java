package hr.unipu.inf.ma.monitorpotrosnje;

/**
 * Created by Mateo on 25.6.2017..
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class BudgetActivity extends AppCompatActivity {
    final Context context = this;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        sqLiteDatabase = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);
        Button createButton = (Button) findViewById(R.id.create_budget);
        Button updateButton = (Button) findViewById(R.id.update_budget);
        final Calendar c = Calendar.getInstance();
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        final String month_name = new DateFormatSymbols().getMonths()[month];
        TextView tx = (TextView)findViewById(R.id.budget_header);
        tx.setText("Budžet\n("+month_name+"-"+year+")");
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){



                Cursor cursor = sqLiteDatabase.rawQuery("select b_month,b_year,b_amount from budget where b_month = "+month+" and b_year = "+year+";", null);
                if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Kreiraj budžet"); //Set Alert dialog title here
                    alert.setMessage("Unesi iznos"); //Message here

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alert.setView(input);

                    alert.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String amount = input.getEditableText().toString();
                            Toast.makeText(context, "Budžet kreiran", Toast.LENGTH_LONG).show();
                            sqLiteDatabase.execSQL("insert into budget(b_month,b_year,b_amount,b_cash) values ("+month+","+year+","+amount+",0);");
                        }
                    });
                    alert.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                }
                else {
                    cursor.moveToFirst();
                    int amount = cursor.getInt(2);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Budžet");

                    String month_name = new DateFormatSymbols().getMonths()[month];
                    builder.setMessage("Budžet je već kreiran za " + month_name + " - " + year + " : " + amount + " HRK");
                    builder.setPositiveButton("Nazad", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);
                Cursor cursor = sqLiteDatabase.rawQuery("select b_month,b_year,b_amount from budget where b_month = "+month+" and b_year = "+year+";", null);
                 if(cursor.moveToFirst() || cursor.getCount() > 0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Izmjeni budžet"); //Naslov
                    alert.setMessage("Unesi iznos:"); //Poruka

                   //Unos podataka
                    final EditText input = new EditText(context);
                     input.setInputType(InputType.TYPE_CLASS_NUMBER);
                     alert.setView(input);

                    alert.setPositiveButton("Ažuriraj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String amount = input.getEditableText().toString();
                            Toast.makeText(context, "Budžet ažuriran ", Toast.LENGTH_LONG).show();
                            sqLiteDatabase.execSQL("update budget set b_amount = "+amount+",b_cash=0 where b_month = "+month+" and b_year = "+year+";");
                        }
                    });
                    alert.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Odustani
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
                else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Budžet");
                     String month_name = new DateFormatSymbols().getMonths()[month];
                    builder.setMessage("Budžet nije dostupan za: " + month_name + " - " + year + "\nPrvo kreiraj novi budžet");
                    builder.setPositiveButton("Nazad", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(BudgetActivity.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }
}
