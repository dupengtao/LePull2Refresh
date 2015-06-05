package com.example.dpt.pulltorefreshdemo.net;


import com.android.volley.*;

public class VolleyErrorHelper {

    public final static String ERROR_SERVER_DOWN = "ERROR_SERVER_DOWN";
    public final static String ERROR_NO_INTERNET = "ERROR_NO_INTERNET";
    public final static String ERROR_OTHERS = "ERROR_OTHERS";

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @return
     */
    public static String getMessage(Object error) {
        if (error instanceof TimeoutError) {
            return ERROR_SERVER_DOWN;
        } else if (isServerProblem(error)) {
            return handleServerError(error);
        } else if (isNetworkProblem(error)) {
            return ERROR_NO_INTERNET;
        }
        return ERROR_OTHERS;
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @return
     */
    private static String handleServerError(Object err) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        // HashMap<String, String> result = new Gson().fromJson(new String(response.data), new
                        // TypeToken<Map<String, String>>() {}.getType());
                        // if (result != null && result.containsKey("error")) {
                        // return result.get("error");
                        // }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return error.getMessage();

                default:
                    return ERROR_NO_INTERNET;
            }
        }
        return ERROR_OTHERS;
    }
}
