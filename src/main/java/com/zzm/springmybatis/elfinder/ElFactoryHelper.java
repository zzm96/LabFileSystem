package com.zzm.springmybatis.elfinder;

import com.zzm.springmybatis.elfinder.configuration.ElfinderConfiguration;
import com.zzm.springmybatis.elfinder.core.Volume;
import com.zzm.springmybatis.elfinder.core.VolumeSecurity;
import com.zzm.springmybatis.elfinder.core.impl.DefaultVolumeSecurity;
import com.zzm.springmybatis.elfinder.core.impl.SecurityConstraint;
import com.zzm.springmybatis.elfinder.param.Node;
import com.zzm.springmybatis.elfinder.service.ElfinderStorage;
import com.zzm.springmybatis.elfinder.service.VolumeSources;
import com.zzm.springmybatis.elfinder.service.impl.DefaultElfinderStorage;
import com.zzm.springmybatis.elfinder.service.impl.DefaultThumbnailWidth;
import com.zzm.springmybatis.elfinder.support.locale.LocaleUtils;

import java.io.File;
import java.util.*;

/**
 * @author zzm
 * @data 2020/4/5 17:54
 */
public class ElFactoryHelper {

    private ElfinderConfiguration elfinderConfiguration = new ElfinderConfiguration();

    public ElfinderStorage getElfinderStorage(String loginId,String fileId) {
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
            final String path = elfinderConfigurationVolume.getPath() + "/" + fileId;
            final String source = elfinderConfigurationVolume.getSource();
            final String locale = elfinderConfigurationVolume.getLocale();
            boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();

            if(!loginId.equals(fileId)){
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
