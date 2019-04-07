package com.jcg.maven;

import com.google.gson.Gson;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.Security;
import java.util.LinkedHashMap;
import java.util.Map;


public class App{
    public static void main(String args[]) throws Exception {
        App app = new App();
       // app.sendPost();
        String url = "https://http-hunt.thoughtworks-labs.net/challenge/input";
        HttpURLConnection httpsCon =app.getConnection("GET",null,null,url,null,null);
        String response =  new App().readStream(httpsCon.getInputStream());
        System.out.println(response);
        Example example = new Gson().fromJson(response,Example.class);
        String result = "true";//app.performtask(example);
        System.out.println(result);
        System.out.println("\nContent at " + url);
        System.out.println("Response Message is " + httpsCon.getResponseMessage());

        Map<Character,Integer> hMap = app.countVowel(example.getText());
        Exaample2 exaample2 = new Exaample2();
       // exaample2.setCount(result);
        String json = new Gson().toJson(exaample2,Exaample2.class);
        System.out.println(hMap);
        String url2 = "https://http-hunt.thoughtworks-labs.net/challenge/output";
        HttpURLConnection httpsCon2 =app.getConnection("POST","wordCount",result,url2,json,hMap);
        String response2 =  new App().readStream(httpsCon2.getInputStream());
        System.out.println(response2);
        System.out.println("\nContent at " + url2);
        System.out.println("Response Message is " + httpsCon2.getResponseMessage());
    }





    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }







    private HttpsURLConnection getConnection(String requestType,String paramName,String paramValue,String targetUrl, String json,Map<Character,Integer> hMap) throws Exception{

        System.setProperty("javax.net.ssl.trustStore", "/usr/lib/jvm/jdk1.6.0_32/jre/lib/security/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        //TrustStore..
        char[] passphrase = "changeit".toCharArray(); //password
        KeyStore keystore = KeyStore.getInstance("JKS");
        //KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream("C:\\Program Files\\Java\\jdk1.8.0_191\\jre\\lib\\security\\cacerts"), passphrase); //path

        //TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //instance
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keystore);


        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(null, trustManagers, null);
        SSLSocketFactory sf = context.getSocketFactory();
        URL url = new URL(targetUrl);

        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
        httpsCon.setSSLSocketFactory(sf);
        httpsCon.setRequestMethod(requestType);
        httpsCon.setRequestProperty("userId","CeDOAx1t0");

        if(paramValue!= null){
            httpsCon.setRequestProperty("content-type","application/json");
            // Send post request
            httpsCon.setDoOutput(true);
          //  int i = Integer.parseInt(paramValue);
            System.out.println(hMap);
            DataOutputStream wr = new DataOutputStream(httpsCon.getOutputStream());
            json = "{\"a\":" + hMap.get('A') + ",\"e\":"+ hMap.get('E') +",\"i\":"+ hMap.get('I') +",\"o\":"+ hMap.get('O') +",\"u\":" + hMap.get('U') +
                    "}";
            System.out.println(json);
            wr.writeBytes(json);
            wr.flush();
            wr.close();
        }

        return httpsCon;
    }









    private String performtask(Example example) {
        String result;

        Integer i = new Integer(example.getText().replaceAll("\\?",".").split("\\.").length);
        result = i.toString();
        return result;
    }
    private Map<Character,Integer> countVowel(String str){
        String str2 = str.toUpperCase();
        LinkedHashMap<Character, Integer> hMap = new LinkedHashMap();
        hMap.put('A', 0);
        hMap.put('E', 0);
        hMap.put('I', 0);
        hMap.put('O', 0);
        hMap.put('U', 0);
        for (int i = 0; i <= str2.length() - 1; i++) {
            if (hMap.containsKey(str2.charAt(i))) {
                int count = hMap.get(str2.charAt(i));
                hMap.put(str2.charAt(i), ++count);
            }
        }
        System.out.println(hMap);
        return hMap;
    }


}