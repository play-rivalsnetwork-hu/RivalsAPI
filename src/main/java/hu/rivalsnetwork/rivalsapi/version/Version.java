package hu.rivalsnetwork.rivalsapi.version;

import java.util.HashMap;

public enum Version {
    v1_19_4(762),
    v1_19_3(761),
    v1_19_2(760),
    v1_19(759),
    v1_18_2(758),
    v1_18_1(757),
    v1_18(757),
    v1_17_1(756),
    v1_17(755);
    static final HashMap<Integer, Version> versions = new HashMap<>();
    public final int versionNumber;

    Version(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    static {
        for (Version version : values()) {
            versions.put(version.versionNumber, version);
        }
    }

    public static Version getVersionByID(int id) {
        return versions.get(id);
    }
}
