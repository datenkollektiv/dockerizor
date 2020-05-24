package com.eclipsesource.dockerizor

class DockerizorExtension {

    String maintainer
    String description

    String uri
    String repository
    String tag

    boolean dryRun
    boolean noCache

    boolean createLocalCopy

    String javaImage
    String virgoVersion
    String ciJobName
    String ciJobNumber
    String virgoFlavour
    String virgoHome
    String hostname
    Boolean removeAdminConsole
    Boolean removeSplash
    Boolean enableUserRegionOsgiConsole
    Boolean exposeHttpPort

    String[] pickupFiles
    String[] binFiles

    String entryPoint

    Closure postDockerizeHook = {
    }

    def postProcessor(DockerizorTask task) {
        postDockerizeHook(task)
    }

    String getShortName() {
        switch (virgoFlavour) {
            case 'VK':
                return "kernel"
            case 'VJS':
                return "jetty-server"
            case 'VTS':
                return "tomcat-server"
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }

    String getArchiveName() {
        switch (virgoFlavour) {
            case 'VK':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-kernel-latest"
                    default:
                    return "virgo-kernel-${virgoVersion}"
                }
            case 'VJS':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-jetty-server-latest"
                    default:
                    return "virgo-jetty-server-${virgoVersion}"
                }
            case 'VTS':
                switch (virgoVersion) {
                    case 'latest':
                    return "virgo-tomcat-server-latest"
                    default:
                    return "virgo-tomcat-server-${virgoVersion}"
                }
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }

    String getDownloadUrl() {
        switch (virgoFlavour) {
            case 'VK':
            case 'VJS':
            case 'VTS':
                switch (virgoVersion) {
                    case 'latest':
                    case ~/.*D-\d{14}/: 
                        if(!ciJobNumber) {
                            ciJobNumber='lastSuccessfulBuild'
                        }
                        return "https://ci.eclipse.org/virgo/job/${ciJobName}/${ciJobNumber}/artifact/packaging/${shortName}/build/distributions/${archiveName}.zip"
                    case ~/.*M\d{2}/:
                    return "https://www.eclipse.org/downloads/download.php?file=/virgo/milestone/${virgoFlavour}/${archiveName}.zip&mirror_id=580&r=1"
                    default:
                    return "https://www.eclipse.org/downloads/download.php?file=/virgo/release/VP/${virgoVersion}/${archiveName}.zip&mirror_id=580&r=1"
                }
            default:
                throw new IllegalArgumentException("Virgo flavour ${virgoFlavour} *not* supported")
        }
    }
}
