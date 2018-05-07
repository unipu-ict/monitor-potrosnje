package hr.unipu.inf.ma.monitorpotrosnje;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    final Context context = this;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar c = Calendar.getInstance();
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        final String month_name = new DateFormatSymbols().getMonths()[month];
        sqLiteDatabase = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("create table if not exists budget (b_id integer primary key autoincrement, b_month integer, b_year integer, b_amount integer,b_cash integer);");
        TextView tx = (TextView) findViewById(R.id.available_budget);
        TextView tx1 = (TextView) findViewById(R.id.balance);
        String temp = tx.getText().toString();
        temp = temp + "(" + month_name + " - " + year + ") :";
        tx.setText(temp);
        Cursor c1 = sqLiteDatabase.rawQuery("select b_amount, b_cash from budget where b_month  = "+month+" and b_year = "+year+"",null);
        if(c1.moveToFirst()){
            int x = c1.getInt(0);
            int y = c1.getInt(1);
            int z = x - y;
            tx1.setText(String.valueOf(z) + " HRK");
        }

        ImageButton budget = (ImageButton) findViewById(R.id.budget);
        ImageButton expenses = (ImageButton) findViewById(R.id.expenses);

        ImageButton reports = (ImageButton) findViewById(R.id.reports);

        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(getApplicationContext(),BudgetActivity.class);
                startActivity(in1);
            }
        });

        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(getApplicationContext(),ExpenseActivity.class);
                startActivity(in3);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in5 = new Intent(getApplicationContext(),ReportsActivity.class);
                startActivity(in5);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Izlaz");
        builder.setMessage("Jeste sigurni da želite izaći?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
