package za.co.vehmon.application.synchronizers;

import android.provider.Browser;

/**
 * Created by Renaldo on 2/16/2015.
 */
public class SynchronizedResult {

    public Boolean getSynchronizedSuccessful() {
        return synchronizedSuccessful;
    }

    public void setSynchronizedSuccessful(Boolean synchronizedSuccessful) {
        this.synchronizedSuccessful = synchronizedSuccessful;
    }

    private Boolean synchronizedSuccessful;

    public String getSynchronizedMessage() {
        return synchronizedMessage;
    }

    public void setSynchronizedMessage(String synchronizedMessage) {
        this.synchronizedMessage = synchronizedMessage;
    }

    private String synchronizedMessage;
}
