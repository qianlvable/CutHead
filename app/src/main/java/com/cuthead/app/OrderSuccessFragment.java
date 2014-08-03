package com.cuthead.app;



import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cuthead.models.OrderAccept;

import it.gmariotti.cardslib.library.internal.Card;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class OrderSuccessFragment extends Fragment {
    TextView baberPhone;
    TextView baberName;
    TextView address;
    TextView shopName;

    public OrderSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_success, container, false);
        OrderAccept order = this.getArguments().getParcelable("order");

        baberName = (TextView)view.findViewById(R.id.baber_name);
        baberPhone = (TextView)view.findViewById(R.id.baber_phone);
        address = (TextView)view.findViewById(R.id.address);
        shopName = (TextView)view.findViewById(R.id.baber_shop);

        if (order != null){
            baberPhone.setText(order.getPhone());
            baberName.setText(order.getBaber());
            address.setText(order.getAddress());
            shopName.setText(order.getShop());

        } else {
            Log.d("CutHead","Order empty error");
        }

        return view;
    }


}
