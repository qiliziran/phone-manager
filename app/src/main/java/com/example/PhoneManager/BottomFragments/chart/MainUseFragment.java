package com.example.PhoneManager.BottomFragments.chart;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.fragment.app.Fragment;
        import com.example.PhoneManager.R;
public class MainUseFragment extends Fragment {
    String mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainuse, container, false) ;
        TextView textView = (TextView) view.findViewById(R.id.tv1);
        return view;
    }

}

