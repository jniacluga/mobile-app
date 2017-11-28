package pup.com.gsouapp.Adapters;

import android.widget.ListAdapter;

import java.util.List;

public interface ChoiceAdapter extends ListAdapter {

    List<Boolean> getCheckboxState();
}
