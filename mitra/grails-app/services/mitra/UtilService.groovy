package mitra

import javax.net.ssl.HttpsURLConnection
import java.io.BufferedReader;

class UtilService {

    def getExistingUserFromParams(deviceId) {
        if (!deviceId) {
            return null
        }

        return User.findByDeviceId(deviceId)
    }

    def getUser(String deviceId) {
        def existingUser = getExistingUserFromParams(deviceId)
        if (existingUser) {
            return existingUser
        }
        User user = new User(deviceId:deviceId, updatedOn: new Date())
        user.save(flush: true, failOnError: true)
        return user
    }

    /**
     * This sends a PUSH notif for a TeleMitra to know new request has come
     * @param deviceId
     * @return
     */
    def pushNotif(String deviceId, String notif) {

        String url = "https://api.parse.com/1/push";

        String jsonStr = """{
            "where": {
                "device_id": "${deviceId}"
            },
            "data": {
                "alert": "${notif}"
            }
        }"""

        sendPost(url, jsonStr)
    }

    // HTTP POST request
    public static String sendPost(String url, String paramString) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-Parse-Application-Id", "X0Pd9oaacmx5UXlMbvDc14lrvowK84f2FJTa9P6I")
        con.setRequestProperty("X-Parse-REST-API-Key", "w47C4JOB6yQh80XQvMq58erKzQFiSnRjSe8gVK7a")
        String urlParameters = paramString;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader input= new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();

        //print result
        System.out.println(response.toString());
        return response.toString();

    }

}
