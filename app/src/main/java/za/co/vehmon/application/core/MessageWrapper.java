package za.co.vehmon.application.core;

/**
 * Created by Renaldo on 1/24/2015.
 */
public class MessageWrapper {

    public class MessageResult
    {
        public boolean isSuccessful() {
            return IsSuccessful;
        }

        public void setSuccessful(boolean isSuccessful) {
            IsSuccessful = isSuccessful;
        }

        public String getErrorMessage() {
            return ErrorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            ErrorMessage = errorMessage;
        }

        public boolean IsSuccessful;
        public String ErrorMessage;
    }
}
