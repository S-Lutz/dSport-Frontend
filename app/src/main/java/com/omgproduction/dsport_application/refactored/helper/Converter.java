package com.omgproduction.dsport_application.refactored.helper;



public interface Converter<I,O> {
    O convert( I input);
}