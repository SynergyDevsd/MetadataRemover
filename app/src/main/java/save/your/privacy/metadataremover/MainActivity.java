
package save.your.privacy.metadataremover;

import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


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

    public static HashMap<String, String> md;

    //METADATA LAYOUT
    TextView valueAperture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BUTTONS
        Button btn_pickImage = (Button) findViewById(R.id.btn_pickImage);
        btn_pickImage.setOnClickListener(this);
        Button btn_delMD = (Button)findViewById(R.id.btn_delMD);
        btn_delMD.setOnClickListener(this);
        Button btn_pickImageBot = (Button) findViewById(R.id.btn_pickImageBot);
        btn_pickImageBot.setOnClickListener(this);
        Button btn_delMDBot = (Button)findViewById(R.id.btn_delMDBot);
        btn_delMDBot.setOnClickListener(this);

        //METADATA LAYOUT
        ImageView imagePreview = (ImageView)findViewById(R.id.imagePreview);
        TextView imageName = (TextView)findViewById(R.id.imageName);
        valueAperture = (TextView)findViewById(R.id.vl_aperture);



        //ListView lv = (ListView) findViewById(R.id.lvMetadata);

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
            getMetadata(imageUri);
            /*File fileDst = new File(imageUri.getPath());
            new RemoveMetadata().execute(fileDst.getAbsolutePath());

            Toast.makeText(getApplicationContext(),"Removed metadata from the image",Toast.LENGTH_LONG).show();*/
            // The data to show
            /*List<Map<String,String>> testList = new ArrayList<>();
            testList.add(md);*/
            //planetsList.add(md);
            /*List<Map<String,String>> testList = new ArrayList<>();
            for (String key : md.keySet()) {
                // ...
                testList.add(key);
            }
            for (Map.Entry<String, String> entry : md.entrySet()) {
                testList.add(new Mapentry);
                // ...
            }
            private HashMap<String, String> listMD(String key, String name) {
                HashMap<String, String> md = new HashMap<String, String>();
                md.put(key, name);
                return md;
            }*/
            /*testList.add(md.   md.get(ExifInterface.TAG_APERTURE));
            testList.add(md.get(ExifInterface.TAG_DATETIME));
            testList.add(md.get(ExifInterface.TAG_MODEL));*/

            /*private HashMap<String, String> createPlanet(String key, String name) {
                HashMap<String, String> planet = new HashMap<String, String>();
                planet.put(key, name);

                return planet;
            }*/
            // This is a simple adapter that accepts as parameter
// Context
// Data list
// The row layout that is used during the row creation
// The keys used to retrieve the data
// The View id used to show the data. The key number and the view id must match


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
            case R.id.btn_pickImage://test message
                Intent intentGallery = new Intent();

                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentGallery, "Complete action using"), PICK_IMAGE);
                break;
            /*case R.id.btn_seeMetadata:
                Intent intentMetadata = new Intent();

                intentMetadata.setType("image/*");
                intentMetadata.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentMetadata, "Complete action using"), EXIF_IMAGE);
                break;*/
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

    private void getMetadata(Uri imageUri) {
        try {
            File fileDst = new File(imageUri.getPath());
            ExifInterface exifData=new ExifInterface(fileDst.getAbsolutePath());

            md.clear();

            valueAperture.setText(exifData.getAttribute(ExifInterface.TAG_APERTURE));
            md.put(ExifInterface.TAG_APERTURE,exifData.getAttribute(ExifInterface.TAG_APERTURE));
            md.put(ExifInterface.TAG_DATETIME,exifData.getAttribute(ExifInterface.TAG_DATETIME));
            md.put(ExifInterface.TAG_EXPOSURE_TIME,exifData.getAttribute(ExifInterface.TAG_EXPOSURE_TIME));
            md.put(ExifInterface.TAG_FLASH,exifData.getAttribute(ExifInterface.TAG_FLASH));
            md.put(ExifInterface.TAG_FOCAL_LENGTH,exifData.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
            md.put(ExifInterface.TAG_GPS_ALTITUDE,exifData.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
            md.put(ExifInterface.TAG_GPS_ALTITUDE_REF,exifData.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF));
            md.put(ExifInterface.TAG_GPS_LATITUDE,exifData.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            md.put(ExifInterface.TAG_GPS_LATITUDE_REF,exifData.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            md.put(ExifInterface.TAG_GPS_LONGITUDE,exifData.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            md.put(ExifInterface.TAG_GPS_LONGITUDE_REF,exifData.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
            md.put(ExifInterface.TAG_GPS_TIMESTAMP,exifData.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
            md.put(ExifInterface.TAG_GPS_PROCESSING_METHOD,exifData.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
            md.put(ExifInterface.TAG_GPS_DATESTAMP,exifData.getAttribute(ExifInterface.TAG_GPS_DATESTAMP));
            md.put(ExifInterface.TAG_IMAGE_LENGTH,exifData.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            md.put(ExifInterface.TAG_IMAGE_WIDTH,exifData.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            md.put(ExifInterface.TAG_ISO,exifData.getAttribute(ExifInterface.TAG_ISO));
            md.put(ExifInterface.TAG_MAKE,exifData.getAttribute(ExifInterface.TAG_MAKE));
            md.put(ExifInterface.TAG_MODEL,exifData.getAttribute(ExifInterface.TAG_MODEL));
            md.put(ExifInterface.TAG_WHITE_BALANCE,exifData.getAttribute(ExifInterface.TAG_WHITE_BALANCE));
            md.put(ExifInterface.TAG_ORIENTATION,exifData.getAttribute(ExifInterface.TAG_ORIENTATION));

        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
            Toast.makeText(getApplicationContext(),"cannot clean exif: " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.w(TAG, "cannot clean exif: " + t.getMessage(), t);
            Toast.makeText(getApplicationContext(),"cannot clean exif: " + t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

}

