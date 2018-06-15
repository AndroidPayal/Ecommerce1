package payal.cluebix.www.ecommerce.Handlers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class RquestHandler {
    //This is Singleton class

    private static RquestHandler mInstance;
    private RequestQueue reqQueue;
    private static Context context;

    private RquestHandler(Context cont){
        context= cont;
        reqQueue = getReqQueue();
    }


    public static synchronized RquestHandler getInstance(Context context1){
        //To get instanse of this class whis returns request handler
        if(mInstance==null){
            mInstance= new RquestHandler(context1);
        }
        return mInstance;
    }


    public RequestQueue getReqQueue(){// to initiallize req Queue
        if(reqQueue == null){
            reqQueue = Volley.newRequestQueue(context.getApplicationContext());

        }
        return  reqQueue;
    }


    //adding request to the req Queue
    public  <T>void addToRequestQueue(Request<T> request){
        reqQueue.add(request);
    }
}
