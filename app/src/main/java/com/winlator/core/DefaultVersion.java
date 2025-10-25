package com.winlator.core;

import static com.winlator.container.Container.STEAM_TYPE_NORMAL;

import com.winlator.container.Container;

public abstract class DefaultVersion {
    public static final String BOX86 = "0.3.2";
    public static final String BOX64 = "0.3.7";
    public static final String BOX64_GLIBC = "0.3.6";
    public static final String BOX64_BIONIC = "0.3.7";
    public static final String FEXCORE = "2507";
    public static final String WRAPPER = "System";
    public static final String TURNIP = "25.2.0";
    public static final String ZINK = "22.2.5";
    public static final String VIRGL = "23.1.9";
    public static String DXVK = "2.6.1-gplasync";
    public static final String D8VK = "1.0";
    public static String VKD3D = "2.14.1";
    public static final String CNC_DDRAW = "6.6";
    public static final String VORTEK = "2.1-22.2.5";
    public static final String ADRENO = "819.2";
    public static final String SD8ELITE = "800.51";
    public static String STEAM_TYPE = STEAM_TYPE_NORMAL;
    public static String VARIANT = Container.GLIBC;
    public static String WINE_VERSION = com.winlator.core.WineInfo.MAIN_WINE_VERSION.identifier();

    /**
     * Get the appropriate Box64 version for the given container variant.
     * @param containerVariant The container variant (Container.GLIBC or Container.BIONIC)
     * @return The appropriate Box64 version
     */
    public static String getBox64Version(String containerVariant) {
        if (Container.GLIBC.equals(containerVariant)) {
            return BOX64_GLIBC;
        } else if (Container.BIONIC.equals(containerVariant)) {
            return BOX64_BIONIC;
        } else {
            // Fallback to the original constant for backward compatibility
            return BOX64;
        }
    }
}
