
package save.your.privacy.metadataremover;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import save.your.privacy.metadataremover.Utils.ImageFilePath;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,MediaScannerConnectionClient {

    private String selectedImagePath;

    private static final int PICK_IMAGE = 1;

    private final String TAG = "MainActivity";


    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";

    private MediaScannerConnection conn;

    //METADATA LAYOUT
    private TextView valueAperture,valueDatetime,valueExposureTime,valueFlash,valueFocalLength,valueGPSAltitude,
                    valueGPSAltitudeRef,valueGPSLatitude,valueGPSLatitudeRef,valueGPSLongitude,
                    valueGPSLongitudeRef,valueGPSTimestamp,valueGPSProcessingMethod,valueGPSDatestamp,
                    valueImageLength,valueImageWidth,valueISO,valueMake,valueModel,valueWhiteBalance,
                    valueOrientation;
    ImageView imagePreview;
    TextView imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BUTTONS
        Button btn_pickImage = (Button) findViewById(R.id.btn_pickImage);
        btn_pickImage.setOnClickListener(this);
        Button btn_delMD = (Button)findViewById(R.id.btn_delMD);
        btn_delMD.setOnClickListener(this);

        //METADATA LAYOUT
        imagePreview = (ImageView)findViewById(R.id.imagePreview);
        imageName = (TextView)findViewById(R.id.imageName);
        valueAperture = (TextView)findViewById(R.id.vl_aperture);
        valueDatetime = (TextView)findViewById(R.id.vl_datetime);
        valueExposureTime = (TextView)findViewById(R.id.vl_exposureTime);
        valueFlash = (TextView)findViewById(R.id.vl_flash);
        valueFocalLength = (TextView)findViewById(R.id.vl_focalLength);
        valueGPSAltitude = (TextView)findViewById(R.id.vl_altitude);
        valueGPSAltitudeRef = (TextView)findViewById(R.id.vl_altitudeRef);
        valueGPSDatestamp = (TextView)findViewById(R.id.vl_datestamp);
        valueGPSLatitude = (TextView)findViewById(R.id.vl_latitude);
        valueGPSLatitudeRef = (TextView)findViewById(R.id.vl_latitudeRef);
        valueGPSLongitude = (TextView)findViewById(R.id.vl_longitude);
        valueGPSLongitudeRef = (TextView)findViewById(R.id.vl_longitudeRef);
        valueGPSProcessingMethod = (TextView)findViewById(R.id.vl_processingMethod);
        valueGPSTimestamp = (TextView)findViewById(R.id.vl_gpsTimestamp);
        valueImageLength = (TextView)findViewById(R.id.vl_imageLength);
        valueImageWidth = (TextView)findViewById(R.id.vl_imageWidth);
        valueISO  = (TextView)findViewById(R.id.vl_iso);
        valueMake = (TextView)findViewById(R.id.vl_make);
        valueModel = (TextView)findViewById(R.id.vl_model);
        valueWhiteBalance = (TextView)findViewById(R.id.vl_whiteBalance);
        valueOrientation = (TextView)findViewById(R.id.vl_orientation);

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
            if (null == data)
                return;
            Uri selectedImageUri = data.getData();
            imagePreview.setImageBitmap(MediaStore.Images.Thumbnails.getThumbnail(
                    getContentResolver(), Long.parseLong(selectedImageUri.getLastPathSegment().replace("image:","")),
                    MediaStore.Images.Thumbnails.MICRO_KIND,
                    (BitmapFactory.Options) null ));
            selectedImagePath = getPath(selectedImageUri);
            imageName.setText(selectedImagePath.substring(selectedImagePath.lastIndexOf("/")+1));
            showMetadata(selectedImagePath);

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
            case R.id.btn_delMD:
                new RemoveMetadata(this).execute(selectedImagePath);
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

    public void showMetadata(String imagePath) {
        try {
            ExifInterface exifData=new ExifInterface(imagePath);

            valueAperture.setText(exifData.getAttribute(ExifInterface.TAG_APERTURE));
            valueDatetime.setText(exifData.getAttribute(ExifInterface.TAG_DATETIME));
            valueExposureTime.setText(exifData.getAttribute(ExifInterface.TAG_EXPOSURE_TIME));
            valueFlash.setText(exifData.getAttribute(ExifInterface.TAG_FLASH));
            valueFocalLength.setText(exifData.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
            valueGPSAltitude.setText(exifData.getAttribute(ExifInterface.TAG_GPS_ALTITUDE));
            valueGPSAltitudeRef.setText(exifData.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF));
            valueGPSLatitude.setText(exifData.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            valueGPSLatitudeRef.setText(exifData.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
            valueGPSLongitude.setText(exifData.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            valueGPSLongitudeRef.setText(exifData.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
            valueGPSTimestamp.setText(exifData.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
            valueGPSProcessingMethod.setText(exifData.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
            valueGPSDatestamp.setText(exifData.getAttribute(ExifInterface.TAG_GPS_DATESTAMP));
            valueImageLength.setText(exifData.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            valueImageWidth.setText(exifData.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            valueISO.setText(exifData.getAttribute(ExifInterface.TAG_ISO));
            valueMake.setText(exifData.getAttribute(ExifInterface.TAG_MAKE));
            valueModel.setText(exifData.getAttribute(ExifInterface.TAG_MODEL));
            valueWhiteBalance.setText(exifData.getAttribute(ExifInterface.TAG_WHITE_BALANCE));
            valueOrientation.setText(exifData.getAttribute(ExifInterface.TAG_ORIENTATION));

        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
            Toast.makeText(getApplicationContext(),"cannot clean exif: " + ex.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.w(TAG, "cannot clean exif: " + t.getMessage(), t);
            Toast.makeText(getApplicationContext(),"cannot clean exif: " + t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri selectedImageUri) {
        System.out.println(selectedImageUri.toString());
        // MEDIA GALLERY
        String selectedImagePath = ImageFilePath.getPath(
                this, selectedImageUri);
        Log.i("Image File Path", "" + selectedImagePath);
        System.out.println("Image Path ="+selectedImagePath);
        return selectedImagePath;
    }
}

