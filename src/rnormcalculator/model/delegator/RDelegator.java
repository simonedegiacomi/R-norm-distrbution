package rnormcalculator.model.delegator;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * The class that actually perform the delegation of the function calls to R.<br>
 * To create a new instance, use {@link RDelegator#createFromEnvironment()}.
 */
public class RDelegator {

    private DelegatorConfig params;

    /**
     * This method creates a new instance of the delegator, loaded with the configuration suitable to the operating
     * system that is currently being used.
     */
    public static RDelegator createFromEnvironment() {
        return new RDelegator(DelegatorConfig.createFromEnvironment());
    }

    private RDelegator(DelegatorConfig params) {
        this.params = params;
    }

    /**
     * Delegates the execution of the R instruction to the R environment. This happens by creating a temporary file
     * and making the R interpret that file. This method perform the operation synchronously, so it might be a good
     * bad to execute it in the main thread.
     * @param instructionToDelegate the instruction that will be executed in the R environment. It must return the
     *                              result as a double.
     * @return the result of the instruction, return from R
     * @throws IOException something bad has happened
     * @throws InvalidResponseException in case R did not respond with a numeric output. Probably because the
     * instruction is malformed.
     */
    public double delegate(String instructionToDelegate) throws IOException, InvalidResponseException {
        //Creating temp file
        File tempFile = File.createTempFile("r-norm-calculator", "r");
        FileUtils.writeStringToFile(tempFile, instructionToDelegate, Charset.defaultCharset());

        //Generating the builder of the process
        ProcessBuilder builder = params.generateProcessBuilder(tempFile);
        Process process = builder.start();

        //Trying to read the result
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            //No interruptions should happen here
            e.printStackTrace();
        }

        //Here we are reading what the process returned us. In Windows (and probably all the other OSs) it returns this:
        //"[1] 0.02274985"
        //The result cannot be just taken and converted to double, we have to get rid off the "[1] " at the beginning.
        //The particular format of the response allows us to check whenever the response that has been returned is valid
        //or whenever an error has happened. We do this kind of check here:
        String dirtyResult = reader.readLine();
        if(isDirtyResultValid(dirtyResult)){
            return parseDirtyResult(dirtyResult);
        } else {
            throw new InvalidResponseException(dirtyResult);
        }
    }

    /**
     * @return true if the result is in "[1] 0.02274985" format, false otherwise
     */
    private boolean isDirtyResultValid(String dirtyResult) {
        return dirtyResult.matches("\\[[0-9]] [0-9]+\\.?[0-9]+");
    }

    private Double parseDirtyResult(String dirtyResult) {
        return Double.valueOf(dirtyResult.substring(4));
    }

}
