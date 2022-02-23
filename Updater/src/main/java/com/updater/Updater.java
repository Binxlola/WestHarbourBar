package main.java.com.updater;

import javafx.application.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.TimerTask;

public class Updater {
//    private static Updater UPDATER;
//    private final App APP;
//
//    private final String owner = "Binxlola";
//    private final String repoName = "WestHarbourBar";
//    private final String token = "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";
//    private final String parentDirectory = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//    private final String updatesPath = parentDirectory + "/temp/";
//    private GitHub gitHub;
//    private GHRepository repository;
//
//    private TaskTimer parentTimer;

//    private Updater() throws URISyntaxException {
//        try {
//            this.gitHub = new GitHubBuilder().withOAuthToken(token).build();
//            this.repository = gitHub.getRepository(owner + "/" + repoName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        APP = App.getInstance();
//    }

//    public static Updater getInstance() {
//        if(UPDATER == null) {
//            try {
//                UPDATER = new Updater();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return UPDATER;
//    }

    /**
     * checks a GitHub repository for any releases. If there exists one or more releases,
     * a loop will be performed to find the most up-to-date release and return it.
     * @return The most up-to-date release
     */
//    private GHRelease findLatestRelease() {
//        Date currentReleaseDate = HibernateUtil.getAppData().getLastUpdated();
//        GHRelease latestRelease = null;
//
//        try {
//            for(GHRelease release : repository.listReleases()) {
//                Date releaseUpdatedDate = release.getUpdatedAt(); // The date the release was updated if any
//                Date releaseLatestDate = releaseUpdatedDate != null ? releaseUpdatedDate : release.getPublished_at(); // Latest date of change for the release
//                if(releaseLatestDate.after(currentReleaseDate)) {
//                    currentReleaseDate = releaseLatestDate;
//                    latestRelease = release;
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return latestRelease;
//    }

    /**
     * WIll will look to see if the set repository has a release that is later than this current release.
     * If one is found, the new up-to-date assets will be downloaded. Once downloaded any actions required to setup
     * the up-to-date release will take place.
     */
//    private void startUpdate() {
//        GHRelease release = findLatestRelease();
//
//        if(release != null) {
//            APP.prepareForUpdate();
//            try {
//                for(GHAsset asset : release.getAssets()) {
//                    copyURLToFile(new URL(asset.getBrowserDownloadUrl()), new File(updatesPath + asset.getName()));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Given a download URL and a File will download the file from the given URL and write it to the given File.
     * This is used for downloading release assets from a GitHub repository
     * @param url The download URL
     * @param file The file for the downloaded data to be written to.
     * @return A boolean stating if the download was successful.
     */
    public boolean copyURLToFile(URL url, File file) {

        boolean downloaded = false;

        try {
            InputStream input = url.openStream();
            if (file.exists()) {
                if (file.isDirectory())
                    throw new IOException("File '" + file + "' is a directory");

                if (!file.canWrite())
                    throw new IOException("File '" + file + "' cannot be written");
            } else {
                File parent = file.getParentFile();
                if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }

            input.close();
            output.close();

            downloaded = true;
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
        }

        return downloaded;
    }
}
