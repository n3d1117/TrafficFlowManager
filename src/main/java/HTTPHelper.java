import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HTTPHelper {

    public static String doRequest(URL url, Boolean jsonOutput) throws IOException {
        return doRequest(url, "GET", "application/json", jsonOutput,
                false, null, null, new HashMap<>(), null);
    }

    public static String doRequest(URL url, String method,
                                   String contentType,
                                   Boolean jsonOutput,
                                   Boolean usesAuthentication,
                                   String user,
                                   String pass,
                                   Map<String, String> parameters,
                                   String fileToUpload) throws IOException {

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(method);

        // Headers
        con.setRequestProperty("Content-type", contentType);
        if (jsonOutput)
            con.setRequestProperty("Accept", "application/json");

        // Authentication
        if (usesAuthentication) {
            String userPass = user + ":" + pass;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
        }

        // Parameters
        if (!parameters.isEmpty()) {
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(parameters));
            out.flush();
            out.close();
        }

        // File upload
        if (fileToUpload != null) {
            con.setDoOutput(true);
            con.setDoInput(true);
            File file = new File(fileToUpload);
            BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int i;
            while ((i = bis.read()) > -1) {
                bos.write(i);
            }
            bis.close();
            bos.close();
        }

        // Read response
        int status = con.getResponseCode();
        Reader streamReader;
        if (status > 299) {
            System.out.println(status);
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }

    // Source: https://www.baeldung.com/java-http-request
    private static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }
        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }
}
