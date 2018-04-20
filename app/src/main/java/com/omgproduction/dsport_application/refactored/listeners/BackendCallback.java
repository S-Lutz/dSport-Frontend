package com.omgproduction.dsport_application.refactored.listeners;


import com.omgproduction.dsport_application.refactored.connection.ErrorResponse;

import java.util.Map;

public interface BackendCallback<T>{
    void onSuccess(T result, Map<String, String> responseHeader);
    void onFailure(ErrorResponse error);
}