/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class KnockKnockClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2 && args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String id = "";
        if(args.length == 3)
            id = args[2];
        boolean firstTime = true;
        
        String max_size = "10";
        String max_power = "10";

        while(true){
            try{
                //First, wait before trying to connect again and request a computation
                double t = Math.random()*10;
                long v = Math.round(t);
                //System.out.println("time: "+t+", in long: "+v);
                TimeUnit.SECONDS.sleep(v);
                try (
                    Socket kkSocket = new Socket(hostName, portNumber);
                    PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));
                ) {
                    BufferedReader stdIn =
                        new BufferedReader(new InputStreamReader(System.in));

                    String fromServer;
                    
                    String msg;

                    while ((fromServer = in.readLine()) != null) {
                        System.out.println("Server: " + fromServer);
                        if(fromServer.charAt(0) == '[')
                            break;
                        if (fromServer.equals("Bye."))
                            break;

                        /*if(firstTime){
                            System.out.print("Please enter the max size of the matrice: ");
                            max_size = stdIn.readLine();

                            System.out.print("Please enter the max difficulty: ");
                            max_power = stdIn.readLine();
                            firstTime = false;
                        }*/

                        if(max_size.equals("") || max_power.equals("")){
                            System.out.println("One of the two values is wrong, retry...");
                            firstTime = true;
                            break;
                        }
                        else if (max_size != null && max_power != null) {
                            //int size = (int) Math.ceil(Math.random()*Integer.parseInt(max_size));
                            int size = 10;
                            double[][] matrice = new double[size][size];

                            //int count = 2;
                            //currently we don't take 0 and 1 into account for the power value
                            int power = (int) Math.ceil(Math.random()*(Integer.parseInt(max_power)-1)+1);

                            msg = id+" "+size+" "+power+" ";
                            for(int i = 0; i < size; i++){
                                for(int j = 0; j < size; j++){
                                    //matrice[i][j] = count;
                                    
                                    matrice[i][j] = Math.ceil(Math.random()*10);
                                    if(j == size-1 && i == size-1){
                                        //End of Matrix
                                        msg = msg+matrice[i][j];
                                    }
                                    else{
                                        msg = msg+matrice[i][j]+",";
                                    }
                                    //count++;

                                }
                            }

                            System.out.println("Message : " + msg);

                            out.println(msg);
                        }
                    }
                } catch (UnknownHostException e) {
                    System.err.println("Don't know about host " + hostName);
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                    System.exit(1);
                }
                catch (Exception e){
                    System.out.println("Error: "+e);
                }
            }
            catch (Exception e){
                System.out.println("Error: "+e);
            }
        }
    }
}