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
    public static final int MAX_SIZE = 50;
    public static final int MAX_POWER = 20;
    public static final long AVERAGE_INTER_REQUEST_TIME = 250;
    public static void main(String[] args) throws IOException {

        if (args.length != 2 && args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number> <id client>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String id = "";
        if(args.length == 3)
            id = args[2];
        boolean done = true;

        //Now we initialise the variables for the performances mesurements
        int current_request_number = 0;
        int max_request_number = 10;
        long time = 0;
        //For each line, the first column will be the response time.
        //The second column will be the time between a first request and the following one.
        //The third one is for the power computation
        //The fourth is for the server computation time
        long[][] performances_matrice = new long[max_request_number][4];

        String max_size = ""+MAX_SIZE;
        String max_power = ""+MAX_POWER;

        while(done && current_request_number < max_request_number){
            try{
                //First, wait before trying to connect again and request a computation
                double t = Math.random()*AVERAGE_INTER_REQUEST_TIME*2;
                long v = Math.round(t);
                //System.out.println("time: "+t+", in long: "+v);
                TimeUnit.MILLISECONDS.sleep(v);
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
                        //System.out.println("Server: " + fromServer);
                        if(fromServer.charAt(0) == '[' && current_request_number < performances_matrice.length){
                            performances_matrice[current_request_number][0] = System.currentTimeMillis() - time;
                            long elapsed_time_server = Long.parseLong(fromServer.split(" ")[1]);
                            performances_matrice[current_request_number][3] = elapsed_time_server;
                            current_request_number++;
                            break;
                        }
                        if (fromServer.equals("Bye.")){
                            done= false;
                            break;
                        }

                        /*if(firstTime){
                            System.out.print("Please enter the max size of the matrice: ");
                            max_size = stdIn.readLine();

                            System.out.print("Please enter the max difficulty: ");
                            max_power = stdIn.readLine();
                            firstTime = false;
                        }*/

                        if(max_size.equals("") || max_power.equals("")){
                            System.out.println("One of the two values is wrong, retry...");
                            break;
                        }
                        else if (max_size != null && max_power != null) {
                            //int size = (int) Math.ceil(Math.random()*Integer.parseInt(max_size));
                            int size = 50;
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

                            //System.out.println("Message : " + msg);

                            if(current_request_number > 0 && current_request_number < performances_matrice.length){
                                performances_matrice[current_request_number][1] = System.currentTimeMillis() - time;
                            }

                            performances_matrice[current_request_number][2] = power;
                            time = System.currentTimeMillis();
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

        //Now compute the means
        double response_time_mean = 0;
        double response_time_min = Integer.MAX_VALUE;
        double response_time_max = 0;

        double request_time_mean = 0;
        double request_time_min = Integer.MAX_VALUE;
        double request_time_max = 0;
        double power_mean = 0;

        double server_time_mean = 0;
        double server_time_min = Integer.MAX_VALUE;
        double server_time_max = 0;
        for(int i = 0; i < performances_matrice.length; i++){
            if(i>0)
            {
                request_time_mean+= (double) performances_matrice[i][1];
                if((double) performances_matrice[i][1] < request_time_min)
                    request_time_min = (double) performances_matrice[i][1];
                if((double) performances_matrice[i][1] > request_time_max)
                    request_time_max = (double) performances_matrice[i][1];

            }
            response_time_mean+= (double) performances_matrice[i][0];
                if((double) performances_matrice[i][0] < response_time_min)
                    response_time_min = (double) performances_matrice[i][0];
                if((double) performances_matrice[i][0] > response_time_max)
                    response_time_max = (double) performances_matrice[i][0];

            power_mean += (double) performances_matrice[i][2];

            server_time_mean+= (double) performances_matrice[i][3];
                if((double) performances_matrice[i][3] < server_time_min)
                    server_time_min = (double) performances_matrice[i][3];
                if((double) performances_matrice[i][3] > server_time_max)
                    server_time_max = (double) performances_matrice[i][3];

        }

        response_time_mean = response_time_mean/(performances_matrice.length-1);
        request_time_mean = request_time_mean/(performances_matrice.length-1);
        power_mean = power_mean/performances_matrice.length;
        server_time_mean = server_time_mean/(performances_matrice.length-1);

        System.out.println("ID:"+id+", p:"+power_mean);
        System.out.println("Response time mean: "+response_time_mean+", min:"+response_time_min+", max:"+response_time_max);
        System.out.println("Request time mean: "+request_time_mean+", min:"+request_time_min+", max:"+request_time_max);
        System.out.println("Server computation time mean: "+server_time_mean+", min:"+server_time_min+", max:"+server_time_max);
        System.out.println("Network time: "+(response_time_mean-server_time_mean));
    }
}
