package com.omgproduction.dsport_application.utils;

import org.json.JSONException;

/**
 * Created by Florian on 03.03.2017.
 */

public interface Converter<I,O> {
    O convert( I input);
}
