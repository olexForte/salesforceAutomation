package logging;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LazyFileAppender<E> extends FileAppender<E> {

    @Override
    public void openFile(String file_name) throws IOException {
        lock.lock();
        try {
            File file = new File(file_name);
            boolean result = FileUtil.createMissingParentDirectories(file);
            if (!result) {
                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }

            LazyFileOutputStream lazyFos = new LazyFileOutputStream(file, append);
            setOutputStream(lazyFos);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void append(E eventObject) {
        byte[] byteArray = this.encoder.encode(eventObject);
        System.out.print(new String(byteArray, StandardCharsets.UTF_8));
        super.append(eventObject);
    }
}
