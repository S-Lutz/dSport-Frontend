package com.omgproduction.dsport_application.config;

/**
 * Created by Florian on 08.12.2016.
 */

public interface ErrorCodes {
    String SOMETHING_WENT_WRONG = "e0";
    String PASSWORD_MISSMATCH = "e1";
    String FIELD_EMPTY = "e2";
    String INVALID_EMAIl = "e3";
    String ACCEPT_AGB = "e4";
    String NO_CAMERA = "e5";
    String NO_CROP = "e6";
    String NO_CANGES = "e7";

    String BACKEND_CONNECTION_FAILED = "e100";

    String DATABASE_CONNECTION_FAILED = "e300";
    String USERNAME_ALREADY_EXISTS = "e301";
    String EMAIL_ALREADY_EXISTS = "e302";
    String USERNAME_OR_PASSWORD_WRONG = "e303";
    String USER_NOT_FOUND = "e304";

    String BACKEND_SOMETHING_WENT_WRONG = "e599";
}
