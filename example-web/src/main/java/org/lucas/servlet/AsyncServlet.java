package org.lucas.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by joy on 17-2-1.
 */
@WebServlet(asyncSupported = true) //是否支持异步
public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //开启异步servlet
        AsyncContext asyncContext = req.startAsync();
        new Thread(new  Executor(asyncContext)).start();
    }

    public class Executor implements Runnable{
        AsyncContext asyncContext;
        public Executor(AsyncContext asyncContext){
            this.asyncContext = asyncContext;
        }

        @Override
        public void run() {
            //...
        }
    }
}