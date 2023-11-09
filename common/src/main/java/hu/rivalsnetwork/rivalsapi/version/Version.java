package hu.rivalsnetwork.rivalsapi.version;

import java.util.HashMap;

public enum Version {
    v1_20_R2(764, "v1_20_R2"),
    v1_20(763, "v1_20_R1"),
    v1_19_4(762, "v1_19_R3"),
    v1_19_3(761, "v1_19_R2"),
    v1_19_2(760, "v1_19_R1"),
    v1_19(759, "v1_19_R2"),
    v1_18_2(758, "v1_18_R2"),
    v1_18_1(757, "v1_18_R1"),
    v1_18(757, "v1_18_R1"),
    v1_17_1(756, "v1_17_R2"),
    v1_17(755, "v1_17_R1"),
    UNKNOWN(-1, "");
    static final HashMap<Integer, Version> versions = new HashMap<>();
    public final int versionNumber;
    public final String packageName;

    Version(int versionNumber, String packageName) {
        this.versionNumber = versionNumber;
        this.packageName = packageName;
    }

    static {
        for (Version version : values()) {
            versions.put(version.versionNumber, version);
        }
    }

    public static Version getVersionByID(int id) {
        Version version = versions.get(id);
        return version == null ? UNKNOWN : version;
    }
}
