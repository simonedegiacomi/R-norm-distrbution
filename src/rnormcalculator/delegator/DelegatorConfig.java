package rnormcalculator.delegator;



import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The delegator config holds the information about where to find the R installation path and how to create a
 * {@link ProcessBuilder} instance in a way proper to the operative system that's being used.
 */
abstract class DelegatorConfig {

    public static final String CONFIG_FILE_NAME = "config.json";

    /**
     * Creates a new instance of the delegator config appropriate for the operative system in use.
     * @throws ConfigurationInvalidException if the loaded configuration would prevent this software from working properly
     */
    public static DelegatorConfig createFromEnvironment() throws ConfigurationInvalidException {
        JSONObject configurationJson = readConfigurationFile();

        DelegatorConfig config = createConfigFromOS(configurationJson);
        config.checkConfigurationIsValid();
        return config;
    }

    private static JSONObject readConfigurationFile() {
        final String currentDir = System.getProperty("user.dir");
        File configurationFile = new File(currentDir+"\\"+CONFIG_FILE_NAME);
        if (configurationFile.exists()) {
            try {
                String configFileContents = FileUtils.readFileToString(configurationFile, Charset.defaultCharset());
                return new JSONObject(configFileContents);
            } catch (IOException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        } else {
            return new JSONObject();
        }
    }

    private static DelegatorConfig createConfigFromOS(JSONObject configurationJson) throws ConfigurationInvalidException {
        DelegatorConfig config;
        if (SystemUtils.IS_OS_WINDOWS) {
            config = new WindowsConfig(configurationJson);
        } else if (SystemUtils.IS_OS_MAC) {
            config = new MacOsConfig(configurationJson);
        } else if (SystemUtils.IS_OS_LINUX) {
            config = new LinuxConfig(configurationJson);
        } else {
            //TODO: it would be nice to have a "GenericOSConfig" that just reads the data from the config file and still
            //tries to deal with it.
            throw new ConfigurationInvalidException("Sistema operativo non supportato!");
        }
        return config;
    }

    /**
     * Checks if this configuration is valid. The check might perform verifying if the executable exists, it's working
     * etc.. The type of tests that need to be performed depend on the operative system of the user.<br>
     * In case the actual configuration is invalid, it mus throw a {@link ConfigurationInvalidException}.
     * @throws ConfigurationInvalidException thrown if the configuration is invalid.
     */
    protected abstract void checkConfigurationIsValid() throws ConfigurationInvalidException;

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

        WindowsConfig(JSONObject configurationJson) {
            String executablePath = configurationJson.optString("executable-path");
            if (executablePath.equals("")) {
                rPath = defaultRPath;
            } else {
                rPath = executablePath;
            }
        }

        @Override
        protected void checkConfigurationIsValid() throws ConfigurationInvalidException {
            File rRunnable = new File(rPath);
            if (!rRunnable.exists()) {
                throw new ConfigurationInvalidException(
                    "Impossibile trovare RScript.exe! L'eseguibile è stato cercato al seguente percorso: \n" + rPath + "\n" +
                        "Nel caso R sia stato installato in una cartella diversa, puoi indicarla nel file di " +
                        "configurazione che si trova nella cartella di questo eseguibile."
                );
            }
        }

        @Override
        public ProcessBuilder generateProcessBuilder(File tempFile) {
            //For some reason it's necessary to include the path to the R executable in double quotes, otherwise java
            //won't be able to find the path (probably an issue with folder having space characters).
            String command = String.format("\"%s\" \"%s\"", rPath, tempFile);
            return new ProcessBuilder(command);
        }

    }

    /**
     * Configuration that is specified to mac os
     */
    private static class MacOsConfig extends DelegatorConfig {

        private static final String defaultRPath = "/usr/local/bin/R";

        private final String rPath;


        MacOsConfig (JSONObject configurationJson) {
            String executablePath = configurationJson.optString("executable-path");
            if (executablePath.equals("")) {
                rPath = defaultRPath;
            } else {
                rPath = executablePath;
            }
        }

        @Override
        protected void checkConfigurationIsValid() throws ConfigurationInvalidException {
            File rRunnable = new File(rPath);
            if (!rRunnable.exists()) {
                throw new ConfigurationInvalidException( "Impossibile trovare RScript! L'eseguibile è stato cercato al seguente percorso: \n" + rPath + "\n" +
                        "Nel caso R sia stato installato in una cartella diversa, puoi indicarla nel file di " +
                        "configurazione che si trova nella cartella di questo eseguibile.");
            }
        }

        @Override
        public ProcessBuilder generateProcessBuilder(File tempFile) {
            // Specify arguments in different strings, otherwise java can't start r.
            // The 'slave' is used to force r to print only the result, omitting the welcome message and
            // the echo of the operation in the file
            return new ProcessBuilder(rPath, "--slave", "-f", tempFile.getAbsolutePath());
        }
    }

    /**
     * Configuration that is specified for linux
     */
    private static class LinuxConfig extends DelegatorConfig {

        private static final String defaultRPath = "/usr/bin/R";

        private final String rPath;


        LinuxConfig (JSONObject configurationJson) {
            String executablePath = configurationJson.optString("executable-path");
            if (executablePath.equals("")) {
                rPath = defaultRPath;
            } else {
                rPath = executablePath;
            }
        }

        @Override
        protected void checkConfigurationIsValid() throws ConfigurationInvalidException {
            File rRunnable = new File(rPath);
            if (!rRunnable.exists()) {
                throw new ConfigurationInvalidException( "Impossibile trovare RScript! L'eseguibile è stato cercato al seguente percorso: \n" + rPath + "\n" +
                        "Nel caso R sia stato installato in una cartella diversa, puoi indicarla nel file di " +
                        "configurazione che si trova nella cartella di questo eseguibile.");
            }
        }

        @Override
        public ProcessBuilder generateProcessBuilder(File tempFile) {
            // Specify arguments in different strings, otherwise java can't start r.
            // The 'slave' is used to force r to print only the result, omitting the welcome message and
            // the echo of the operation in the file
            return new ProcessBuilder(rPath, "--slave", "-f", tempFile.getAbsolutePath());
        }
    }
}
