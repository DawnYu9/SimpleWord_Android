package com.bubble.simpleword.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: InputStreamVolleyRequest</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author 
 * @date 2015-10-9 下午11:14:35
 */

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
	private Map<String, String> mParams;
    //create a static map for directly accessing headers
	public Map<String, String> responseHeaders ;

    public InputStreamVolleyRequest(int post, String mUrl,Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener, HashMap<String, String> params) {
		// TODO Auto-generated constructor stub
    	
    	 super(post, mUrl, errorListener);
         // this request would never use cache.
         setShouldCache(false);
         mListener = listener;
         mParams=params;
	}

	@Override
	protected Map<String, String> getParams()
			throws com.android.volley.AuthFailureError {
		return mParams;
	};

	
    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}