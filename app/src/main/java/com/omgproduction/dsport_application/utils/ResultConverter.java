package com.omgproduction.dsport_application.utils;

/**
 * Created by Florian on 15.03.2017.
 */

public interface ResultConverter<Input, Output> {
    Output convert(Input input);
}
