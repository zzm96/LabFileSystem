/*
 * #%L
 * %%
 * Copyright (C) 2015 Trustsystems Desenvolvimento de Sistemas, LTDA.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the Trustsystems Desenvolvimento de Sistemas, LTDA. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package com.zzm.springmybatis.elfinder.service.impl;


import com.zzm.springmybatis.elfinder.ElFinderConstants;
import com.zzm.springmybatis.elfinder.configuration.ElfinderConfiguration;
import com.zzm.springmybatis.elfinder.core.Volume;
import com.zzm.springmybatis.elfinder.core.VolumeSecurity;
import com.zzm.springmybatis.elfinder.core.impl.DefaultVolumeSecurity;
import com.zzm.springmybatis.elfinder.core.impl.SecurityConstraint;
import com.zzm.springmybatis.elfinder.param.Node;
import com.zzm.springmybatis.elfinder.service.ElfinderStorage;
import com.zzm.springmybatis.elfinder.service.ElfinderStorageFactory;
import com.zzm.springmybatis.elfinder.service.VolumeSources;
import com.zzm.springmybatis.elfinder.support.locale.LocaleUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

public class DefaultElfinderStorageFactory implements ElfinderStorageFactory {

    ElfinderConfiguration elfinderConfiguration;

//    private ElfinderStorage elfinderStorage;

    private HashMap<String, ElfinderStorage> map;

    public DefaultElfinderStorageFactory(ElfinderConfiguration elfinderConfiguration) {
        this.elfinderConfiguration = elfinderConfiguration;
        this.map = new HashMap<>();
    }

    @Override
    public ElfinderStorage getVolumeSource() {
        HttpServletRequest httpServletRequestrequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id = (String) httpServletRequestrequest.getSession().getAttribute("diskFalg");
        String loginId = (String) httpServletRequestrequest.getSession().getAttribute("loginId");
        if (!map.containsKey(id)) map.put(id, getElfinderStorage(id, loginId));
        return map.get(id);
    }

    private ElfinderStorage getElfinderStorage(String id, String loginId) {
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();

        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnail().getWidth().intValue());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
        List<Node> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
        for (Node elfinderConfigurationVolume : elfinderConfigurationVolumes) {

            final String alias = elfinderConfigurationVolume.getAlias();
            final String path = elfinderConfigurationVolume.getPath() + "/" + id;
            final String source = elfinderConfigurationVolume.getSource();
            final String locale = elfinderConfigurationVolume.getLocale();
            boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

            if (!"0000".equals(loginId) && id.equals("shared")) {
                isLocked = true;
                isWritable = false;
            }
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) file.mkdir();
            // creates new volume
            Volume volume = VolumeSources.of(source).newInstance(alias, path);

            elfinderVolumes.add(volume);
            elfinderVolumeIds.put(volume, Character.toString(defaultVolumeId));
            elfinderVolumeLocales.put(volume, LocaleUtils.toLocale(locale));

            // creates security constraint
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setLocked(isLocked);
            securityConstraint.setReadable(isReadable);
            securityConstraint.setWritable(isWritable);

            // creates volume pattern and volume security
            final String volumePattern = Character.toString(defaultVolumeId) + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
            elfinderVolumeSecurities.add(new DefaultVolumeSecurity(volumePattern, securityConstraint));

            // prepare next volumeId character
            defaultVolumeId++;
        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }
}

