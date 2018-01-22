package demopay.gusheng.merchantpay_android.Fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demopay.gusheng.merchantpay_android.R;

/**
 * Created by fby on 2017/9/11.
 */

public class TabbarFragement extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tabbar_button,container,false);

//        View view = inflater.inflate(R.layout.fragment_first, null);

        return view;

    }


}
