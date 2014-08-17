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

package library;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 *
 * @author shivanshu
 */
public class Library {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket libSocket = null;
        DataInputStream is = null;
        PrintStream os = null;
        BufferedReader input = null;
        BufferedReader console = null;
        try{
            libSocket = new Socket("127.0.0.1", 9500);
            is = new DataInputStream(libSocket.getInputStream());
            os = new PrintStream(libSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(is));
            console = new BufferedReader(new InputStreamReader(System.in));
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: hostname");
        }
        if (libSocket != null && os != null && is != null) {
            String state = "insert/search";
            String menu= "";
            String choice, response, name, parts[];
            try {
                loop:while(true){
                    switch(state){
                        case("username"):
                            menu = "Enter your name: ";
                            System.out.print(menu);
                            name = console.readLine();
                            state = "insert/search";
                        case("insert/search"):
                            menu = "1. Search a book\n";
                            menu += "2. Add a book\n";
                            menu += "3. Change User\n";
                            menu += "4. exit\n";
                            menu += "Enter your choice: ";
                            System.out.print(menu);
                            choice = console.readLine();
                            if(choice == "1"){
                                state = "search";
                            }
                            else if(choice == "2"){
                                state = "insert";
                            }
                            else if(choice == "3"){
                                state = "username";
                            }
                            else if(choice == "4"){
                                break loop;
                            }
                            else{
                                System.out.println("Invalid input");
                            }
                            break;
                        case("insert"):
                            menu = "Enter the name of the book: ";
                            System.out.print(menu);
                            choice = console.readLine();
                            os.println("1#"+choice);
                            if(input.readLine() == "Success"){
                                System.out.println("Success");
                            }
                            else{
                                System.out.println("Something went wrong. Try again!");
                            }
                            state = "insert/search";
                            break;
                        case("search"):
                            menu = "Enter the name of the book: ";
                            System.out.print(menu);
                            choice = console.readLine();
                            os.println("2#"+choice);
                            response = input.readLine();
                            if(response == "Not found"){
                                System.out.println("Not found!");
                            }
                            else{
                                String parts[] = response.split("#");
                                String print = "Name: "+parts[0];
                                print += "\nIssued: "+parts[1];
                                print += "\nIssued To: "+parts[2];
                                print += "\nIssued On: "+parts[3];
                                print += "\n Reserved By: "+parts[4];
                                System.out.println(print);
                            }
                            state = "reservse/renew";
                            break;
                        case("reserve/renew"):
                            menu = "1. Reserve the book";
                            break;
                    }
                } 
                os.println("1#abc"); 
                String responseLine = input.readLine();
                System.out.println("Server: " + responseLine);

                os.close();
                is.close();
                libSocket.close();   
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
    
}
