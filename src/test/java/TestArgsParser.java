import org.junit.Assert;
import org.junit.Test;
import project1.ArgsParser;

import java.io.IOException;

public class TestArgsParser {

    @Test
    public void testValidMandatory() {
        // Test string array with the 2 mandatory args in a valid form
        String expectedFilename = "testName.dot";
        String expectedNumStr = "2";
        int expectedNum = 2;
        String[] args = {expectedFilename, expectedNumStr};

        // Attempt to construct argsParser obj with minimum args
        try {
            ArgsParser argsParser = new ArgsParser(args);
            // Two mandatory arguments successfully parsed
            Assert.assertEquals(expectedFilename, argsParser.getInputFilename());
            Assert.assertEquals(expectedNum, argsParser.getProcessorCount());
        } catch (IOException e) {
            // Should not be reached based upon this input
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testDefaultArgs() {
        String[] args = {"filename.dot", "2"};

        int expectedNum = 1;
        boolean expectedVisualise = false;
        String expectedOutputName = "filename-output.dot";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            // Check that default values are properly assigned
            Assert.assertEquals(expectedNum, argsParser.getParallelCoreCount());
            Assert.assertEquals(expectedOutputName, argsParser.getOutputFilename());
            Assert.assertEquals(expectedVisualise, argsParser.isVisualise());
        } catch (IOException e) {
            // Should not be reached based upon this input
            Assert.fail("No exception should be thrown");
        }

    }

    @Test
    public void testParallelCoreParse() {
        String[] args = {"test.dot", "2", "-p", "4"};
        int expectedCoreCount = 4;

        try {
            ArgsParser argsParser = new ArgsParser(args);
            // Checks that core count was recognised and parsed properly
            Assert.assertEquals(expectedCoreCount, argsParser.getParallelCoreCount());
        } catch (IOException e) {
            // Should not be reached based upon this input
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testVisualiseParse() {
        String[] args = {"test.dot", "2", "-v"};
        boolean expectedVisualise = true;

        try {
            ArgsParser argsParser = new ArgsParser(args);
            // Checks that visualisation flag was recognised and set appropriately
            Assert.assertEquals(expectedVisualise, argsParser.isVisualise());
        } catch (IOException e) {
            // Should not be reached based upon this input
            Assert.fail("No exception should be thrown");
        }
    }

    @Test
    public void testManualOutputName() {
        String[] args = {"test.dot", "2", "-o", "differentName"};
        String expectedOutputFilename = "differentName.dot";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            // Checks that output filename flag was recognised and new name was set appropriately
            Assert.assertEquals(expectedOutputFilename, argsParser.getOutputFilename());
        } catch (IOException e) {
            // Should not be reached based upon this input
            Assert.fail("No exception should be thrown");
        }

    }

    @Test
    public void testMissingMandatoryArg() {
        // args too small, i.e only 1 arg
        String[] args = {"test.dot"};
        String expectedMessage = "Mandatory arguments missing. Usage: java -jar scheduler.jar INPUT.dot P [OPTION]. See README for more OPTION choices.";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testProcessorsNotInt() {
        // processor count not given as a parsable int
        String[] args = {"test.dot", "Five"};
        String expectedMessage = "Processor count P not given as an integer";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testDotExtensionMissing() {
        // the .dot extension missing in filename
        String[] args = {"test", "2"};
        String expectedMessage = "DOT_FILE missing .dot extension in command line argument";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testUnknownArgSupplied() {
        // unknown arg given
        String[] args = {"test.dot", "2", "-z"};
        String expectedMessage = "Supplied argument does not match any known optional args";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCoreCountNotInt() {
        // core count is not a parsable to an integer
        String[] args = {"test.dot", "2", "-p", "NotInteger"};
        String expectedMessage = "Parallel core count (for -p) not given as an integer";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testIncompleteOptionalArg() {
        // optional args -o or -p missing subsequent arg
        String[] args = {"test.dot", "2", "-p"};
        String expectedMessage = "No arg given following at least one optional arg -p or -o";

        try {
            ArgsParser argsParser = new ArgsParser(args);
            Assert.fail("An IOException should be thrown");

        } catch (IOException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

}
