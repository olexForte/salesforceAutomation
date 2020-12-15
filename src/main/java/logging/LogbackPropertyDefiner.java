package logging;

import datasources.FileManager;
import ch.qos.logback.core.PropertyDefinerBase;

/**
 * Logback property definer (required to make available logs to multiple locations)
 */
public class LogbackPropertyDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return FileManager.OUTPUT_DIR;
    }
}
