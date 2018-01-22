package demopay.gusheng.merchantpay_android.adapter;

import com.wizarpos.paymentrouter.aidl.IWizarPayment;

/**
 * Created by fby on 2017/11/6.
 */

public class ServiceObject {

    public IWizarPayment IWizarPayment;

    private static ServiceObject instance;

    private ServiceObject(){

        this.IWizarPayment = null;
    }

    public static ServiceObject getInstance() {
        if (instance == null){

            instance = new ServiceObject();
        }

        return instance;
    }

}
