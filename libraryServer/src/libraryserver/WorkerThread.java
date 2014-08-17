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

package libraryserver;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author shivanshu
 */
public class WorkerThread implements Runnable{
    
    Comparator<Work> WorkComparator = new Comparator<Work>() {
        @Override
        public int compare(Work work1, Work work2){
            return work1.op <= work2.op ? 1 : -1;
        }
    };
    public PriorityBlockingQueue<Work> WorkQueue;
    Map<String,Book> books;

    WorkerThread(){
        this.WorkQueue = new PriorityBlockingQueue(10, WorkComparator);
        this.books = new HashMap<>();
    }
    
    @Override
    public void run(){
        while(true){
            Work remove = WorkQueue.poll();
            if(remove != null){
                switch(remove.op){
                    case(1):
                        books.put(remove.book.name, remove.book);
                        remove.os.println("Success");
                        break;
                    case(2):
                        Book get = books.get(remove.book.name);
                        if(get == null)
                            remove.os.println("Not found");
                        else
                            remove.os.println(remove.book.toString());
                        break;
                    case(3):
                    case(4):
                        books.put(remove.book.name, remove.book);
                        remove.os.println(remove.book.toString());
                        break;
                    default:
                        remove.os.println("Invalid Operation");
                }
                
                System.out.println("Work succeeded:"+remove.book.toString());
            }
        }
    }
    
    
}
