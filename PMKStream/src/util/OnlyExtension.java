package util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Evandrino Barros, Jônatas Tonholo
 * @version 2.0
 */
public class OnlyExtension implements FilenameFilter {
    private String ext;

    public OnlyExtension (String ext) {
        this.ext = "." + ext;
    }

    public boolean accept(File dir, String name) {
        return name.endsWith(ext);
    }

    /**
     * Gets the Extension
     * @return ext
     */
    public String getExt() {
        return ext;
    }
}
