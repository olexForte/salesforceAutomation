package web;

import datasources.FileManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 *  Proxy<br>
 *     Allows to record Requests/Responses in HAR file<br>
 *         User has to startServer first and then create proxy on started server
 */
public class BrowserProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(web.BrowserProxy.class);

    public static ThreadLocal<BrowserProxy> instance = new ThreadLocal<BrowserProxy>();

    public BrowserMobProxy proxyServer;
    private boolean isServerStarted;
    public String currentHarName;

    public static web.BrowserProxy getInstance() {
        if (instance.get() == null) {
            LOGGER.info("Create Proxy");
            instance.set(new web.BrowserProxy());
        }
        return instance.get();
    }


    /**
     * Start proxy server and create HTTP proxy connection
     * @param capabilities
     */
    public void startServer(DesiredCapabilities capabilities) {
        proxyServer = new BrowserMobProxyServer();
        try {
            proxyServer.setTrustAllServers(true);
            proxyServer.start();
            isServerStarted = true;
        } catch (Exception e) {
            throw new RuntimeException("Cant start proxy-server on port: " + proxyServer.getPort(), e);

        }

        Proxy proxy = null;
        try {
            proxy = createHttpProxy(proxyServer.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // configure it as a desired capability
        capabilities.setCapability(CapabilityType.PROXY, proxy);
    }

    /**
     * Start proxy server
     */
    public void startServer(){
        proxyServer = new BrowserMobProxyServer();
        try {

            proxyServer.setTrustAllServers(true);
//            proxyServer.autoAuthorization(ProjectConfiguration.getConfigProperty("URL"),
//                    ProjectConfiguration.getConfigProperty("Username"),
//                    ProjectConfiguration.getConfigProperty("Password"),
//                    AuthType.BASIC);
//
//            String encodedCreadentials = "Basic " + (Base64.getEncoder().encodeToString((ProjectConfiguration.getConfigProperty("Username") + ":" + ProjectConfiguration.getConfigProperty("Password")).getBytes()));
//            proxyServer.addHeader("Authorization", encodedCreadentials);

            proxyServer.start();
            isServerStarted = true;
        } catch (Exception e) {
            throw new RuntimeException("Cant start proxy-server on port: " + proxyServer.getPort(), e);

        }
    }

    /**
     * Create and return proxy connection
     * @return
     */
    public Proxy getProxy(){
        Proxy proxy = null;
        try {
            proxy = createHttpProxy(proxyServer.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return proxy;
    }

    /**
     * Create HTTP proxy
     * @param port
     * @return
     * @throws UnknownHostException possible exception
     */
    private Proxy createHttpProxy(int port) throws UnknownHostException {
        //Proxy proxy = new Proxy();
        Proxy proxy = ClientUtil.createSeleniumProxy(proxyServer);

        String hostIp = Inet4Address.getLocalHost().getHostAddress();
        proxy.setHttpProxy(hostIp+":" + proxyServer.getPort());
        proxy.setSslProxy(hostIp+":" + proxyServer.getPort());

//        proxy.setProxyType(Proxy.ProxyType.MANUAL);
//        String proxyStr = "localhost:" + port;  //String.format("%s:%d", InetAddress.getLocalHost().getCanonicalHostName(),  port);
//        proxy.setHttpProxy(proxyStr);
//        proxy.setSslProxy(proxyStr);
        return proxy;
    }

    /**
     * Stop Proxy server instance
     */
    public static void stopServer() {
        if(instance.get() != null) {
            instance.get().stopProxy();
            instance.set(null);
        }
    }

    /**
     * Stop Proxy server
     */
    public void stopProxy(){
        if (isServerStarted) {
            try {
                proxyServer.stop();
            } catch (Exception e) {
                throw new RuntimeException("Cant stop proxy server", e);
            }
        }
    }

    /**
     * Create new HAR file from proxy
     * @param fileName
     */
    public void createHARFileFromProxy(String fileName) {
        String filename = FileManager.OUTPUT_DIR + File.separator + fileName.replaceAll("\\W","") + ".txt";
        createNewHar(filename);
    }

    /**
     * Notify Proxy Server to create new har
     * @param name
     */
    public void createNewHar(String name){
        currentHarName = name;
        proxyServer.newHar();
    }

    /**
     * Get HAR file from proxy server
     * @return
     */
    public Har getHar() {
        return proxyServer.getHar();
    }


    /**
     * Save HAR file
     * @return
     */
    public String dumpHARFileFromProxy() {
        String fileName = writeHarToFile();
        return fileName;
    }

    /**
     * Save current HAR file from memory to physical file
     * @return
     */
    public String writeHarToFile() {
        String filename = currentHarName;
        try {
            Har har = proxyServer.getHar();
            if (har != null){
                if(har.getLog().getEntries().size() > 0) {
                    FileWriter wr = new FileWriter(new File(filename));
                    wr.write(packEntryToString(har));
                    wr.close();
                } else
                    filename = null;
            } else
                filename = null;
        } catch (Exception e) {
            filename = null;
            e.printStackTrace();
        }
        return filename;
    }

    /**
     * Transform HAR file into human readable form (TODO format of final file should be discussed)
     * @param har
     * @return
     */
    public String packEntryToString(Har har){
        StringBuilder sb = new StringBuilder();
        int numberOfRequests = 0;
        for (HarEntry entry : har.getLog().getEntries()){
            numberOfRequests++;
            String url =entry.getRequest().getUrl();
            if (    !url.contains(".woff") &&
                    !url.contains(".png") &&
                    !url.contains(".gif") &&
                    !url.contains(".ttf") &&
                    !url.contains(".css") &&
                    !url.contains(".ico") &&
                    !url.contains("google") &&
                    !url.contains(".js") ){

                sb.append("==================== " + numberOfRequests + " ==================== ");
                sb.append(entry.getServerIPAddress());
                sb.append("\n");
                sb.append("Request method and URL: " + entry.getRequest().getMethod() + " " + entry.getRequest().getUrl());
                Iterator<HarNameValuePair> iterator = entry.getRequest().getQueryString().iterator();
                while (iterator.hasNext()) {
                    HarNameValuePair item = iterator.next();
                    sb.append(item.getName() + " = " + item.getValue() + "\n");
                }
/*
                sb.append("\n== cookies \n");
                Iterator<HarCookie> iterCookies = entry.getRequest().getCookies().iterator();
                while (iterCookies.hasNext()) {
                    HarCookie cookie = iterCookies.next();
                    sb.append(cookie.getName() + " = " + cookie.getValue() + "\n");
                }
*/
                if (entry.getRequest().getPostData() != null)
                    sb.append(entry.getRequest().getPostData().getText());
                sb.append("\n");
                sb.append("\n");
                sb.append(entry.getResponse().getStatusText());
                sb.append("\n");
                sb.append("Status code: " + entry.getResponse().getStatus());
                sb.append("\n");
                if (entry.getResponse().getContent() != null)
                    sb.append(entry.getResponse().getContent().getText());
/*
                sb.append("\n== cookies \n");
                iterCookies = entry.getResponse().getCookies().iterator();
                while (iterCookies.hasNext()) {
                    HarCookie cookie = iterCookies.next();
                    sb.append(cookie.getName() + " = " + cookie.getValue() + "\n");
                }
               */
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

}
