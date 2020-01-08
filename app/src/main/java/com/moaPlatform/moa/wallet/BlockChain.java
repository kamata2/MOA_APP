package com.moaPlatform.moa.wallet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import m.common.moa.api.Moa020Client;
import m.common.model.MoaTransaction;
import m.common.sdk.api.SDKAPI;
import m.common.sdk.api.moa.CustomApiImpl;
import m.common.sdk.api.moa.RPCapi;

public class BlockChain {
    private Moa020Client blockChainClient;
    private SDKAPI sdkApi;
    private MoaTransaction moaTransaction;

    private BlockChain(Builder builder) {
        String host = builder.host;
        int port = builder.port;
        RPCapi rpcApi = map -> {
            String msg;
            HttpURLConnection con;
            try {
                URL url = new URL("http://"+host+":"+port);
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Accept", "application/json,text/plain");
                con.setRequestProperty("Method", "POST");
                OutputStream os = con.getOutputStream();

                StringBuilder postData = new StringBuilder();

                for (Map.Entry<String, String> param : map.entrySet()) {
                    if (postData.length() != 0)
                        postData.append("&");
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append("=");
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                os.write(postData.toString().getBytes(StandardCharsets.UTF_8));
                os.close();
                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                } else {
                    throw new Exception("http err code:" + HttpResult);
                }
                msg = sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return msg;
        };

        sdkApi = new CustomApiImpl(rpcApi);
        blockChainClient = new Moa020Client();
        blockChainClient.init(sdkApi);
    }

    public Moa020Client getBlockChainClient() {
        return blockChainClient;
    }

    public SDKAPI getSdkApi() {
        return sdkApi;
    }

    public MoaTransaction getMoaTransaction() {
        return moaTransaction;
    }

    public void setMoaTransaction(MoaTransaction moaTransaction) {
        this.moaTransaction = moaTransaction;
    }

    public static class Builder {
        private String host;
        private int port;

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public BlockChain build() {
            return new BlockChain(this);
        }
    }
}
