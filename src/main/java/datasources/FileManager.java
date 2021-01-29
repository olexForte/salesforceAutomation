package datasources;

import configuration.ProjectConfiguration;
import configuration.SessionManager;
import reporting.ReporterManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Interaction with file system<br>
 *     This class "knows" all paths and has implementation of basic interactions with file system
 */
public class FileManager {

    // main logger
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    static String MAIN_RESOURCES = "src/main/resources/";
    static String TEST_SCRIPTS = "src/test/automation/scripts/";
    public static String TEST_ACTIONS = "src/test/automation/actions/";
    static String TEST_SUITES = "src/test/automation/suites/";
    public static String TEST_AUTOMATION_RESOURCES = "src/test/automation/resources/";

    //folder in .target with downloaded/created during session files)
    public static String OUTPUT_DIR = getOutputDir();

    /**
     * Create output directory for current test session
     * @return test output dir
     */
    public static String getOutputDir(){
        String directory = System.getProperty("user.dir") + File.separator + "target" + File.separator + SessionManager.getSessionID();
        System.setProperty("OUTPUT_DIR", directory);
        //create output dir
        try {
            createDir(directory);
        } catch (IOException e) {
            LOGGER.error("Fail create DIR"+e);
            return null;
        }
        return directory;
    }

    /**
     * Get content of file as string
     * @param filePath path to file
     * @return file content
     */
    public static String getFileContent(String filePath){
        String result = null;
        try {
            result = FileUtils.readFileToString(new File(filePath).getAbsoluteFile(), StandardCharsets.UTF_8).replace("\r", "");
        } catch (IOException e) {
            LOGGER.error("Fail get contetnt from file path "+filePath+" "+e.toString());
        }
        return result ;
    }

    /**
     * Get content of file as string
     * @param file file object
     * @return file content
     */
    public static String getFileContent(File file){
        String result = null;
        try {
            result = FileUtils.readFileToString(file, StandardCharsets.UTF_8).replace("\r", "");
        } catch (Exception e) {
            LOGGER.error("Fail get content from "+file.getAbsolutePath()+" "+e.toString());
        }
        return result ;
    }

    /**
     * create dir
     * @param path filePath
     * @throws IOException exception
     */
    public static void createDir(String path) throws IOException {
        FileUtils.forceMkdir(new File(path));
    }

    /**
     * Get full file path from Output Directory (folder in .target with downloaded files)
     * @param fileName name of file
     * @return full file location
     */
    public static String getFileFromDownloadDir(String fileName) {
        waitForDownloadedFile(fileName);
        return OUTPUT_DIR + File.separator + fileName;
    }



    /**
     * Wait for file existance
     * @param actualFileLocation path to file
     * @return was file found
     */
    public static boolean waitForFile(String actualFileLocation) {
        boolean result = false;
        boolean timeoutReached = false;
        int currentTime = 0;
        while(!timeoutReached){
            if(Files.exists(Paths.get(actualFileLocation))){
                return true;
            }
            currentTime++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //
            }
            if(currentTime >= Integer.parseInt(ProjectConfiguration.getConfigProperty("DefaultTimeoutInSeconds")))
                timeoutReached = true;
        }
        return result;
    }

    /**
     * Write to file
     * @param dataFile file location
     * @param data data
     * @throws IOException exception
     */
    public static void writeToFile(String dataFile, String data) throws IOException {
        FileUtils.writeStringToFile(new File(dataFile), data, StandardCharsets.UTF_8);
    }

    /**
     * Get file from Resources dir
     * @param fileName name of file in Main resources
     * @return file object
     */
    public static File getFileFromMainResources(String fileName) {
        return new File(MAIN_RESOURCES + fileName );
    }

    /**
     * wait for downloaded file
     * @param filePath file path
     * @return was file downloaded
     */
    public static boolean waitForDownloadedFile(String filePath){
        return waitForFile(filePath);
    }

    /**
     * Archive files to ZIP
     * @param listOfResultsFile list of file locations
     * @return zip file location
     */
    public static String archiveFiles(List<String> listOfResultsFile) {
        String archiveName = SessionManager.getSessionID() + ".zip";

        try (FileOutputStream fos = new FileOutputStream(archiveName);
             ZipOutputStream zipOut = new ZipOutputStream(fos);)
        {
            for (String file : listOfResultsFile) {
                if(file != null) {
                    File fileToZip = new File(file);
                    zipFile(fileToZip, fileToZip.getName(), zipOut);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Fail archive Files");

        }

        return archiveName;
    }

    /**
     * Compress file as ZIP
     * @param fileToZip File object
     * @param fileName zip file name
     * @param zipOut ZipOutputStream object
     * @throws IOException exception
     */
        private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
            if (fileToZip.isHidden()) {
                return;
            }
            if (fileToZip.isDirectory()) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    zipOut.closeEntry();
                } else {
                    zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                    zipOut.closeEntry();
                }
                File[] children = fileToZip.listFiles();
                if(children == null)
                    return;
                for (File childFile : children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
                return;
            }
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

     /**
     * Append to file
     * @param fileName file location
     * @param data data to add
     * @throws IOException exception
     */
    public static void appendToFile(String fileName, String data) throws IOException {
        FileUtils.writeStringToFile(new File(fileName), data, true);
    }

    /**
     * Get file from Scripts dir
     * @param fileName file name
     * @return file object
     */
    public static File getFileFromScriptsDir(String fileName) {
        Collection<File> files = FileUtils.listFiles(new File(TEST_SCRIPTS),
                new String[]{"txt","json","ct"}, true);
        for(File file: files){
            if (file.getName().matches(fileName + "\\.(txt|json|ct)") || file.getName().equals(fileName))
                return file;
        }
        return null;
    }

    /**
     * Get file from Suites dir
     * @param fileName file name
     * @return file object
     */
    public static File getFileFromSuitesDir(String fileName) {
        Collection<File> files = FileUtils.listFiles(new File(TEST_SUITES),
                new String[]{"txt","json","sct"}, true);
        for(File file: files){
            if (file.getName().matches(fileName + "\\.(txt|json|sct)"))
                return file;
        }
        return null;
    }

    /**
     * Get file from Actions dir
     * @param fileName file name
     * @return file object
     */
    public static File getFileFromActionsDir(String fileName) {
        Collection<File> files = FileUtils.listFiles(new File(TEST_ACTIONS),
                new String[]{"txt","json","act"}, true);
        for(File file: files){
            if (file.getName().matches(fileName + "\\.(txt|json|act)"))
                return file;
        }
        return null;
    }

    /**
     * Get files from dir
     * @param dir dir location
     * @param extensions array of extensions
     * @return list of files
     */
    public static Collection<File> getFilesFromDir(String dir, String[] extensions ) {
        ArrayList<String> result = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(new File(dir),
                extensions, true);
        return files;
    }

    /**
     * Get files from dir
     * @param fileName name of file
     * @param dir dir location
     * @return list of files
     */
    public static File getFileFromDir(String fileName, String dir ) {
        ArrayList<String> result = new ArrayList<>();
        IOFileFilter fn = new NameFileFilter(fileName);
        IOFileFilter dn = new NameFileFilter(fileName);
        try {

            return FileUtils.listFiles(new File(dir), new WildcardFileFilter(fileName + ".*"), new WildcardFileFilter("*")).stream().findFirst().get();

            //was
           // return FileUtils.listFiles(new File(dir), new WildcardFileFilter(fileName + "*"), new WildcardFileFilter("*")).stream().findFirst().get();
        }catch (NoSuchElementException e){
            LOGGER.error("File was not found: " + fileName + " in " + dir);
        }
        return null;
    }

    /**
     * Get files from dir
     * @param fileName name of file
     * @param dir dir location
     * @return list of files
     */
    public static Collection<File> getFilesFromDir(String fileName, String dir ) {
        ArrayList<String> result = new ArrayList<>();
        IOFileFilter fn = new NameFileFilter(fileName);
        IOFileFilter dn = new NameFileFilter(fileName);
        try {
            return FileUtils.listFiles(new File(dir), new WildcardFileFilter(fileName + "*"), new WildcardFileFilter("*"));
        }catch (NoSuchElementException e){
            LOGGER.error("File was not found: " + fileName + " in " + dir);
        }
        return null;
    }

    /**
     * Get files from Scripts dir
     * @return files from scripts dir
     */
    public static Collection<File> getFilesFromScriptsDir() {
        return FileUtils.listFiles(new File(TEST_SCRIPTS),
                new String[]{"txt","json","ct"}, true);
    }

    /**
     * Get files from Actions dir
     * @return files from action dir
     */
    public static Collection<File> getFilesFromActionsDir() {
        ArrayList<String> result = new ArrayList<>();
        return FileUtils.listFiles(new File(TEST_ACTIONS),
                new String[]{"txt","json","act"}, true);
    }

    /**
     * Load properties from file
     * @param file property file
     * @return map of properties
     */
    public static Map<String,String> loadProperties(File file){
        HashMap<String,String> result = new HashMap<String,String>();
        //create Properties object
        Properties properties = new Properties();

        try {

            //open input stream to read file
            FileInputStream fileInput = new FileInputStream(file);
            //load properties from file
            properties.load(fileInput);
            //close file
            fileInput.close();


        } catch (FileNotFoundException e) {
            LOGGER.error("File was not found: " + file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Config was not opened: " + file.getAbsolutePath());
        } catch (Exception e){
            LOGGER.error("Fail open file : " + file.getAbsolutePath());
        }

        for(String property : properties.stringPropertyNames()){
            result.put(property.toLowerCase(), properties.getProperty(property));
        }
        return result;
    }

    /**
     * Copy file
     * @param fromFile from location
     * @param toFile to locations
     * @throws IOException exception
     */
    public static void copyFile(String fromFile, String toFile) throws IOException {
        FileUtils.copyFile(new File(fromFile), new File(toFile));
    }

    /**
     * Check if file exists
     * @param file file location
     * @return does file exist
     */
    public static boolean doesExist(String file) {
        return (new File(file)).exists();
    }

    /**
     * Delete file
     * @param file file location
     * @return was file deleted
     */
    public static boolean deleteFile(String file) {
        return (new File(file)).delete();
    }

    /**
     * Replace String in specified file
     * @param fileContent whole file content
     * @param startWith start of line
     * @param newLineValue new value of line
     * @return file content
     */
    public static String replaceStringInFileContent(String fileContent, String startWith, String newLineValue) {
        StringBuilder results = new StringBuilder();
        String[] allLines = fileContent.split("\n");
        boolean alreadyProcessed = false;
        for (String allLine : allLines) {
            if (allLine.startsWith(startWith) && !alreadyProcessed) {
                results.append(results.toString().equals("") ? "" : "\n").append(newLineValue);
                alreadyProcessed = true;
            } else
                results.append(results.toString().equals("") ? "" : "\n").append(allLine);
        }
        return results.toString();
    }

    /**
     * Get file name from string PATH
     * @param filePath file location
     * @return file name with extension
     */
    public static String getFileNameFromPath(String filePath) {
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * Get parent folder name from File string PATH
     * @param filePath file location
     * @return parent dir location
     */
    public static String getParentFolderNameFromPath(String filePath) {
        return filePath.substring(0,filePath.lastIndexOf(File.separator));
    }

    /**
     * get list of lines from file
     * @param file file object
     * @return list of lines from files
     * @throws IOException exception
     */
    public static List<String> getFileContentAsListOfLines(File file) throws IOException {
        return FileUtils.readLines(file, StandardCharsets.UTF_8);
    }

    /**
     * Get line that contains label
     * @param file file
     * @param label substring
     * @return line from file that contains substring
     * @throws IOException exception
     */
    public static String getLineThatContainsSubstring(File file, String label) throws IOException {
        String result = "";
        for(String line : getFileContentAsListOfLines(file)){
            if(line.contains(label))
                return line;
        }
        return result;
    }

    /**
     * Get line that starts with label
     * @param file file
     * @param label substring
     * @return line from file that starts with substring
     * @throws IOException exception
     */
    public static String getLineThatStartsWithSubstring(File file, String label) throws IOException {
        String result = "";
        for(String line : getFileContentAsListOfLines(file)){
            if(line.startsWith(label))
                return line;
        }
        return result;
    }

    /**
     * Create new file with content
     * @param fileLocation file location
     * @param content content of file
     */
    public static void createFile(String fileLocation, String content) throws IOException {
        FileUtils.writeStringToFile(new File(fileLocation), content, StandardCharsets.UTF_8);
    }

    /**
     * Create new file with content
     * @param file target file
     * @param content content of file
     */
    public static void createFile(File file, String content) throws IOException {
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
    }

    /**
     * Get file from Scripts dir
     * @param fileName file name
     * @return file object
     */
    public static File getFileFromDir(String fileName) {
        Collection<File> all = FileUtils.listFiles(new File("."),
                new WildcardFileFilter(fileName),
                new WildcardFileFilter("*"));
        if(all.isEmpty())
            return null;
        else
            return all.stream().findFirst().get();
    }

    /**
     * Simple comparison - use DIFF to see more details TODO diff
     * @param actualFile
     * @param expectedFile
     * @return
     * @throws IOException
     */
    public static String compareFiles(File actualFile, File expectedFile) throws IOException {
        String result = "";
        List<String> actualLines = FileUtils.readLines(actualFile, StandardCharsets.UTF_8);
        List<String> expectedLines = FileUtils.readLines(expectedFile, StandardCharsets.UTF_8);
        for(String actualLine : actualLines){
            String key = actualLine.replaceFirst("^(\\d+\t\\d+\t..).*$","$1");
            boolean wasFound = false;
            for(String expectedLine : expectedLines){
                if(expectedLine.startsWith(key)){
                    String difference = StringUtils.difference(actualLine, expectedLine);
                    if(!difference.equals("")) {
                        result = result + actualLine + "\n";
                        result = result + expectedLine + "\n\n";
                    }
                    wasFound = true;
                    break;
                }
            }
            if(!wasFound){
                result = result + key + " was not found in Expected file" + "\n";
            }
        }
        for(String expectedLine : expectedLines){
            String key = expectedLine.replaceFirst("^(\\d+\t\\d+\t..).*$","$1");
            boolean wasFound = false;
            for(String actualLine : actualLines){
                if(actualLine.startsWith(key)){
                    wasFound = true;
                    break;
                }
            }
            if(!wasFound){
                result = result + key + " was not found in Actual file" + "\n";
            }
        }


        //        String result = null;
//        FileReader af;
//        BufferedReader actual = Files.newBufferedReader(actualFile.toPath());
//        BufferedReader expected = Files.newBufferedReader(expectedFile.toPath());
//        String line1;
//        String line2;
//        int i = 0;
//        while((( line1 = actual.readLine()) != null) && ((line2 = expected.readLine()) != null)){
//            i++;
//            if(!line1.equals(line2)) {
//                LOGGER.error("Failed on Row: " + i);
//                LOGGER.error("Line New: "  + line1);
//                LOGGER.error("Line Old: "  + line2);
//                result = result + "" + "\n";
//            }
//        }
        return result;
    }
    /**
     * Get files from dir
     * @param fileName name of file
     * @param dir dir location
     * @return list of files
     */
    public static File getFileFromDirs(String fileName, String... dir ) {
        ArrayList<String> result = new ArrayList<>();
        IOFileFilter fn = new NameFileFilter(fileName);
        IOFileFilter dn = new NameFileFilter(fileName);

        try {
            return FileUtils.listFiles(new File(dir[0]), new WildcardFileFilter(fileName + "*"), new WildcardFileFilter("*")).stream().findFirst().get();
        }catch (NoSuchElementException e){
            LOGGER.error("File was not found: " + fileName + " in " + dir[0]);
        }

        try {
            return FileUtils.listFiles(new File(dir[1]), new WildcardFileFilter(fileName + "*"), new WildcardFileFilter("*")).stream().findFirst().get();
        }catch (NoSuchElementException e){
            LOGGER.error("File was not found: " + fileName + " in " + dir[1]);
        }

        try {
            return FileUtils.listFiles(new File("."), new WildcardFileFilter(fileName + "*"), null).stream().findFirst().get();
        }catch (NoSuchElementException e){
            LOGGER.error("File was not found: " + fileName + " in current dir");
        }

        return null;
    }
    public static Collection<File> getFilesFromDownloadDir(String data) {
        return FileManager.getFilesFromDir(data, OUTPUT_DIR);
    }
}

