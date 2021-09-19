package com.joaosimonassi.obscurrentscene;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UdpSocket {

    public static DatagramSocket socket;
    static SocketCallBacks callbacks;
    public static boolean is_live = true;
    public static boolean initConnection(String ip, String port, SocketCallBacks c){
        try{
            socket = new DatagramSocket();
            callbacks = c;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String messageStr = "add_client";
                        int server_port = Integer.parseInt(port);
                        InetAddress local = InetAddress.getByName(ip);
                        int msg_length = messageStr.length();
                        byte[] message = messageStr.getBytes();

                        DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                        socket.send(p);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            //Listen response
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //TODO: Matar esse cara depois
                    while(is_live){
                        try {
                            byte[] message = new byte[1024];
                            DatagramPacket datagram = new DatagramPacket(message,message.length );
                            socket.receive(datagram);
                            Log.d("DATAGRAMA", "RECEBI ESSE CARA AQUI" + datagram.toString());
                            String response = new String(datagram.getData(), StandardCharsets.UTF_8);
                            if(response.contains("initial_response")){
                                String filter1 = response.substring(response.indexOf('[') + 1, response.indexOf(']'));
                                String[] values = filter1.split(",");
                                List<String> listResponse = new ArrayList<>();
                                for (String item : values) {
                                    item = item.replace("'", "");
                                    listResponse.add(item);
                                }
                                callbacks.onAddComplete(listResponse);
                            }else{
                                callbacks.onEvent(response.substring(response.indexOf('[') + 1, response.indexOf(']')));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();

        }
        return true;
    }

}
