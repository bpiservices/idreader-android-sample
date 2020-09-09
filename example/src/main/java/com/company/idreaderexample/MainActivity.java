package com.company.idreaderexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.bpiservices.idreadersdk.Keys;
import eu.bpiservices.idreadersdk.ReadMrtd;
import eu.bpiservices.idreadersdk.Utils;

public class MainActivity extends Activity {
    private TextView dg1TextView;
    private static final String mrz = "P<UTOMOZART<<WOLFGANG<AMADEUS<<<<<<<<<<<<<<<" +
                                      "5463728195UTO5601273M1107146<<<<<<<<<<<<<<04";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dg1TextView = (TextView) findViewById(R.id.dg1_text_view);
        Button readButton = (Button) findViewById(R.id.read_button);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dg1TextView.setText("");
                Intent intent = new Intent(MainActivity.this, ReadMrtd.class);
                intent.putExtra(Keys.VIZ_MRZ, mrz);
                intent.putExtra(Keys.FILE_TO_READ, Utils.READ_MRTD_FILE_DG1_CODE);
                MainActivity.this.startActivityForResult(intent, Utils.READ_MRTD_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.READ_MRTD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras() != null) {
                    int mrtdResultCode = data.getIntExtra(Keys.RESULT_CODE, 9099);
                    String dg1Mrz = data.getStringExtra(Keys.DG1_DATA);
                    if (dg1Mrz != null) {
                        dg1TextView.setText(dg1Mrz + "\n\nCode: " + mrtdResultCode);
                    }
                }
            }
            if (resultCode == RESULT_CANCELED) {
                if (data.getExtras() != null) {
                    int mrtdResultCode = data.getIntExtra(Keys.RESULT_CODE, 9099);
                    switch (mrtdResultCode) {
                        case Utils.READ_MRTD_RESULT_BACDENIED_CODE:
                            dg1TextView.setText("Document / Chip mismatch!");
                            break;
                        case Utils.READ_MRTD_RESULT_FILEREADERROR_CODE:
                            dg1TextView.setText("Keep document in place until finished!");
                            break;
                        default:
                            dg1TextView.setText("Read failed! Code " + mrtdResultCode);
                            break;
                    }
                }
            }
        }
    }
}