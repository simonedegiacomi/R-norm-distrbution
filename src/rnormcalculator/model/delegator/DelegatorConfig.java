package rnormcalculator.model.delegator;



import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.Locale;

/**
 * The delegator config holds the information about where to find the R installation path and how to create a
 * {@link ProcessBuilder} instance in a way proper to the operative system that's being used.
 */
abstract class DelegatorConfig {

    /**
     * Creates a new instance of the delegator config appropriate for the operative system in use.
     */
    public static DelegatorConfig createFromEnvironment() {
        DelegatorConfig config;

        if (SystemUtils.IS_OS_WINDOWS) {
            config = new WindowsConfig();

        } else {
            //TODO: it would be nice to have a "GenericOSConfig" that just reads the data from the config file and still
            //tries to deal with it.
            throw new RuntimeException("Unknown OS");
        }

        return config;
    }

    /**
     * Creates a new instance of the {@link ProcessBuilder} and initializes in in such a way to make it usable for the
     * {@link RDelegator}.
     * @param tempFile the temporary file where will be written the commands to send to the R executable.
     * @return an initialized {@link ProcessBuilder}
     */
    public abstract ProcessBuilder generateProcessBuilder(File tempFile);

    /**
     * Configuration that is specific to windows computers
     */
    private static class WindowsConfig extends DelegatorConfig {

        private final String rPath;

        private static final String defaultRPath = "C:\\Program Files\\R\\R-3.3.3\\bin\\x64\\Rscript.exe";

        public WindowsConfig() {
            rPath = defaultRPath;
        }

        @Override
        public ProcessBuilder generateProcessBuilder(File tempFile) {
            //For some reason it's necessary to include the path to the R executable in double quotes, otherwise java
            //won't be able to find the path (probably an issue with folder having space characters).
            String command = String.format("\"%s\" \"%s\"", rPath, tempFile);
            return new ProcessBuilder(command);
        }

    }
}
