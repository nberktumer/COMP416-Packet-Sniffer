package config;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Constants {
    public static final boolean DEBUG_MODE = true;

    public static final int RETRY_INTERVAL = 3000;

    public static final String SUBMIT = "submit";
    public static final String GET = "get";

    public static final String OK = "OK";
    public static final String ERROR = "error";

    public static final ArrayList<String> COMMANDS = new ArrayList<>(Arrays.asList(SUBMIT, GET));
}
