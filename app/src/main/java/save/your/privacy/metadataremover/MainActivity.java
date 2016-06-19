package save.your.privacy.metadataremover;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,MediaScannerConnectionClient {

    private Uri mImageCaptureUri;

    private static final int PICK_IMAGE = 1;
    private static final int EXIF_IMAGE = 2;

    private String appFolder ="MetadataRemover";

    private final String TAG = "MainActivity";


    public String[] allFiles;
    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";

    private MediaScannerConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set interface elements
        Button btn_pickImage = (Button) findViewById(R.id.btn_pickImage);
        btn_pickImage.setOnClickListener(this);
        Button scanBtn = (Button)findViewById(R.id.btn_seeMetadata);
        scanBtn.setOnClickListener(this);
    }

    private void startScan()
    {
        Log.d("Connected", "success" + conn);
        if(conn!=null)
        {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(getApplicationContext(),this);
        conn.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String path     = "";

        if (requestCode == PICK_IMAGE) {

            Uri imageUri =  data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //InputStream stream = getContentResolver().openInputStream(data.getData());
            File fileDst = new File(imageUri.getPath());
            new RemoveMetadata().execute(fileDst.getAbsolutePath());

            Toast.makeText(getApplicationContext(),"Removed metadata from the image",Toast.LENGTH_LONG).show();

        } else if (requestCode == EXIF_IMAGE){

            Uri imageUri =  data.getData();
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //InputStream stream = getContentResolver().openInputStream(data.getData());
            File fileDst = new File(imageUri.getPath());
            new RemoveMetadata().execute(fileDst.getAbsolutePath());

            Toast.makeText(getApplicationContext(),"Removed metadata from the photo",Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v){

        switch(v.getId())
        {
            case R.id.btn_pickImage:
                Intent intentGallery = new Intent();

                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentGallery, "Complete action using"), PICK_IMAGE);
                break;
            case R.id.btn_seeMetadata:
                Intent intentMetadata = new Intent();

                intentMetadata.setType("image/*");
                intentMetadata.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentMetadata, "Complete action using"), EXIF_IMAGE);
                break;
        }
    }

    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected","success"+conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted",uri + "success"+conn);
            System.out.println("URI " + uri);
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            }
        } finally
        {
            conn.disconnect();
            conn = null;
        }
    }
}

