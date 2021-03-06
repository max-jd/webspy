package seospy.max_jd.seo.util.serializ.impl;

import org.apache.commons.lang3.ArrayUtils;
import seospy.max_jd.seo.entities.SeoEntity;
import seospy.max_jd.seo.util.serializ.interfaces.Exporter;
import seospy.max_jd.seo.util.serializ.interfaces.Loader;
import seospy.max_jd.seo.util.serializ.interfaces.Saver;

import java.io.*;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

public class ReadWrite implements Exporter, Loader, Saver {
    private String extensionExport = ".xlsx";
    private String extensionLoad = ".ser";
    private String extensionSave = ".ser";

    public void export(Deque<SeoEntity> dequeUrls, Set<SeoEntity> imagesSet, File fileToWrite) {
        SeoEntity[] arraySeoUrls = new SeoEntity[dequeUrls.size()];
        dequeUrls.toArray(arraySeoUrls);
        SeoEntity[] arraySeoImages = new SeoEntity[imagesSet.size()];
        imagesSet.toArray(arraySeoImages);

        SeoEntity[] joinedSeoArray = ArrayUtils.addAll(arraySeoUrls, arraySeoImages);
        ExcelWriter.writeToFile(fileToWrite.toPath(), joinedSeoArray);
    }

    public void loadFrom(Deque<SeoEntity> dequeUrls, Set<SeoEntity> imagesSet, File fileFrom) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(fileFrom);
             ObjectInputStream inStreamOb = new ObjectInputStream(fileInputStream)) {

            Deque<SeoEntity> tempCopyDeque = (Deque) inStreamOb.readObject();
            dequeUrls.addAll(tempCopyDeque);
            Set<SeoEntity> tempCopySet = (Set<SeoEntity>) inStreamOb.readObject();
            imagesSet.addAll(tempCopySet);
            SeoEntity.statisticLinksOn = (Map<String, Set<String>>) inStreamOb.readObject();
            SeoEntity.statisticLinksOut = (Map<String, Set<String>>) inStreamOb.readObject();
            SeoEntity.externalLinks = (Map<String, Set<String>>) inStreamOb.readObject();
            SeoEntity.cacheContentTypePages = (Map<String, String>) inStreamOb.readObject();
        }
    }

    public void saveTo(Deque<SeoEntity> dequeUrls, Set<SeoEntity> imagesSet, File fileTo) throws IOException, ClassNotFoundException {

        try (FileOutputStream fileOut = new FileOutputStream(fileTo, false);
             ObjectOutputStream objectsOutput = new ObjectOutputStream(fileOut)) {
            objectsOutput.writeObject(dequeUrls);
            objectsOutput.writeObject(imagesSet);
            objectsOutput.writeObject(SeoEntity.statisticLinksOn);
            objectsOutput.writeObject(SeoEntity.statisticLinksOut);
            objectsOutput.writeObject(SeoEntity.externalLinks);
            objectsOutput.writeObject(SeoEntity.cacheContentTypePages);
        }
    }

    public String getExtensionExport() {
        return extensionExport;
    }

    public String getExtensionForLoad() {
        return extensionLoad;
    }

    public String getExtensionToSave() {
        return extensionSave;
    }
}


