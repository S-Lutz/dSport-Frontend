package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 06.03.2017.
 */

public interface ConnectionErrorCodes {

    String BACKEND_CONNECTION_FAILED_ERROR = "e100";

    String DATABASE_CONNECTION_FAILED_ERROR = "e300";
    String USERNAME_ALREADY_EXISTS_ERROR = "e301";
    String EMAIL_ALREADY_EXISTS_ERROR = "e302";
    String USERNAME_OR_PASSWORD_WRONG_ERROR = "e303";
    String USER_NOT_FOUND_ERROR = "e304";
    String ACCESS_DENIED = "e305";

    String BACKEND_SOMETHING_WENT_WRONG_ERROR = "e599";
}
