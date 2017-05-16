package common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collection;

/**
 * Detects the encoding of a {@code File}
 *
 * @author Georgios Migdos
 * @author Frederik Kammel
 */
@SuppressWarnings("unused")
public class CharsetDetector {

    /**
     * Detects the encoding of the specified file
     *
     * @param file The file to guess the encoding from
     * @return The most probable {@code Charset} of the specified file or {@code null} if it could not be detected
     */
    public static Charset detectCharset(File file) {
        return detectCharset(file, Charset.availableCharsets().values());
    }

    /**
     * Detects the encoding of the specified file
     *
     * @param file     The file to guess the encoding from
     * @param charsets The charsets to be taken under consideration
     * @return The most probable {@code Charset} of the specified file or {@code null} if it could not be detected
     */
    public static Charset detectCharset(File file, Collection<Charset> charsets) {

        Charset charset = null;

        for (Charset currentCharset : charsets) {
            charset = detectCharset(file, currentCharset);
            if (charset != null) {
                break;
            }
        }

        return charset;
    }

    private static Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
}