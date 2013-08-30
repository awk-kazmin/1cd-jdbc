/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spb.awk.driver.for1c.jdbc;

import java.sql.DriverPropertyInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Василий Казьмин
 */
public class Config1C {
    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;
    private static Logger logger;
    private static final List<String> tableTypes;
    static {
        logger = Logger.getGlobal();
        tableTypes = new ArrayList<>();
        tableTypes.add("TABLE");
        tableTypes.add("SYSTEM TABLE");
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * @param aLogger the logger to set
     */
    public static void setLogger(Logger aLogger) {
        logger = aLogger;
    }

    static DriverPropertyInfo[] getDriverPropertyInfo() {
        Pragma[] pragma = Pragma.values();
        DriverPropertyInfo[] result = new DriverPropertyInfo[pragma.length];
        int index = 0;
        for (Pragma p : Pragma.values()) {
            DriverPropertyInfo di = new DriverPropertyInfo(p.pragmaName, null);
            di.choices = p.choices;
            di.description = p.description;
            di.required = false;
            result[index++] = di;
        }

        return result;
    }

    static String getDriverVersion() {
        return "" + MAJOR_VERSION + "." + MINOR_VERSION;
    }

    static String getDriverName() {
        return Driver1C.class.getName();
    }

    static List<String> getTableTypes() {
        return tableTypes;
    }

    
    private static enum Pragma {

        // Parameters requiring SQLite3 API invocation
        OPEN_MODE("open_mode", "Database open-mode flag", null),
        //SHARED_CACHE("shared_cache", "Enable SQLite Shared-Cache mode, native driver only", OnOff),
        //LOAD_EXTENSION("enable_load_extension", "Enable SQLite load_extention() function, native driver only", OnOff),

        // Pragmas that can be set after opening the database
        //CACHE_SIZE("cache_size"),
        //CASE_SENSITIVE_LIKE("case_sensitive_like", OnOff),
        //COUNT_CHANGES("count_changes", OnOff),
        //DEFAULT_CACHE_SIZE("default_cache_size"),
        //EMPTY_RESULT_CALLBACKS("empty_result_callback", OnOff),
        //ENCODING("encoding", toStringArray(Encoding.values())),
        //FOREIGN_KEYS("foreign_keys", OnOff),
        //FULL_COLUMN_NAMES("full_column_names", OnOff),
        //FULL_SYNC("fullsync", OnOff),
        //INCREMENTAL_VACUUM("incremental_vacuum"),
        //JOURNAL_MODE("journal_mode", toStringArray(JournalMode.values())),
        //JOURNAL_SIZE_LIMIT("journal_size_limit"),
        //LEGACY_FILE_FORMAT("legacy_file_format", OnOff),
        //LOCKING_MODE("locking_mode", toStringArray(LockingMode.values())),
        PAGE_SIZE("page_size"),
        MAX_PAGE_COUNT("max_page_count");
        //READ_UNCOMMITED("read_uncommited", OnOff),
        //RECURSIVE_TRIGGERS("recursive_triggers", OnOff),
        //REVERSE_UNORDERED_SELECTS("reverse_unordered_selects", OnOff),
        //SHORT_COLUMN_NAMES("short_column_names", OnOff),
        //SYNCHRONOUS("synchronous", toStringArray(SynchronousMode.values())),
        //TEMP_STORE("temp_store", toStringArray(TempStore.values())),
        //TEMP_STORE_DIRECTORY("temp_store_directory"),
        //USER_VERSION("user_version"),

        // Others
        //TRANSACTION_MODE("transaction_mode", toStringArray(TransactionMode.values())),
        //DATE_PRECISION("date_precision", "\"seconds\": Read and store integer dates as seconds from the Unix Epoch (SQLite standard).\n\"milliseconds\": (DEFAULT) Read and store integer dates as milliseconds from the Unix Epoch (Java standard).", toStringArray(DatePrecision.values())),
        //DATE_CLASS("date_class", "\"integer\": (Default) store dates as number of seconds or milliseconds from the Unix Epoch\n\"text\": store dates as a string of text\n\"real\": store dates as Julian Dates", toStringArray(DateClass.values())),
        //DATE_STRING_FORMAT("date_string_format", "Format to store and retrieve dates stored as text. Defaults to \"yyyy-MM-dd HH:mm:ss.SSS\"", null),
        //BUSY_TIMEOUT("busy_timeout", null);

        public final String   pragmaName;
        public final String[] choices;
        public final String   description;

        private Pragma(String pragmaName) {
            this(pragmaName, null);
        }

        private Pragma(String pragmaName, String[] choices) {
            this(pragmaName, null, choices);
        }

        private Pragma(String pragmaName, String description, String[] choices) {
            this.pragmaName = pragmaName;
            this.description = description;
            this.choices = choices;
        }
    }
}
