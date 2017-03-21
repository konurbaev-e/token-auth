package org.konurbaev.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Constants {
    public static final List<String> publicResources = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
            "/js/",
            "/css/",
            "/index",
            "/index.html",
            "/api/login"
    )));
}
