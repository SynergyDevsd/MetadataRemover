package save.your.privacy.metadataremover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by ifrey on 23/06/16.
 */


public class MDAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] labels;

    public MDAdapter(Context context, HashMap<String,String> mdMap) {
        super(context, R.layout.fragment_list_mda);
        this.context = context;
        this.values = mdMap.values().toArray(new String[0]);
        this.labels = mdMap.entrySet().toArray(new String[0]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.fragment_list_mda, parent, false);
        TextView tvLabel = (TextView) rowView.findViewById(R.id.label);
        TextView tvText = (TextView) rowView.findViewById(R.id.text);
        tvLabel.setText(labels[position]);
        tvText.setText(values[position]);

        return rowView;
    }
}