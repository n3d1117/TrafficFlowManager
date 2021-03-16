import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HTTPHelper {

    public static Integer doRequest(URL url, String method,
                                   String contentType,
                                   String user,
                                   String pass,
                                   String fileToUpload) throws IOException {
        return doRequest(url, method, contentType, false,
                true, user, pass, fileToUpload);
    }

    public static Integer doRequest(URL url, String method,
                                   String contentType,
                                   Boolean jsonOutput,
                                   Boolean usesAuthentication,
                                   String user,
                                   String pass,
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

        Integer responseCode = con.getResponseCode();

        // Read response
        Reader streamReader;
        if (responseCode > 299) {
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
        if (!content.toString().isEmpty())
            System.out.println(content.toString());

        con.disconnect();
        return responseCode;
    }
}
