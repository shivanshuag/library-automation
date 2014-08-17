/*
 * The MIT License
 *
 * Copyright 2014 shivanshu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 Protocol - if the first character is
 '1' - add book the the data structure
 '2' - search book
 '3' - issue book
 '4' - reserve
*/
package libraryserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author shivanshu
 */
public class LibraryServer implements Runnable{
    
    Socket csocket;
    WorkerThread worker;

    LibraryServer(Socket csocket, WorkerThread worker) {
        this.csocket = csocket;
        this.worker = worker;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ServerSocket libServer;
        Socket csocket = null;
        DataInputStream is;
        PrintStream os;
        try {
            libServer = new ServerSocket(9500);
            System.out.println("Listening");
            WorkerThread worker = new WorkerThread();
            new Thread(worker).start();
            while(true){
                csocket = libServer.accept();
                System.out.println("Connected");
                new Thread(new LibraryServer(csocket, worker)).start();
            }
        }
        catch (IOException e) {
           System.out.println(e);
        }

    }
    
    @Override
    public void run() {
      try {
         PrintStream os = new PrintStream(csocket.getOutputStream());
         DataInputStream is = new DataInputStream(csocket.getInputStream());
         BufferedReader input = new BufferedReader(new InputStreamReader(is));
         while(true){
             String line = input.readLine();
             if(line == null)
                break;
             handletask(line, os);
             System.out.println(line);
             if(line.length() == 0)
                 break;
         }
         System.out.println("Connection Closed from a client");
         os.close();
         is.close();
         csocket.close();
      }
      catch (IOException e) {
         System.out.println(e);
      }
   }
    
   public void handletask(String input, PrintStream os){
       System.out.println("handling task");
       String[] parts = input.split("#");
       Work newWork;
       switch(parts[0]){
           case("1"):
               Book insert = new Book(parts[1], false, "", new Date(), "");
               newWork = new Work(insert, 1, os);
               worker.WorkQueue.put(newWork);
               break;
            case("2"):
                Book search = new Book(parts[1], false, "", new Date(), "");
                newWork = new Work(search, 2, os);
                worker.WorkQueue.put(newWork);
                break;
            case("3"):
                Book issue = new Book(parts[1], Boolean.valueOf(parts[2]), parts[3], new Date(), "");
                newWork = new Work(issue, 3, os);
                worker.WorkQueue.put(newWork);
                break;                
            case("4"):
                Book reserve = new Book(parts[1], Boolean.valueOf(parts[2]), parts[3], new Date(), parts[4]);
                newWork = new Work(reserve, 4, os);
                worker.WorkQueue.put(newWork);
                break;
            default:
                os.println("Invalid Operation");                
       }  
   }
}
