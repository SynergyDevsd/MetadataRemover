package save.your.privacy.metadataremover;

import java.util.ArrayList;
import java.util.List;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ListMDFragment extends ListFragment {

    public static ListMDFragment newInstance() {
        return new ListMDFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_list_mda, R.id.text);
        setListAdapter(adapter);
        adapter.addAll(createDataList(100));
    }

    private static List<String> createDataList(int counts) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < counts; i++) {
            list.add("i=" + i);
        }
        return list;
    }
}