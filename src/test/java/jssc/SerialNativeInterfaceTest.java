package jssc;

import org.junit.Assume;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SerialNativeInterfaceTest {


    @Test
    public void testInitNativeInterface() {
        SerialNativeInterface serial = new SerialNativeInterface();

        long handle = -1;
        try {
            handle = serial.openPort("ttyS0",false);
            assertThat(handle, is(not(-1L)));
        } finally {
            if (handle != -1) {
                serial.closePort(handle);
            }
        }
    }

    @Test
    public void testPrintVersion() {
        try {
            final String nativeLibraryVersion = SerialNativeInterface.getNativeLibraryVersion();
            assertThat(nativeLibraryVersion, is(not(nullValue())));
            assertThat(nativeLibraryVersion, is(not("")));
        } catch (UnsatisfiedLinkError linkError) {
            linkError.printStackTrace();
            fail("Should be able to call method!");
        }

    }

    @Test(expected = java.io.IOException.class)
    public void reportsWriteErrorsAsIOException() throws Exception {
        Assume.assumeFalse(SerialNativeInterface.getOsType() == SerialNativeInterface.OS_WINDOWS);

        long fd = -1; /*bad file by intent*/
        byte[] buf = new byte[]{ 0x6A, 0x73, 0x73, 0x63, 0x0A };
        SerialNativeInterface testTarget = new SerialNativeInterface();
        testTarget.writeBytes(fd, buf);
    }

}
