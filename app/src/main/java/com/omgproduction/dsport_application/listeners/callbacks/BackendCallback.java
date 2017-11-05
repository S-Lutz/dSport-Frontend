package com.omgproduction.dsport_application.listeners.callbacks;


import com.omgproduction.dsport_application.aaRefactored.connection.ErrorResponse;

import java.util.Map;

public interface BackendCallback<T>{
    void onSuccess(T result, Map<String, String> responseHeader);
    void onFailure(ErrorResponse error);
}