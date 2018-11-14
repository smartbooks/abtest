package com.github.smartbooks.abtest.core.servlet;

import com.github.smartbooks.abtest.core.EchoExperimentFlowObserver;
import com.github.smartbooks.abtest.core.FlowMessage;
import com.github.smartbooks.abtest.core.FlowSubject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletFlowSubject extends HttpServlet {

    private final static FlowSubject flowSubject = new FlowSubject();

    @Override
    public void init() {
        flowSubject.attach(new EchoExperimentFlowObserver());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        notify(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        notify(req, resp);
    }

    private void notify(HttpServletRequest req, HttpServletResponse resp) {
        flowSubject.notify(new FlowMessage(req, resp));
    }

    @Override
    public void destroy() {
        flowSubject.detachAll();
    }

}
