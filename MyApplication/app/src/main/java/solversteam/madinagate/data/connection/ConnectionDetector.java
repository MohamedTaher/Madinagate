package solversteam.madinagate.data.connection;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionDetector {

	private Context _context;
	 
    public ConnectionDetector(Context context){
        this._context = context;
    }

 	public boolean isConnectingToInternet() {
 		System.out.println("in checkInternetConnection");
 		final ConnectivityManager conMgr = 
 			(ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
 		if (conMgr.getActiveNetworkInfo() != null
 				&& conMgr.getActiveNetworkInfo().isAvailable()
 				&& conMgr.getActiveNetworkInfo().isConnected()) {
 			System.out.println("in if in checkInternetConnection");
 			return true;
 		} else {
 			System.out.println("Connection Not Present");
 			return false;
 		}
 	}
}
