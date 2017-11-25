package pup.com.gsouapp.Interfaces;

import java.util.List;
import java.util.Map;

public interface DialogCallbackContract {

    void passDataBackToFragment(Map<String, List<String>> values);
}
