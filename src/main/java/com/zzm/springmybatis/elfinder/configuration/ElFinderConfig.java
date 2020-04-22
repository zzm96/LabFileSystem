package com.zzm.springmybatis.elfinder.configuration;

import com.zzm.springmybatis.elfinder.command.CommandFactory;
import com.zzm.springmybatis.elfinder.service.ElfinderStorageFactory;
import com.zzm.springmybatis.elfinder.service.impl.DefaultElfinderStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElFinderConfig {

    @Autowired
    private ElfinderConfiguration elfinderConfiguration;

    @Bean(name = "commandFactory")
    public CommandFactory getCommandFactory() {
        CommandFactory commandFactory = new CommandFactory();
        commandFactory.setClassNamePattern(elfinderConfiguration.getCommand() + ".%sCommand");
        return commandFactory;
    }

    @Bean(name = "elfinderStorageFactory")
    public ElfinderStorageFactory getElfinderStorageFactory() {
        DefaultElfinderStorageFactory elfinderStorageFactory = new DefaultElfinderStorageFactory(elfinderConfiguration);
        return elfinderStorageFactory;
    }


}
