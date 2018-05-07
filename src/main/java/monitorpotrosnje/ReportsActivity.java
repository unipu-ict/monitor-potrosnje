package hr.unipu.inf.ma.monitorpotrosnje;

/**
 * Created by Mateo on 25.6.2017..
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import hr.unipu.inf.ma.monitorpotrosnje.ExpenseActivity;

import java.util.Calendar;

public class ReportsActivity extends AppCompatActivity {
    DatePicker datePicker;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Calendar c1 = Calendar.getInstance();
        final int month = c1.get(Calendar.MONTH);
        final int year = c1.get(Calendar.YEAR);

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("ExpenseTracker", MODE_PRIVATE, null);

        Cursor c = sqLiteDatabase.rawQuery("select e_category_id,e_amount,e_mark from expenses where budget_id = (select b_id from budget where b_month=" + month + " and b_year=" + year + ");", null);
        if (c.moveToFirst() || c.getCount() > 0) {
            int rows = c.getCount();
            int cols = c.getColumnCount();
            ScrollView scrollView = new ScrollView(context);
            scrollView.setVerticalScrollBarEnabled(true);
            scrollView.setBackgroundColor(Color.WHITE);
            scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                    ScrollView.LayoutParams.MATCH_PARENT));
            TableLayout table_layout = new TableLayout(context);

            // vanjska petlja
            for (int i = 0; i < rows; i++) {

                TableRow row = new TableRow(context);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // unutarnja petlja
                for (int j = 0; j < cols; j++) {

                    TextView tv = new TextView(context);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(18);
                    tv.setPadding(50, 10, 10, 10);
                    if (j == 2)
                        if (c.getInt(2) != 0)
                            tv.setText("Plaćeno");
                        else
                            tv.setText("Nije plaćeno!");
                    else if (j == 0) {
                        int y = c.getInt(j);
                        Cursor t = sqLiteDatabase.rawQuery("select c_name from category where c_id=" + y + ";", null);
                        t.moveToFirst();
                        tv.setText(t.getString(0));
                    } else
                        tv.setText(c.getString(j));
                    row.addView(tv);

                }

                c.moveToNext();

                table_layout.addView(row);

            }
            scrollView.addView(table_layout);
            RelativeLayout rt = (RelativeLayout) findViewById(R.id.reports);
            rt.addView(scrollView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

             return super.onOptionsItemSelected(item);
    }
}