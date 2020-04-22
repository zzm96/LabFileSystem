package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.DiskCommon;
import com.zzm.springmybatis.elfinder.ElFinderConstants;
import com.zzm.springmybatis.elfinder.command.ElfinderCommand;
import com.zzm.springmybatis.elfinder.command.ElfinderCommandFactory;
import com.zzm.springmybatis.elfinder.core.ElfinderContext;
import com.zzm.springmybatis.elfinder.service.ElfinderStorageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
@RequestMapping("elfinder/connector")
public class CloudDiskController {

    private static final Logger logger = LoggerFactory.getLogger(CloudDiskController.class);

    @Resource(name = "commandFactory")
    private ElfinderCommandFactory elfinderCommandFactory;

    @Resource(name = "elfinderStorageFactory")
    private ElfinderStorageFactory elfinderStorageFactory;

    @RequestMapping
    public void connector(HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            request = new DiskCommon().processMultipartContent(request);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        request.getSession().setAttribute("diskFalg", request.getSession().getAttribute("loginId"));

        String cmd = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_COMMAND);
        ElfinderCommand elfinderCommand = elfinderCommandFactory.get(cmd);

        try {
            final HttpServletRequest protectedRequest = request;
            elfinderCommand.execute(new ElfinderContext() {
                @Override
                public ElfinderStorageFactory getVolumeSourceFactory() {
                    return elfinderStorageFactory;
                }

                @Override
                public HttpServletRequest getRequest() {
                    return protectedRequest;
                }

                @Override
                public HttpServletResponse getResponse() {
                    return response;
                }
            });
        } catch (Exception e) {
            logger.error("Unknown error", e);
        }
    }

}
