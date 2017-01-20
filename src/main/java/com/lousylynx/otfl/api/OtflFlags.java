package com.lousylynx.otfl.api;

public class OtflFlags {

    public static class Registration {
        public static final int DONT_OVERRIDE = 1; // 0001
        public static final int USE_GAMEDATA = 2; // 0010
        public static final int USE_FOUND = 4; // 0100
        public static final int ALL_OPTS = 15; // 1111
    }
}
