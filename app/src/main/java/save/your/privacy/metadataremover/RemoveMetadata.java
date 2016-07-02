package save.your.privacy.metadataremover;

import android.content.Context;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ifrey on 4/03/15.
 */
public class RemoveMetadata extends AsyncTask<String,Void,Boolean> {
    private final String TAG = "RemoveMetadata";
    private Context mContext;
    private String selectImagePath;

    public RemoveMetadata(Context context){
        mContext = context;
    }
    @Override
    protected Boolean doInBackground(String... filePath){
        if (null == filePath[0]) return false;
        try {

            selectImagePath = filePath[0];
            ExifInterface exifData=new ExifInterface(selectImagePath);

            exifData.setAttribute(ExifInterface.TAG_APERTURE, "0"); //rational64u
            exifData.setAttribute(ExifInterface.TAG_DATETIME, ""); //string
            exifData.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, "0.0000" ); //rational64u
            exifData.setAttribute(ExifInterface.TAG_FLASH, "0" ); //int16u
            exifData.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, " 0/0" ); //rational64u
            exifData.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "0/0" ); //rational64u
            exifData.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, "0" ); //string[2]
            exifData.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "0/0,0/0000,00000000/00000" ); // rational64u
            exifData.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "0" ); //string[2]
            exifData.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "0/0,0/0,000000/00000 " ); //rational64u
            exifData.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "0" ); //sting[2]
            exifData.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, "0:0:0 " ); //rational64u[3]
            //exifData.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD, "0" ); //undef
            exifData.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, " " ); //string[11]
            exifData.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, "0" ); //int32u
            exifData.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, "0" ); //int32u
            exifData.setAttribute(ExifInterface.TAG_ISO, " " ); //int16u
            exifData.setAttribute(ExifInterface.TAG_MAKE, " " ); //string
            exifData.setAttribute(ExifInterface.TAG_MODEL, " " ); //string
            exifData.setAttribute(ExifInterface.TAG_WHITE_BALANCE, " " ); //string
            exifData.setAttribute(ExifInterface.TAG_ORIENTATION, " " ); // int16u

            exifData.saveAttributes();
        }
        catch (  IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
            return false;
        }
        catch (  Throwable t) {
            Log.w(TAG, "cannot clean exif: ", t);
            return false;
        }
        return true;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        ((MainActivity) mContext).showMetadata(selectImagePath);
        Toast.makeText(mContext.getApplicationContext(),"Removed metadata from the photo",Toast.LENGTH_LONG).show();
    }
}
